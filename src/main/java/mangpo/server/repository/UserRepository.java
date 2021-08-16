package mangpo.server.repository;

import mangpo.server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {

    List<User> findUsersByEmail(String email);
}
