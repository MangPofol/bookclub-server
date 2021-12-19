package mangpo.server.repository;

import mangpo.server.entity.Liked;
import mangpo.server.entity.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikedRepository extends JpaRepository<Liked,Long> {

    void deleteByPost(Post post);
    List<Liked> findAllByPost(Post post);
}
