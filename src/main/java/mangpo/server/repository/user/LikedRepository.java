package mangpo.server.repository.user;

import mangpo.server.entity.user.Liked;
import mangpo.server.entity.post.Post;
import mangpo.server.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikedRepository extends JpaRepository<Liked,Long> {

    void deleteByPost(Post post);

    List<Liked> findAllByPost(Post post);

    void deleteAllByUser(User user);

    Integer countByPost(Post post);
}
