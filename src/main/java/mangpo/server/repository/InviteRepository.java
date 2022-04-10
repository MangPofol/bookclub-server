package mangpo.server.repository;

import mangpo.server.entity.Club;
import mangpo.server.entity.Invite;
import mangpo.server.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InviteRepository extends JpaRepository<Invite, Long> {
    List<Invite> findListByUser(User user);

    void deleteByUser(User user);

    Optional<Invite> findByUserAndClub(User user, Club club);
}
