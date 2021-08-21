package mangpo.server.service;

import lombok.RequiredArgsConstructor;
import mangpo.server.entity.Book;
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

    private final ClubBookUserRepository clubBookUserRepository;

    @Transactional
    public Long createClubBookUser(ClubBookUser clubBookUser){
        clubBookUserRepository.save(clubBookUser);
        return clubBookUser.getId();
    }

    public List<ClubBookUser> findListByUser(User user){
        List<ClubBookUser> listByUser = clubBookUserRepository.findListByUser(user);
        return listByUser;
    }

    public ClubBookUser findByUserAndBookExceptClub(User user, Book book){
        ClubBookUser clubBookUser = clubBookUserRepository.findByUserAndBook(user,book);
        return clubBookUser;
    }




}
