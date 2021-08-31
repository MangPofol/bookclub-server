package mangpo.server.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import mangpo.server.entity.Comment;
import mangpo.server.entity.Post;
import mangpo.server.entity.User;
import mangpo.server.service.CommentService;
import mangpo.server.service.PostService;
import mangpo.server.session.SessionConst;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;
    private final PostService postService;


    @PostMapping
    public ResponseEntity<Result<CommentDto>> createComment(@RequestBody CommentDto commentRequestDto,
                                                               UriComponentsBuilder b,
                                                               @SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser){
        Post post = postService.findPost(commentRequestDto.postId);

        Comment comment = Comment.builder()
                .user(loginUser)
                .content(commentRequestDto.getContent())
                .build();
        comment.addComment(post);
        Long commentId = commentService.createComment(comment);

        UriComponents uriComponents =
                b.path("/comments/{commentId}").buildAndExpand(commentId);

        CommentDto commentResponseDto = new CommentDto(comment);
        Result<CommentDto> result= new Result(commentResponseDto);

        return ResponseEntity.created(uriComponents.toUri()).body(result);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId){
        commentService.deleteComment(commentId);
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
    static class CommentDto {
        private Long postId;
        private String content;

        public CommentDto(Comment comment){
            this.postId = comment.getPost().getId();
            this.content = comment.getContent();
        }
    }
}
