package mangpo.server.dto.comment;


import lombok.Data;
import mangpo.server.entity.post.Comment;

import java.time.LocalDateTime;

@Data
public class CommentResponseDto {
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