package mangpo.server.repository;

import mangpo.server.entity.Club;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClubRepository extends JpaRepository<Club,Long> {

    Optional<Club> findByName(String name);
}
