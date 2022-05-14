package mangpo.server.service.cbu;

import lombok.RequiredArgsConstructor;
import mangpo.server.dto.ClubBookUserSearchCondition;
import mangpo.server.entity.club.Club;
import mangpo.server.entity.cbu.ClubBookUser;
import mangpo.server.entity.book.Book;
import mangpo.server.entity.user.User;
import mangpo.server.repository.cbu.ClubBookUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

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
    public void deleteAllByClub(Club club){
        Long delete = cbuRepository.deleteAllByClub(club);
    }

    public List<ClubBookUser> findListByUserAndClubIsNullAndBookIsNotNull(User user){
        return cbuRepository.findListByUserAndClubIsNullAndBookIsNotNull(user);
//        return cbuRepository.findListByUserExceptClub(user);
    }

//    public ClubBookUser findByUserAndBookExceptClub(User user, Book book){
//        return cbuRepository.findByUserAndBook(user,book);
//    }
    public List<ClubBookUser> findAllByCondition(ClubBookUserSearchCondition cbuSearchCond){
        return cbuRepository.findAllBySearchCondition(cbuSearchCond);
    }

    public List<User> findUsersByClub(Club club){
        return cbuRepository.findByClubAndUserIsNotNullAndBookIsNull(club)
                .stream()
                .map(ClubBookUser::getUser)
                .collect(Collectors.toList());
    }

//    public List<ClubBookUser> findClubBookUserByClub(Club club){
//        return cbuRepository.findByClubAndBookIsNul(club);
//    }

    @Transactional
    public void deleteAll(List<ClubBookUser> clubBookUsers){
        cbuRepository.deleteAll(clubBookUsers);
    }

    public List<ClubBookUser> findByUserAndClubIsNull(User user){
         return cbuRepository.findListByUserAndClubIsNull(user);
    }

    public Long countByUserAndClubIsNull(User user){
        return cbuRepository.countByUserAndClubIsNull(user);
    }

    public ClubBookUser findByUserAndBookAndClubIsNull(User user, Book book){
        return cbuRepository.findByUserAndBookAndClubIsNull(user,book);
    }

    public List<ClubBookUser> findDistinctByUserAndBookIsNull(User user){
        return cbuRepository.findDistinctByUserAndBookIsNull(user);
    }

    public List<ClubBookUser> findByUserAndClubAndBookIsNotNull(User user, Club club){
        return cbuRepository.findByUserAndClubAndBookIsNotNull(user,club);
    }

    public List<ClubBookUser> findListByClubAndUserIsNotNullAndBookIsNotNull(Club club) {
        return cbuRepository.findListByClubAndUserIsNotNullAndBookIsNotNull(club);
    }

    public Integer countByUserAndClubIsNotNullAndBookIsNull(User user){
        return cbuRepository.countByUserAndClubIsNotNullAndBookIsNull(user);
    }

    @Transactional
    public void deleteByClubAndBookAndUser(Club club, Book book, User user) {
        cbuRepository.deleteByClubAndBookAndUser(club,book,user);
    }

    public ClubBookUser findByClubAndBookAndUser(Club club, Book book, User user) {
        return cbuRepository.findByClubAndBookAndUser(club,book,user).orElseThrow(()-> new EntityNotFoundException("존재하지 않는 ClubBookUser 정보입니다."));
    }

    public void deleteCbu(ClubBookUser cbu){
        cbuRepository.delete(cbu);
    }

    public ClubBookUser findByUserAndClubAndBookIsNull(User user, Club club) {
        return cbuRepository.findByUserAndClubAndBookIsNull(user,club).orElseThrow(()-> new EntityNotFoundException("존재하지 않는 ClubBookUser 정보입니다."));
    }

    public List<ClubBookUser> findListByBook(Book book) {
        return cbuRepository.findListByBook(book);
    }

    public void deleteAllByUserAndClub(User user, Club club) {
        cbuRepository.deleteAllByUserAndClub(user,club);
    }
}
