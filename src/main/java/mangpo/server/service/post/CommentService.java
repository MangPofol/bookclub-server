package mangpo.server.service.post;

import lombok.RequiredArgsConstructor;
import mangpo.server.dto.comment.CommentRequestDto;
import mangpo.server.entity.post.Comment;
import mangpo.server.entity.post.Post;
import mangpo.server.entity.user.User;
import mangpo.server.repository.post.CommentRepository;
import mangpo.server.service.post.PostService;
import mangpo.server.service.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        Post post = postService.findPostById(commentRequestDto.getPostId());

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

    public List<Comment> findAllByPost(Post post){
        return commentRepository.findAllByPost(post);
    }

    @Transactional
    public void deleteAll(List<Comment> comments){
        commentRepository.deleteAll(comments);
    }

    public Integer countByPost(Post post){
        return commentRepository.countByPost(post);
    }
}
