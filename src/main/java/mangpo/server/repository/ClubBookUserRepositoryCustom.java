package mangpo.server.repository;

import mangpo.server.entity.Book;
import mangpo.server.entity.Club;
import mangpo.server.entity.ClubBookUser;
import mangpo.server.entity.User;

import java.util.List;

public interface ClubBookUserRepositoryCustom {
    public ClubBookUser findByUserAndBook(User user, Book book);

    public List<User> findUsersByClub(Club club);

    public List<ClubBookUser> findClubBookUserByClub(Club club);

    public Boolean isDuplicate(ClubBookUser clubBookUser);
}
