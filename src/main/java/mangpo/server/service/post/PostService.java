package mangpo.server.service.post;

import lombok.RequiredArgsConstructor;
import mangpo.server.dto.post.LinkRequestDto;
import mangpo.server.dto.post.PostRequestDto;
import mangpo.server.entity.book.Book;
import mangpo.server.entity.cbu.ClubBookUser;
import mangpo.server.entity.club.Club;
import mangpo.server.entity.post.*;
import mangpo.server.entity.user.Liked;
import mangpo.server.entity.user.User;
import mangpo.server.repository.post.CommentRepository;
import mangpo.server.repository.post.PostRepository;
import mangpo.server.service.book.BookService;
import mangpo.server.service.cbu.ClubBookUserService;
import mangpo.server.service.club.ClubService;
import mangpo.server.service.user.LikedService;
import mangpo.server.service.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    private final ClubBookUserService cbuService;
    private final ClubService clubService;
    private final PostClubScopeService pcsService;
    private final BookService bookService;
    private final UserService userService;
    private final LikedService likedService;

    @Transactional
    public Long createPost(PostRequestDto postRequestDto) {
        Book book = bookService.findBookById(postRequestDto.getBookId());
        Post post = postRequestDto.toEntityExceptBookAndUser();
        post.changeBook(book);

        addLinks(postRequestDto, post);
        addPostImageLocations(postRequestDto, post);

        User user = userService.findUserFromToken();
        post.addUser(user);

        postRepository.save(post);

        if (postRequestDto.getScope() == PostScope.CLUB) {
            addPostToClubAndBookIfNotExists(postRequestDto, book, post, user);
        }

        return post.getId();
    }

    private void addPostToClubAndBookIfNotExists(PostRequestDto postRequestDto, Book book, Post post, User user) {
        for (Long clubId : postRequestDto.getClubIdListForScope()) {
            Club club = clubService.findById(clubId);
            if (!clubService.isClubContainsBook(club, book, user)) {
                //cbu에 book 추가, 관련 메세지, pcs 추가
                clubService.addClubToUserBook(club.getId(), book.getId());
            }

            club.updateLastAddBookDate();
            createPostClubScope(club, post);
        }
    }

    private void createPostClubScope(Club club, Post post) {
        PostClubScope pcs = PostClubScope.builder()
                .post(post)
                .club(club)
                .clubName(club.getName())
                .build();

        pcsService.createPCS(pcs);
    }

    private void addLinks(PostRequestDto postRequestDto, Post post) {
        for (LinkRequestDto l : postRequestDto.getLinkRequestDtos()) {
            Link link = Link.builder()
                    .post(post)
                    .hyperlinkTitle(l.getHyperlinkTitle())
                    .hyperlink(l.getHyperlink())
                    .build();

            post.addLink(link);
        }
    }

    private void addPostImageLocations(PostRequestDto postRequestDto, Post post) {
        for (String imgLoc : postRequestDto.getPostImgLocations()) {
            PostImageLocation postImageLocation = PostImageLocation.builder()
                    .post(post)
                    .imgLocation(imgLoc)
                    .build();

            post.getPostImageLocations().add(postImageLocation);
        }
    }

    @Transactional
    public void updatePost(Long postId, PostRequestDto postRequestDto) {
        Post post = findPostWithImgLoc(postId);
        PostScope ogScope = post.getScope();

        //remove all postImageLoc, then add all
        post.getPostImageLocations().clear();
        addPostImageLocations(postRequestDto, post);


        if (ogScope == PostScope.CLUB) {
            List<Club> clubScopeListOfDeletedPost = pcsService.findAllByPost(post).stream()
                    .map(PostClubScope::getClub)
                    .collect(Collectors.toList());

            pcsService.deleteAllPcsByPost(post);
            deletePostAndBookFromClubIfNoPostExists(post, clubScopeListOfDeletedPost);
        }

        if (postRequestDto.getScope() == PostScope.CLUB) {
            addPostToClubAndBookIfNotExists(postRequestDto, post.getBook(), post, userService.findUserFromToken());
        }

        //remove all links, then add all
        post.getLinks().clear();
        addLinks(postRequestDto, post);

        post.update(postRequestDto);
    }

    @Transactional
    public void deletePostById(Long postId) {
        Post post = findPostById(postId);

        List<Liked> likedList = likedService.findAllByPost(post);
        likedService.deleteAll(likedList);


        if (post.getScope() == PostScope.CLUB) {
            List<Club> clubScopeListOfDeletedPost = pcsService.findAllByPost(post).stream()
                    .map(PostClubScope::getClub)
                    .collect(Collectors.toList());

            pcsService.deleteAllPcsByPost(post);
            deletePostAndBookFromClubIfNoPostExists(post, clubScopeListOfDeletedPost);
        }

        //순환참조 방지용  repository 호출
        List<Comment> commentList = commentRepository.findListByPost(post);
        commentRepository.deleteAll(commentList);

        postRepository.deleteById(postId);
    }

    private void deletePostAndBookFromClubIfNoPostExists(Post post, List<Club> clubScopeListOfDeletedPost) {
        Set<Club> clubSet = new HashSet<>();

        for (Post p : findListByBook(post.getBook())) {
            for (PostClubScope pcs : pcsService.findAllByPost(p)) {
                clubSet.add(pcs.getClub());
            }
        }

        for (Club deletedClub : clubScopeListOfDeletedPost) {
            if (!clubSet.contains(deletedClub)) {
                ClubBookUser cbu = cbuService.findByClubAndBookAndUser(deletedClub, post.getBook(), userService.findUserFromToken());
                cbuService.deleteCbu(cbu);
            }
        }
    }

    @Transactional
    public void deleteAll(List<Post> posts) {
        postRepository.deleteAll(posts);
    }

    public void deleteAllWithCascade(List<Post> posts) {
        for (Post post : posts) {
            deletePostById(post.getId());
        }
    }

    public Post findPostById(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 포스트입니다."));
    }

    public Post findPostWithImgLoc(Long id) {
        return postRepository.findFetchById(id).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 포스트입니다."));
    }

    public List<Post> findListByBookId(Long bookId) {
        Book book = bookService.findBookById(bookId);
        return postRepository.findByBook(book);
    }

    @Transactional
    public void doLikePost(Long postId) {
        User user = userService.findUserFromToken();
        Post post = findPostById(postId);

        Liked liked = Liked.builder()
                .user(user)
                .isLiked(true)
                .build();
        liked.doLikeToPost(post);

        likedService.createLiked(liked);
    }

    @Transactional
    public void undoLikePost(Long postId) {
        User user = userService.findUserFromToken();
        Post post = findPostById(postId);

        Liked liked = findLikedWithUserAndPost(user, post);
        liked.undoLikeToPost(post);

        likedService.deleteLiked(liked);
    }

    public Integer findTotalCountOfUser() {
        User user = userService.findUserFromToken();

        List<ClubBookUser> cbuList = cbuService.findByUserAndClubIsNull(user);
        List<Book> books = cbuList.stream()
                .map(ClubBookUser::getBook)
                .collect(Collectors.toList());

        int sum = 0;
        for (Book book : books)
            sum += postRepository.countByBook(book);
        return sum;
    }

    private Liked findLikedWithUserAndPost(User user, Post post) {
        return post.getLikedList().stream()
                .filter(l -> l.getUser().getId().equals(user.getId()))
                .findAny()
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 Liked 정보입니다."));
    }


    public List<Post> findPostsByBookIdAndClubScope(Long bookId, Long clubId) {
        List<Post> posts = findListByBookId(bookId);

        if (clubId != null)
            excludePostByClubScope(clubId, posts);

        return posts;
    }

    private void excludePostByClubScope(Long clubId, List<Post> posts) {
        Club clubRequest = clubService.findById(clubId);

        Iterator<Post> iter = posts.iterator();
        while (iter.hasNext()) {
            Post p = iter.next();

            if (p.getScope() == PostScope.PRIVATE)
                iter.remove();
            else if (p.getScope() == PostScope.CLUB) {
                List<PostClubScope> listByPost = pcsService.findListWithClubByPost(p);

                boolean present = listByPost.stream()
                        .anyMatch(m -> m.getClub() == clubRequest);

                if (!present)
                    iter.remove();
            }
        }
    }

    public List<Post> findListByBook(Book b) {
        return postRepository.findListByBook(b);
    }

}
