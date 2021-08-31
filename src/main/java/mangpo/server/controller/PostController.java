package mangpo.server.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import mangpo.server.entity.*;
import mangpo.server.service.BookService;
import mangpo.server.service.LikedService;
import mangpo.server.service.PostService;
import mangpo.server.session.SessionConst;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final BookService bookService;
    private final LikedService likedService;

    @GetMapping
    public Result<List<PostResponseDto>> getPostsByBookId(@RequestParam Long bookId){
        List<Post> posts = postService.findPostsByBookId(bookId);

        List<PostResponseDto> collect = posts.stream()
                .map(PostResponseDto::new)
                .collect(Collectors.toList());

        return new Result(collect);
    }

    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(@RequestBody PostRequestDto requestDto, UriComponentsBuilder b){
        Book requestBook = bookService.findBook(requestDto.getBookId());
        Post post = requestDto.toEntityExceptBook();
        post.changeBook(requestBook);
        Long postId = postService.createPost(post);

        UriComponents uriComponents =
                b.path("/posts/{postId}").buildAndExpand(postId);

        PostResponseDto postResponseDto = new PostResponseDto(post);

        return ResponseEntity.created(uriComponents.toUri()).body(postResponseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id){
        postService.deletePost(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updatePost(@PathVariable Long id, @RequestBody PostRequestDto requestDto){
        Post requestEntity = requestDto.toEntityExceptBook();
        postService.updatePost(id,requestEntity);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{postId}/do-like")
    public ResponseEntity<?> doLikePost(@PathVariable Long postId, @SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser){
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
    public ResponseEntity<?> undoLikeBook(@PathVariable Long postId, @SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser){
        Post post = postService.findPost(postId);

        List<Liked> collect = post.getLikedList().stream()
                .filter(l -> l.getUser().getId() == loginUser.getId())
                .collect(Collectors.toList());

        Liked liked = collect.get(0);
        liked.undoLikeToPost(post);
        likedService.deleteLiked(liked);

        return ResponseEntity.noContent().build();
    }



    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class Result<T>{
        private T data;
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class PostResponseDto {
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


        public PostResponseDto(Post post){
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
                    .map(m-> new LikedResponseDto(m.getUser().getNickname(),m.getIsLiked()))
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

        public Post toEntityExceptBook(){
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


}
