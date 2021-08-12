package mangpo.server.repository;

import mangpo.server.entity.Club;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClubRepository extends JpaRepository<Club,Long> {

    List<Club> findByName(String name);
}
