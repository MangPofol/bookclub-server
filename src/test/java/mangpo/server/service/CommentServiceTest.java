package mangpo.server.service;

import mangpo.server.entity.Book;
import mangpo.server.entity.BookCategory;
import mangpo.server.entity.Comment;
import mangpo.server.repository.CommentRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class CommentServiceTest {

    @Autowired CommentService commentService;
    @Autowired CommentRepository commentRepository;

    @Test
    void 댓글_생성() {
        //given
        Comment comment = Comment.builder().build();

        //when
        Long commentId = commentService.createComment(comment);

        //then
        assertThat(comment).isEqualTo(commentRepository.findById(commentId).get());
    }

    @Test
    void 댓글_삭제() {
        //given
        Comment comment = Comment.builder().build();
        Long commentId = commentService.createComment(comment);

        //when
        commentService.deleteComment(commentId);

        //then
        Optional<Comment> byId = commentRepository.findById(commentId);
        assertThat(byId).isEqualTo(Optional.empty());
    }
}