package mangpo.server.repository;

import mangpo.server.entity.post.Comment;
import mangpo.server.entity.post.Post;
import mangpo.server.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {

    List<Comment> findAllByPost(Post post);
    void deleteAllByUser(User user);

    Long countByPost(Post post);
}
