package mangpo.server.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mangpo.server.dto.post.PostRequestDto;
import mangpo.server.dto.post.PostResponseDto;
import mangpo.server.dto.Result;
import mangpo.server.entity.*;
import mangpo.server.service.*;
import mangpo.server.service.book.BookService;
import mangpo.server.session.SessionConst;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final BookService bookService;
    private final ClubService clubService;
    private final LikedService likedService;
    private final CommentService commentService;
    private final PostClubScopeService pcsService;

    //Todo dto로 직접 조회 고려
//    @GetMapping
//    public Result<List<PostResponseDto>> getPostsByBookId(@RequestParam Long bookId) {
//        List<Post> posts = postService.findPostsByBookId(bookId);
//
//        List<PostResponseDto> collect = posts.stream()
//                .map(PostResponseDto::new)
//                .collect(Collectors.toList());
//
//        return new Result(collect);
//    }

    @GetMapping
    public Result<List<PostResponseDto>> getPostsByBookIdAndClubScope(@RequestParam Long bookId, @RequestParam(defaultValue = "-1") Long clubId) {
        List<Post> posts = postService.findPostsByBookId(bookId);
//        log.info("posts={}", posts);

        if (clubId != -1)
            excludePostByClubScope(clubId, posts);

        List<PostResponseDto> collect = new ArrayList<>();
        createPostResponseDto(posts, collect);

        return new Result<>(collect);
    }

    private void createPostResponseDto(List<Post> posts, List<PostResponseDto> collect) {
        for (Post post : posts) {
            PostResponseDto postResponseDto = new PostResponseDto(post);
            PostScope scope = post.getScope();

            if (scope.equals(PostScope.CLUB)){
                List<PostClubScope> listByPost = pcsService.findListByPost(post);

                for (PostClubScope pcs : listByPost) {
                    postResponseDto.addPostScopeClub(pcs.getId(), pcs.getClubName());
                }
            }
            collect.add(postResponseDto);
        }
    }

    private void excludePostByClubScope(Long clubId, List<Post> posts) {
        Club clubRequest = clubService.findClub(clubId);

        Iterator<Post> iter = posts.iterator();

        while(iter.hasNext()){
            Post p = iter.next();
            if (p.getScope() == PostScope.CLUB) {
                List<PostClubScope> listByPost = pcsService.findListByPost(p);

                boolean present = listByPost.stream()
                        .anyMatch(m -> m.getClub() == clubRequest);

                if (!present)
                    iter.remove();
            }
        }
    }

    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(@RequestBody PostRequestDto requestDto, UriComponentsBuilder b) {
        Book requestBook = bookService.findBook(requestDto.getBookId());
        Post post = requestDto.toEntityExceptBook();
        post.changeBook(requestBook);

        for (String imgLoc : requestDto.getPostImgLocations()) {
            PostImageLocation postImageLocation = PostImageLocation.builder()
                    .post(post)
                    .imgLocation(imgLoc)
                    .build();

            post.getPostImageLocations().add(postImageLocation);
        }

        Long postId = postService.createPost(post);

        if (requestDto.getScope() == PostScope.CLUB)
            createAndPersistPostClubScope(requestDto, post);

        UriComponents uriComponents =
                b.path("/posts/{postId}").buildAndExpand(postId);

        PostResponseDto postResponseDto = new PostResponseDto(post);

        return ResponseEntity.created(uriComponents.toUri()).body(postResponseDto);
    }

    private void createAndPersistPostClubScope(PostRequestDto requestDto, Post post) {

        List<Long> clubIdListForScope = requestDto.getClubIdListForScope();

        for (Long clubId : clubIdListForScope) {
            Club club = clubService.findClub(clubId);

            PostClubScope pcs = PostClubScope.builder()
                    .post(post)
                    .club(club)
                    .clubName(club.getName())
                    .build();

            Long pcsId = pcsService.createPCS(pcs);
        }


    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
        Post post = postService.findPost(id);
        pcsService.deleteAllPcsByPost(post);
        likedService.deleteByPost(post);
        postService.deletePost(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(@PathVariable Long id, @RequestBody PostRequestDto requestDto) {
        Post post = postService.findPostFetchJoinImgLoc(id);
        PostScope ogScope = post.getScope();

        post.getPostImageLocations().clear();

        for (String s:  requestDto.getPostImgLocations()) {
            PostImageLocation p = PostImageLocation.builder()
                    .post(post)
                    .imgLocation(s)
                    .build();

            post.getPostImageLocations().add(p);
        }

        if (ogScope == PostScope.CLUB) {
            pcsService.deleteAllPcsByPost(post);
        }
        if (requestDto.getScope() == PostScope.CLUB)
            createAndPersistPostClubScope(requestDto, post);

        postService.updatePost(id, requestDto);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{postId}/do-like")
    public ResponseEntity<?> doLikePost(@PathVariable Long postId, @SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser) {
        Post post = postService.findPost(postId);

        Liked liked = Liked.builder()
                .user(loginUser)
                .isLiked(true)
                .build();
        liked.doLikeToPost(post);
        likedService.createLiked(liked);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{postId}/undo-like")
    public ResponseEntity<?> undoLikeBook(@PathVariable Long postId, @SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser) {
        Post post = postService.findPost(postId);

        List<Liked> collect = post.getLikedList().stream()
                .filter(l -> l.getUser().getId().equals(loginUser.getId()))
                .collect(Collectors.toList());

        Liked liked = collect.get(0);
        liked.undoLikeToPost(post);
        likedService.deleteLiked(liked);

        return ResponseEntity.noContent().build();
    }



}
