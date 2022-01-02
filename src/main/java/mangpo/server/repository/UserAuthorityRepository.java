package mangpo.server.repository;

import mangpo.server.entity.user.User;
import mangpo.server.entity.user.UserAuthority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAuthorityRepository extends JpaRepository<UserAuthority, Long> {
    UserAuthority findByUser(User user);
}
