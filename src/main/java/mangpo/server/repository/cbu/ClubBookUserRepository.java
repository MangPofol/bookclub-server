package mangpo.server.repository.cbu;

import mangpo.server.entity.club.Club;
import mangpo.server.entity.cbu.ClubBookUser;
import mangpo.server.entity.book.Book;
import mangpo.server.entity.user.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClubBookUserRepository extends JpaRepository<ClubBookUser,Long>, ClubBookUserRepositoryCustom {

//    @EntityGraph(attributePaths = ("book"))
//    List<ClubBookUser> findListByUserExceptClub(User user);

    Long deleteAllByClub(Club club);

    @EntityGraph(attributePaths = "book")
    List<ClubBookUser> findListByUserAndClubIsNull(User user);

    void deleteAllByUser(User user);

    Long countByUserAndClubIsNull(User user);

    ClubBookUser findByUserAndBookAndClubIsNull(User user, Book book);

    List<ClubBookUser> findDistinctByUserAndBookIsNull(User user);

    List<ClubBookUser> findByUserAndClubAndBookIsNotNull(User user, Club club);

    List<ClubBookUser> findListByClubAndUserIsNotNullAndBookIsNotNull(Club club);

    Integer countByUserAndClubIsNotNullAndBookIsNull(User user);

    void deleteByClubAndBookAndUser(Club club, Book book, User user);

    Optional<ClubBookUser> findByClubAndBookAndUser(Club club, Book book, User user);

    Optional<ClubBookUser> findByUserAndClubAndBookIsNull(User user, Club club);

    List<ClubBookUser> findListByBook(Book book);

    List<ClubBookUser> findListByUserAndClubIsNullAndBookIsNotNull(User user);

    List<User> findByClubAndUserIsNotNullAndBookIsNull(Club club);
}
