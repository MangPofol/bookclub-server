package mangpo.server.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mangpo.server.entity.*;
import mangpo.server.service.*;
import mangpo.server.session.SessionConst;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
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
    private final PostClubScopeService pscService;

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
        log.info("posts={}", posts);

        if (clubId != -1) {
            Club clubRequest = clubService.findClub(clubId);
            log.info("clubRequest={}", clubRequest);

            Iterator<Post> iter = posts.iterator();

            while(iter.hasNext()){
                Post p = iter.next();
                log.info("post={}", p);
                if (p.getScope() == PostScope.CLUB) {
                    List<PostClubScope> listByPost = pscService.findListByPost(p);

                    boolean present = listByPost.stream()
                            .anyMatch(m -> m.getClub() == clubRequest);

                    if (!present)
                        iter.remove();
                }

            }

        }
        List<PostResponseDto> collect = posts.stream()
                .map(PostResponseDto::new)
                .collect(Collectors.toList());

        return new Result(collect);
    }

    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(@RequestBody PostRequestDto requestDto, UriComponentsBuilder b) {
        Book requestBook = bookService.findBook(requestDto.getBookId());
        Post post = requestDto.toEntityExceptBook();
        post.changeBook(requestBook);
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

            Long pcsId = pscService.createPCS(pcs);
        }


    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
        Post post = postService.findPost(id);
        pscService.deleteAllPcsByPost(post);
        postService.deletePost(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updatePost(@PathVariable Long id, @RequestBody PostRequestDto requestDto) {
        Post post = postService.findPost(id);
        PostScope ogScope = post.getScope();


        if (ogScope == PostScope.CLUB) {
            pscService.deleteAllPcsByPost(post);
        }
        if (requestDto.getScope() == PostScope.CLUB)
            createAndPersistPostClubScope(requestDto, post);


        Post requestEntity = requestDto.toEntityExceptBook();
        postService.updatePost(id, requestEntity);

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

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class Result<T> {
        private T data;
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PostResponseDto {
        private Long id;
        private PostType type;
        private PostScope scope;
        private Boolean isIncomplete;
        private String imgLocation;
        private String title;
        private String content;
        private LocalDateTime createdDate;
        private LocalDateTime modifiedDate;
        private List<LikedResponseDto> likedList;
        private List<CommentResponseDto> commentsDto;

        //        @QueryProjection
        public PostResponseDto(Post post) {
            this.id = post.getId();
            this.type = post.getType();
            this.scope = post.getScope();
            this.isIncomplete = post.getIsIncomplete();
            this.imgLocation = post.getImgLocation();
            this.title = post.getTitle();
            this.content = post.getContent();
            this.createdDate = post.getCreatedDate();
            this.modifiedDate = post.getModifiedDate();

            this.likedList = post.getLikedList()
                    .stream()
                    .map(m -> new LikedResponseDto(m.getUser().getNickname(), m.getIsLiked()))
                    .collect(Collectors.toList());

            this.commentsDto = post.getComments()
                    .stream()
                    .map(CommentResponseDto::new)
                    .collect(Collectors.toList());
        }
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class LikedResponseDto {
        private String userNickname;
        private Boolean isLiked;
    }

    @Data
    static class PostRequestDto {
        private Long bookId;
        private PostType type;
        private PostScope scope;
        private Boolean isIncomplete;
        private String imgLocation;
        private String title;
        private String content;
        private List<Long> clubIdListForScope;

        public Post toEntityExceptBook() {
            return Post.builder()
                    .type(this.type)
                    .scope(this.scope)
                    .isIncomplete(this.isIncomplete)
                    .imgLocation(this.imgLocation)
                    .title(this.title)
                    .content(this.content)
                    .build();
        }
    }

    @Data
    static class CommentResponseDto {
        private Long commentId;
        private Long parentCommentId;
        private String userNickname;
        private String content;
        private LocalDateTime createdDate;
        private LocalDateTime modifiedDate;

        public CommentResponseDto(Comment comment) {
            this.commentId = comment.getId();
            this.parentCommentId = comment.getParentCommentId();
            this.userNickname = comment.getUser().getNickname();
            this.content = comment.getContent();
            this.createdDate = comment.getCreatedDate();
            this.modifiedDate = comment.getModifiedDate();
        }
    }

}
