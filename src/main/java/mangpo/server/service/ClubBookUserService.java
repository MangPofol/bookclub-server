package mangpo.server.service;

import lombok.RequiredArgsConstructor;
import mangpo.server.dto.ClubBookUserSearchCondition;
import mangpo.server.entity.Book;
import mangpo.server.entity.Club;
import mangpo.server.entity.ClubBookUser;
import mangpo.server.entity.User;
import mangpo.server.repository.ClubBookUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ClubBookUserService {

    private final ClubBookUserRepository cbuRepository;

    @Transactional
    public Long createClubBookUser(ClubBookUser clubBookUser){
        validateDuplicateCBU(clubBookUser);

        cbuRepository.save(clubBookUser);
        return clubBookUser.getId();
    }

    private void validateDuplicateCBU(ClubBookUser clubBookUser) {
        ClubBookUserSearchCondition cbuCond = new ClubBookUserSearchCondition(clubBookUser);
        Boolean isDuplicate = cbuRepository.isDuplicate(cbuCond);

        if (isDuplicate == Boolean.TRUE)
            throw new IllegalStateException("이미 존재하는 정보입니다");
    }

    @Transactional
    public void deleteAllClubBookUserByClub(Club club){
        Long delete = cbuRepository.deleteAllByClub(club);
    }

    public List<ClubBookUser> findListByUserExceptClub(User user){
        return cbuRepository.findListByUserExceptClub(user);
    }

    public ClubBookUser findByUserAndBookExceptClub(User user, Book book){
        return cbuRepository.findByUserAndBook(user,book);
    }

    public List<User> findUsersByClub(Club club){
        return cbuRepository.findUsersByClub(club);
    }

    public List<ClubBookUser> findClubBookUserByClub(Club club){
        return cbuRepository.findClubBookUserByClub(club);
    }


}
