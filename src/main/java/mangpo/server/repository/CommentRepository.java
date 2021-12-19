package mangpo.server.repository;

import mangpo.server.entity.post.Comment;
import mangpo.server.entity.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {

    List<Comment> findAllByPost(Post post);
}
