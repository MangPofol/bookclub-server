package mangpo.server.repository;

import mangpo.server.entity.Liked;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikedRepository extends JpaRepository<Liked,Long> {

}
