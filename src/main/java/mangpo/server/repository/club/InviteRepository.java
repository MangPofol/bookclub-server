package mangpo.server.repository.club;

import mangpo.server.entity.club.Club;
import mangpo.server.entity.club.Invite;
import mangpo.server.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InviteRepository extends JpaRepository<Invite, Long> {
    List<Invite> findListByUser(User user);

    void deleteByUser(User user);

    Optional<Invite> findByUserAndClub(User user, Club club);
}
