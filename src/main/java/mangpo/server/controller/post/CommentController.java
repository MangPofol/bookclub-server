package mangpo.server.controller.post;

import lombok.RequiredArgsConstructor;
import mangpo.server.dto.Result;
import mangpo.server.dto.comment.CommentRequestDto;
import mangpo.server.dto.comment.CommentResponseDto;
import mangpo.server.service.post.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Result<CommentResponseDto>> createComment(@RequestBody CommentRequestDto commentRequestDto, UriComponentsBuilder b) {
        Long commentId = commentService.createComment(commentRequestDto);

        UriComponents uriComponents =
                b.path("/comments/{commentId}").buildAndExpand(commentId);

        CommentResponseDto commentResponseDto = new CommentResponseDto(commentService.findById(commentId));
        Result<CommentResponseDto> result = new Result<>(commentResponseDto);

        return ResponseEntity.created(uriComponents.toUri()).body(result);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}
