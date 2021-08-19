package mangpo.server.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import mangpo.server.entity.*;
import mangpo.server.service.BookService;
import mangpo.server.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final BookService bookService;

    @GetMapping
    public Result<GetPostsResponseDto> getPostsByBookId(@RequestParam Long bookId){
        Book book = bookService.findBook(bookId);
        List<Post> posts = book.getPosts();//페치조인 필요

        List<GetPostsResponseDto> collect = posts.stream()
                .map(GetPostsResponseDto::new)
                .collect(Collectors.toList());

        return new Result(collect);
    }

    @PostMapping
    public ResponseEntity<Long> createPost(@RequestBody CreatePostRequestDto requestDto, UriComponentsBuilder b){
        Book requestBook = bookService.findBook(requestDto.getBookId());

        Post post = Post.builder()
                .book(requestBook)
                .type(requestDto.type)
                .scope(requestDto.scope)
                .isIncomplete(requestDto.isIncomplete)
                .imgLocation(requestDto.imgLocation)
                .title(requestDto.title)
                .content(requestDto.content)
                .build();

        Long postId = postService.createPost(post);

        UriComponents uriComponents =
                b.path("/posts/{postId}").buildAndExpand(postId);

        return ResponseEntity.created(uriComponents.toUri()).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletePost(@PathVariable Long id){
        postService.deletePost(id);

        return ResponseEntity.ok().build();
    }

//    @PatchMapping     QDL 활용해보기

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

        public GetPostsResponseDto(Post post){
            this.id = post.getId();
            this.type = post.getType();
            this.scope = post.getScope();
            this.isIncomplete = post.getIsIncomplete();
            this.imgLocation = post.getImgLocation();
            this.title = post.getTitle();
            this.content = post.getContent();
        }
    }

    @Data
    static class CreatePostRequestDto {
        private Long bookId;
        private PostType type;
        private PostScope scope;
        private Boolean isIncomplete;
        private String imgLocation;
        private String title;
        private String content;
    }


}
