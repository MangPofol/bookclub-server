package mangpo.server.repository.cbu;

import mangpo.server.dto.ClubBookUserSearchCondition;
import mangpo.server.entity.cbu.ClubBookUser;

import java.util.List;

public interface ClubBookUserRepositoryCustom {
//    List<ClubBookUser> findListByUserExceptClub(User user);
//
//    public ClubBookUser findByUserAndBook(User user, Book book);
//
//    public List<User> findUsersByClub(Club club);
//
//    public List<ClubBookUser> findClubBookUserByClub(Club club);

    public List<ClubBookUser> findAllBySearchCondition(ClubBookUserSearchCondition cbuSearchCondition);

    public Boolean isDuplicate(ClubBookUserSearchCondition cbuSearchCondition);
}
