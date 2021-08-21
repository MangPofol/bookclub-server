package mangpo.server.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import mangpo.server.entity.*;
import mangpo.server.service.BookService;
import mangpo.server.service.PostService;
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

    @GetMapping
    public Result<List<GetPostsResponseDto>> getPostsByBookId(@RequestParam Long bookId){
        List<Post> posts = postService.findPostsByBookId(bookId);

        List<GetPostsResponseDto> collect = posts.stream()
                .map(GetPostsResponseDto::new)
                .collect(Collectors.toList());

        return new Result(collect);
    }

    @PostMapping
    public ResponseEntity<Long> createPost(@RequestBody PostRequestDto requestDto, UriComponentsBuilder b){
        Book requestBook = bookService.findBook(requestDto.getBookId());
        Post post = requestDto.toEntityExceptBook();
        post.changeBook(requestBook);
        Long postId = postService.createPost(post);

        UriComponents uriComponents =
                b.path("/posts/{postId}").buildAndExpand(postId);

        return ResponseEntity.created(uriComponents.toUri()).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id){
        postService.deletePost(id);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updatePost(@PathVariable Long id, @RequestBody PostRequestDto requestDto){
        Post requestEntity = requestDto.toEntityExceptBook();
        postService.updatePost(id,requestEntity);

        return ResponseEntity.ok().build();
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
    static class GetPostsResponseDto {
        private Long id;
        private PostType type;
        private PostScope scope;
        private Boolean isIncomplete;
        private String imgLocation;
        private String title;
        private String content;
        private LocalDateTime createdDate;
        private LocalDateTime modifiedDate;

        public GetPostsResponseDto(Post post){
            this.id = post.getId();
            this.type = post.getType();
            this.scope = post.getScope();
            this.isIncomplete = post.getIsIncomplete();
            this.imgLocation = post.getImgLocation();
            this.title = post.getTitle();
            this.content = post.getContent();
            this.createdDate = post.getCreatedDate();
            this.modifiedDate = post.getModifiedDate();
        }
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
