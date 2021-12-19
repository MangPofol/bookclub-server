package mangpo.server.repository;

import mangpo.server.entity.Club;
import mangpo.server.entity.ClubBookUser;
import mangpo.server.entity.user.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClubBookUserRepository extends JpaRepository<ClubBookUser,Long>, ClubBookUserRepositoryCustom {

//    @EntityGraph(attributePaths = ("book"))
//    List<ClubBookUser> findListByUserExceptClub(User user);

    Long deleteAllByClub(Club club);

    @EntityGraph(attributePaths = "book")
    List<ClubBookUser> findByUserAndClubIsNull(User user);
}
