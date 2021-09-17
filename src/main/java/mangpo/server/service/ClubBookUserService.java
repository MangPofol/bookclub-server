package mangpo.server.service;

import lombok.RequiredArgsConstructor;
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
        cbuRepository.save(clubBookUser);
        return clubBookUser.getId();
    }

    @Transactional
    public void deleteAllClubBookUserByClub(Club club){
        Long delete = cbuRepository.deleteAllByClub(club);
    }

    public List<ClubBookUser> findListByUser(User user){
        List<ClubBookUser> listByUser = cbuRepository.findListByUser(user);
        return listByUser;
    }

    public ClubBookUser findByUserAndBookExceptClub(User user, Book book){
        ClubBookUser clubBookUser = cbuRepository.findByUserAndBook(user,book);
        return clubBookUser;
    }

    public List<User> findUsersByClub(Club club){
        return cbuRepository.findUsersByClub(club);
    }

    public List<ClubBookUser> findClubBookUserByClub(Club club){
        return cbuRepository.findClubBookUserByClub(club);
    }


}
