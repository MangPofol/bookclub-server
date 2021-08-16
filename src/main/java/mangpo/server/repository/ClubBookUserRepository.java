package mangpo.server.repository;

import mangpo.server.entity.ClubBookUser;
import mangpo.server.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClubBookUserRepository extends JpaRepository<ClubBookUser,Long> {

    @EntityGraph(attributePaths = ("book"))
    List<ClubBookUser> findListByUser(User user);
}
