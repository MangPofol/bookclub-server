package mangpo.server.repository;

import mangpo.server.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    @EntityGraph(attributePaths = "userAuthorityList")
    Optional<User> findWithAuthByEmail(String email);

    Optional<User> findByEmail(String email);
}
