package mangpo.server.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import mangpo.server.dto.CommentRequestDto;
import mangpo.server.dto.CommentResponseDto;
import mangpo.server.dto.Result;
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

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;
    private final PostService postService;


    @PostMapping
    public ResponseEntity<Result<CommentResponseDto>> createComment(@RequestBody CommentRequestDto commentRequestDto,
                                                                    UriComponentsBuilder b,
                                                                    @SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser){
        Post post = postService.findPost(commentRequestDto.getPostId());

        Comment comment = Comment.builder()
                .user(loginUser)
                .content(commentRequestDto.getContent())
                .parentCommentId(commentRequestDto.getParentCommentId())
                .build();
        comment.addComment(post);
        Long commentId = commentService.createComment(comment);

        UriComponents uriComponents =
                b.path("/comments/{commentId}").buildAndExpand(commentId);

        CommentResponseDto commentResponseDto = new CommentResponseDto(comment);
        Result<CommentResponseDto> result= new Result(commentResponseDto);

        return ResponseEntity.created(uriComponents.toUri()).body(result);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId){
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }


}
