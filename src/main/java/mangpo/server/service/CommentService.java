package mangpo.server.service;

import lombok.RequiredArgsConstructor;
import mangpo.server.dto.comment.CommentRequestDto;
import mangpo.server.entity.Comment;
import mangpo.server.entity.Post;
import mangpo.server.entity.User;
import mangpo.server.repository.CommentRepository;
import mangpo.server.service.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    private final UserService userService;
    private final PostService postService;

    @Transactional
    public Long createComment(CommentRequestDto commentRequestDto){
        User user = userService.findUserFromToken();
        Post post = postService.findPost(commentRequestDto.getPostId());

        Comment comment = Comment.builder()
                .user(user)
                .content(commentRequestDto.getContent())
                .parentCommentId(commentRequestDto.getParentCommentId())
                .build();
        comment.addComment(post);

        Comment save = commentRepository.save(comment);
        return save.getId();
    }

    public Comment findById(Long id){
        return commentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 comment입니다."));
    }


//    @Transactional
//    public Long createComment(Comment comment){
//        Comment save = commentRepository.save(comment);
//        return save.getId();
//    }
//

    @Transactional
    public void deleteComment(Long commentId){
        commentRepository.deleteById(commentId);
    }

}
