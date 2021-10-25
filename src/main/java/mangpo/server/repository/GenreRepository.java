package mangpo.server.repository;

import mangpo.server.entity.Comment;
import mangpo.server.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre,Long> {
}
