package mangpo.server.service.club;


import lombok.RequiredArgsConstructor;
//import mangpo.server.dto.AddClubToUserBookRequestDto;
import mangpo.server.dto.club.CreateClubDto;
import mangpo.server.dto.club.UpdateClubDto;
import mangpo.server.entity.club.Club;
import mangpo.server.entity.cbu.ClubBookUser;
import mangpo.server.entity.club.Invite;
import mangpo.server.entity.book.Book;
import mangpo.server.entity.post.PostClubScope;
import mangpo.server.entity.user.User;
import mangpo.server.repository.club.ClubRepository;
import mangpo.server.service.cbu.ClubBookUserService;
import mangpo.server.service.book.BookService;
import mangpo.server.service.post.PostClubScopeService;
import mangpo.server.service.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ClubService {

    private final ClubRepository clubRepository;

    private final UserService userService;
    private final ClubBookUserService cbuService;
    private final PostClubScopeService pcsService;
    private final BookService bookService;
    private final InviteService inviteService;

    @Transactional
    public Long createClub(CreateClubDto createClubDto) {
        validateDuplicateClubName(createClubDto.getName());

        User user = userService.findUserFromToken();
        validateClubMaxExceed(user);

        Club club = Club.builder()
                .name(createClubDto.getName())
                .description(createClubDto.getDescription())
                .level(1)
                .presidentId(user.getId())
                .build();
        clubRepository.save(club);

        ClubBookUser cbu = ClubBookUser.builder()
                .club(club)
                .user(user)
                .build();
        cbuService.createClubBookUser(cbu);

        return club.getId();
    }

    private void validateDuplicateClubName(String clubName) {
        Optional<Club> findClub = clubRepository.findByName(clubName);
        if (findClub.isPresent()) {
            throw new IllegalStateException("이미 존재하는 클럽 이름입니다.");
        }
    }

    @Transactional
    public void updateClub(Long clubId, UpdateClubDto updateClubDto) {
        validateDuplicateClubName(updateClubDto.getName());

        Club club = findById(clubId);
        club.update(updateClubDto.toEntity());
        //공개범위 관련 수정
        updatePostClubScope(updateClubDto, club);
    }

    private void updatePostClubScope(UpdateClubDto updateClubDto, Club club) {
        List<PostClubScope> listByClub = pcsService.findListByClub(club);
        for (PostClubScope pcs : listByClub)
            pcs.changeClubName(updateClubDto.getName());
    }

    @Transactional
    public void deleteClubAndRelated(Long clubId) {
        Club club = findById(clubId);

        cbuService.deleteAllByClub(club);
        pcsService.deleteAllPcsByClub(club);

        deleteClub(clubId);
    }

    @Transactional
    public void deleteClub(Long clubId) {
        Club club = clubRepository.findById(clubId).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 클럽입니다."));
        clubRepository.delete(club);
    }

    public List<Club> findClubs() {
        return clubRepository.findAll();
    }

    public Club findById(Long clubId) {
        return clubRepository.findById(clubId).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 클럽입니다."));
    }

    public Club findByName(String clubName) {
        return clubRepository.findByName(clubName).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 클럽입니다."));
    }

    @Transactional
    public void addClubToUserBook(Long clubId, Long bookId) {
        User user = userService.findUserFromToken();
        Book book = bookService.findBookById(bookId);
        Club club = findById(clubId);

        ClubBookUser build = ClubBookUser.builder()
                .user(user)
                .book(book)
                .club(club)
                .build();

        cbuService.createClubBookUser(build);
    }

    public List<Club> getClubsByUser(Long userId) {
        User user = userService.findById(userId);

        return cbuService.findDistinctByUserAndBookIsNull(user).stream()
                .map(m -> m.getClub())
                .collect(Collectors.toList());
    }

    //클럽 가입 3개까지만 가능
    public void validateClubMaxExceed(User user){
        Integer cnt = cbuService.countByUserAndClubIsNotNullAndBookIsNull(user);

        if(cnt >= 3)
            throw new IllegalStateException("클럽 가입은 3개까지만 가능합니다");
    }


    //예비 클럽원이 수락하면 사용
    //클럽원 클럽에 추가하는 기능
    @Transactional
    public void addUserToClub(Long clubId) {
        User user = userService.findUserFromToken();
        validateClubMaxExceed(user);

        Club club = findById(clubId);
        ClubBookUser clubBookUser = ClubBookUser.builder()
                .club(club)
                .user(user)
                .build();

        cbuService.createClubBookUser(clubBookUser);

        Invite invite = inviteService.findByUserAndClub(user, club);
        inviteService.deleteById(invite.getId());
    }

    @Transactional
    public void deleteBookFromClub(Long clubId, Long bookId) {
        Club club = findById(clubId);
        Book book = bookService.findBookById(bookId);
        User user = userService.findUserFromToken();

        ClubBookUser cbu = cbuService.findByClubAndBookAndUser(club, book, user);
        cbuService.deleteCbu(cbu);
    }
}
