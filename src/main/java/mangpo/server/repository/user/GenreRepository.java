package mangpo.server.repository.user;

import mangpo.server.entity.user.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre,Long> {
}
