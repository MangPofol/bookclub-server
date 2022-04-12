package mangpo.server.repository.club;

import mangpo.server.entity.club.Club;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClubRepository extends JpaRepository<Club,Long> {

    Optional<Club> findByName(String name);
}
