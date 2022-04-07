package mangpo.server.service.club;


import lombok.RequiredArgsConstructor;
import mangpo.server.dto.AddClubToUserBookRequestDto;
import mangpo.server.dto.AddUserToClubRequestDto;
import mangpo.server.dto.ClubUserCountInfoDto;
import mangpo.server.dto.club.ClubInfoResponseDto;
import mangpo.server.dto.club.CreateClubDto;
import mangpo.server.dto.club.UpdateClubDto;
import mangpo.server.entity.Club;
import mangpo.server.entity.ClubBookUser;
import mangpo.server.entity.book.Book;
import mangpo.server.entity.post.PostClubScope;
import mangpo.server.entity.user.User;
import mangpo.server.repository.ClubRepository;
import mangpo.server.service.ClubBookUserService;
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

    @Transactional
    public Long createClub(CreateClubDto createClubDto) {
        validateDuplicateClubName(createClubDto.getName());

        User user = userService.findUserFromToken();
        Club club = createClubDto.toEntity(user);
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
    public void addClubToUserBook(Long clubId, AddClubToUserBookRequestDto requestDto) {
        User user = userService.findUserFromToken();
        Book book = bookService.findBookById(requestDto.getBookId());
        Club club = findById(clubId);

        ClubBookUser build = ClubBookUser.builder()
                .user(user)
                .book(book)
                .club(club)
                .build();

        cbuService.createClubBookUser(build);
    }

    //클럽장이 클럽원 클럽에 추가하는 기능
    @Transactional
    public void addUserToClub(Long clubId, AddUserToClubRequestDto requestDto) {
        User user = userService.findUserFromToken();

        Club club = findById(clubId);
        Long presidentId = club.getPresidentId();

        if (!presidentId.equals(user.getId())) {
            throw new IllegalStateException("해당 유저는 클럽장이 아닙니다.");
        }

        User userByEmail = userService.findUserByEmail(requestDto.getEmail());

        ClubBookUser clubBookUser = ClubBookUser.builder()
                .club(club)
                .user(userByEmail)
                .build();

        cbuService.createClubBookUser(clubBookUser);
    }


    public List<Club> getClubsByUser(Long userId) {
        User user = userService.findById(userId);

        return cbuService.findDistinctByUserAndBookIsNull(user).stream()
                .map(m -> m.getClub())
                .collect(Collectors.toList());
    }


}
