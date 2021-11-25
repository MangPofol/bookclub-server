package mangpo.server.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import mangpo.server.dto.*;
import mangpo.server.entity.*;
import mangpo.server.repository.ClubQueryRepository;
import mangpo.server.service.*;
import mangpo.server.service.book.BookService;
import mangpo.server.session.SessionConst;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/clubs")
public class ClubController {

    private final ClubService clubService;
    private final ClubBookUserService cbuService;
    private final BookService bookService;
    private final ClubQueryRepository clubQueryRepository;
    private final UserService userService;
    private final PostClubScopeService pscService;

    @GetMapping("{clubId}")
    public Result<ClubInfoResponseDto> getClubInfoByClubId(@PathVariable Long clubId) {
        Club club = clubService.findClub(clubId);
        List<User> usersInClub = cbuService.findUsersByClub(club);

        int memberSize = cbuService.findUsersByClub(club).size();
        List<ClubBookUser> cbuByClub = cbuService.findClubBookUserByClub(club);

        ClubInfoResponseDto clubInfoResponseDto = new ClubInfoResponseDto();

////        log.info("@@@@@메모 시작@@@@@@@@@@");
//        List<Post> hotMemo = clubQueryRepository.findTrendingPostByClub(club,memberSize);
////        log.info("@@@@@메모 끝@@@@@@@@@@");
//        List<Post> hotTopic = clubQueryRepository.findHotTopicByClub(club,memberSize);

        List<Post> trendingPostByClub = clubQueryRepository.findTrendingPostByClub(club, memberSize);


//        clubInfoResponseDto.setHotMemo(hotMemo);
//        clubInfoResponseDto.setTrendingPost(hotTopic);
        clubInfoResponseDto.setTrendingPost(trendingPostByClub);
        clubInfoResponseDto.setClubInfo(club);
        clubInfoResponseDto.setUsersInClubDtoList(usersInClub);
        clubInfoResponseDto.setBookAndUserDtoList(cbuByClub);

        return new Result<ClubInfoResponseDto>(clubInfoResponseDto);
    }

    @GetMapping
    public Result<List<ClubResponseDto>> getClubsByUser(@SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser) {
        List<Club> distinctClub = clubQueryRepository.findDistinctClub(loginUser);
        List<ClubResponseDto> collect = distinctClub.stream()
                .map(ClubResponseDto::new)
                .collect(Collectors.toList());

        return new Result(collect);
    }

    @PostMapping
    public ResponseEntity<ClubResponseDto> createClub(@SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser,
                                                      @RequestBody CreateClubRequestDto createClubRequestDto, UriComponentsBuilder builder) {

        Club club = createClubRequestDto.toEntity(loginUser);
        Long clubId = clubService.createClub(club);

        ClubBookUser cbu = ClubBookUser.builder()
                .club(club)
                .user(loginUser)
                .build();

        cbuService.createClubBookUser(cbu);

        UriComponents uriComponents =
                builder.path("/clubs/{clubId}").buildAndExpand(clubId);

        ClubResponseDto clubResponseDto = new ClubResponseDto(club);

        return ResponseEntity.created(uriComponents.toUri()).body(clubResponseDto);
    }

    //컨트롤 uri
    //클럽화면에서 클럽에 책 추가하는 기능
    //user,book만이 존재하는 ClubBookUser 엔티티의 정보를 이용해 새로운 ClubBookUser 엔티티를 만든다
    @PostMapping("/{clubId}/add-book")
    public ResponseEntity<?> addClubToUserBook(@SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser,
                                               @PathVariable Long clubId, @RequestBody addClubToUserBookRequestDto requestDto) {

        Book book = bookService.findBook(requestDto.bookId);
//        ClubBookUser byUserAndBook = cbuService.findByUserAndBookExceptClub(loginUser, book);//qdl 동적 쿼리 통한 리팩토링 고려
        ClubBookUserSearchCondition cbuSearchCond = new ClubBookUserSearchCondition();
        cbuSearchCond.setUser(loginUser);
        cbuSearchCond.setBook(book);

        ClubBookUser byUserAndBook = cbuService.findAllByCondition(cbuSearchCond).get(0);
        Club byId = clubService.findClub(clubId);

        ClubBookUser build = ClubBookUser.builder()
                .user(byUserAndBook.getUser())
                .book(byUserAndBook.getBook())
                .club(byId)
                .build();

        cbuService.createClubBookUser(build);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{clubId}/add-user")
    public ResponseEntity<?> addUserToClub(@SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser,
                                               @PathVariable Long clubId, @RequestBody addUserToClubRequestDto requestDto) {

        Club club = clubService.findClub(clubId);
        Long presidentId = club.getPresidentId();

        if (loginUser.getId() != presidentId){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        String email = requestDto.getEmail();
        User userByEmail = userService.findUserByEmail(email);

        ClubBookUser clubBookUser = ClubBookUser.builder()
                .club(club)
                .user(userByEmail)
                .build();
        cbuService.createClubBookUser(clubBookUser);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{clubId}")
    public ResponseEntity<?> updateClub(@PathVariable Long clubId, @RequestBody UpdateClubRequestDto updateClubRequestDto) {
        Club request = updateClubRequestDto.toEntity();
        clubService.updateClub(clubId, request);

        Club club = clubService.findClub(clubId);
        List<PostClubScope> listByClub = pscService.findListByClub(club);

        for (PostClubScope pcs : listByClub) {
            pcs.changeClubName(updateClubRequestDto.getName());
        }

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{clubId}")
    public ResponseEntity<?> deleteClub(@PathVariable Long clubId) {
        Club club = clubService.findClub(clubId);
        cbuService.deleteAllByClub(club);
        pscService.deleteAllPcsByClub(club);

        clubService.deleteClub(clubId);

        return ResponseEntity.noContent().build();
    }

    @Data
    static class addClubToUserBookRequestDto {
        private Long bookId;
    }

    @Data
    static class addUserToClubRequestDto{
        private String email;
    }

}
