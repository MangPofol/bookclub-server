package mangpo.server.repository;

import mangpo.server.entity.Liked;
import mangpo.server.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikedRepository extends JpaRepository<Liked,Long> {

    void deleteByPost(Post post);
}
