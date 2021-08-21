package mangpo.server.repository;

import mangpo.server.entity.Book;
import mangpo.server.entity.Club;
import mangpo.server.entity.ClubBookUser;
import mangpo.server.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClubBookUserRepository extends JpaRepository<ClubBookUser,Long>, ClubBookUserRepositoryCustom {

    @EntityGraph(attributePaths = ("book"))
    List<ClubBookUser> findListByUser(User user);
}
