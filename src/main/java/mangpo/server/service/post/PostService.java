package mangpo.server.service.post;

import lombok.RequiredArgsConstructor;
import mangpo.server.dto.post.LinkRequestDto;
import mangpo.server.dto.post.PostRequestDto;
import mangpo.server.entity.*;
import mangpo.server.entity.book.Book;
import mangpo.server.entity.post.*;
import mangpo.server.entity.user.User;
import mangpo.server.repository.*;
import mangpo.server.service.ClubBookUserService;
import mangpo.server.service.LikedService;
import mangpo.server.service.book.BookService;
import mangpo.server.service.club.ClubService;
import mangpo.server.service.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
//    private final CommentService commentService;
    private final ClubBookUserService cbuService;
    private final ClubService clubService;
    private final PostClubScopeService pcsService;
    private final BookService bookService;
    private final UserService userService;
    private final LikedService likedService;

    //TODO:
    // 이렇게 의존성이 많은 서비스는 어떻게 테스트를 작성해야 하지?
    // 이걸 다 mocking 할 수 있는건가?
    // 애초에 이렇게 많이 가져다 쓰는게 틀린건가?

    @Transactional
    public Long createPost(PostRequestDto postRequestDto){
        Book requestBook = bookService.findBookById(postRequestDto.getBookId());
        Post post = postRequestDto.toEntityExceptBookAndUser();
        post.changeBook(requestBook);


        addLinks(postRequestDto, post);
//        post.addLink(postRequestDto.get)
        addPostImageLocations(postRequestDto, post);

//        Long postId = postService.createPost(post);
        User user = userService.findUserFromToken();
        post.addUser(user);

        postRepository.save(post);

        if (postRequestDto.getScope() == PostScope.CLUB)
            createAndPersistPostClubScope(postRequestDto.getClubIdListForScope(), post);

//        postRepository.save(post);
        return post.getId();
    }

    private void addLinks(PostRequestDto postRequestDto, Post post) {
        for(LinkRequestDto l : postRequestDto.getLinkRequestDtos()){
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
    public void updatePost(Long postId, PostRequestDto postRequestDto){
        Post post = findPostFetchJoinImgLoc(postId);
        PostScope ogScope = post.getScope();

        //remove all postImageLoc, then add all
        post.getPostImageLocations().clear();
        addPostImageLocations(postRequestDto, post);



        if (ogScope == PostScope.CLUB) {
            pcsService.deleteAllPcsByPost(post);
        }
        if (postRequestDto.getScope() == PostScope.CLUB)
            createAndPersistPostClubScope(postRequestDto.getClubIdListForScope(), post);



        //remove all links, then add all
        post.getLinks().clear();
        addLinks(postRequestDto, post);

        post.update(postRequestDto);
    }

    private void createAndPersistPostClubScope( List<Long> clubIdListForScope, Post post) {
        for (Long clubId : clubIdListForScope) {
            Club club = clubService.findById(clubId);

            club.updateLastAddBookDate();

            PostClubScope pcs = PostClubScope.builder()
                    .post(post)
                    .club(club)
                    .clubName(club.getName())
                    .build();

            pcsService.createPCS(pcs);
        }
    }

    @Transactional
    public void deletePostById(Long postId){
        Post post = findPostById(postId);

//        pcsService.deleteAllPcsByPost(post);
//        likedService.deleteByPost(post);

        List<Liked> likedList = likedService.findAllByPost(post);
        likedService.deleteAll(likedList);

        List<PostClubScope> pcsList = pcsService.findAllByPost(post);
        pcsService.deleteAll(pcsList);
        //순환참조 방지용  repository 호출
        List<Comment> commentList = commentRepository.findAllByPost(post);
        commentRepository.deleteAll(commentList);

        postRepository.deleteById(postId);
    }

    @Transactional
    public void deleteAll(List<Post> posts){
        postRepository.deleteAll(posts);
    }

    public void deleteAllWithCascade(List<Post> posts){
        for (Post post : posts) {
            deletePostById(post.getId());
        }
    }

    public Post findPostById(Long id){
        return postRepository.findById(id).orElseThrow(()->  new EntityNotFoundException("존재하지 않는 포스트입니다."));
    }

    public Post findPostFetchJoinImgLoc(Long id){
        return postRepository.findFetchById(id).orElseThrow(()->  new EntityNotFoundException("존재하지 않는 포스트입니다."));
    }

    public List<Post> findPostsByBookId(Long bookId){
        Book book = bookService.findBookById(bookId);

        List<Post> byBook = postRepository.findByBook(book);

        return byBook;
    }

    @Transactional
    public void doLikePost(Long postId){
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
    public void undoLikePost(Long postId){
        User user = userService.findUserFromToken();
        Post post = findPostById(postId);

        Liked liked = likedUserFromPost(user, post);
        liked.undoLikeToPost(post);

        likedService.deleteLiked(liked);
    }

    public Integer findTotalCount(){
        User user = userService.findUserFromToken();

        List<ClubBookUser> cbuList = cbuService.findByUserAndClubIsNull(user);
        List<Book> books = cbuList.stream()
                .map(m -> m.getBook())
                .collect(Collectors.toList());

        int sum = 0;
        for (Book book : books)
            sum += postRepository.countByBook(book);
        return sum;
    }

    private Liked likedUserFromPost(User user, Post post) {
        List<Liked> collect = post.getLikedList().stream()
                .filter(l -> l.getUser().getId().equals(user.getId()))
                .collect(Collectors.toList());
        Liked liked = collect.get(0);
        return liked;
    }


    public List<Post> findPostsByBookIdAndClubScope(Long bookId, Long clubId) {
        List<Post> posts = findPostsByBookId(bookId);

        if (clubId != -1)
            excludePostByClubScope(clubId, posts);

        return posts;
    }

    private void excludePostByClubScope(Long clubId, List<Post> posts) {
        Club clubRequest = clubService.findById(clubId);

        Iterator<Post> iter = posts.iterator();

        while(iter.hasNext()){
            Post p = iter.next();

            if(p.getScope() == PostScope.PRIVATE)
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

    public List<Post> findListByBook(Book b){
        return postRepository.findListByBook(b);
    }

}
