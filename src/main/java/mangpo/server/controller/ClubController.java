package mangpo.server.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import mangpo.server.dto.ClubInfoResponseDto;
import mangpo.server.entity.*;
import mangpo.server.repository.ClubQueryRepository;
import mangpo.server.service.BookService;
import mangpo.server.service.ClubBookUserService;
import mangpo.server.service.ClubService;
import mangpo.server.service.UserService;
import mangpo.server.session.SessionConst;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
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

    @GetMapping("{clubId}")
    public Result<ClubInfoResponseDto> getClubInfoByClubId(@PathVariable Long clubId) {
        Club club = clubService.findClub(clubId);
        List<User> usersInClub = cbuService.findUsersByClub(club);

        int memberSize = cbuService.findUsersByClub(club).size();
        List<ClubBookUser> cbuByClub = cbuService.findClubBookUserByClub(club);

        ClubInfoResponseDto clubInfo = new ClubInfoResponseDto();

//        log.info("@@@@@메모 시작@@@@@@@@@@");
        List<Post> hotMemo = clubQueryRepository.findHotMemoByClub(club,memberSize);
//        log.info("@@@@@메모 끝@@@@@@@@@@");
        List<Post> hotTopic = clubQueryRepository.findHotTopicByClub(club,memberSize);

        clubInfo.setHotMemo(hotMemo);
        clubInfo.setHotTopic(hotTopic);
        clubInfo.setClubInfo(club);
        clubInfo.setUsersInClubDtoList(usersInClub);
        clubInfo.setBookAndUserDtoList(cbuByClub);

        return new Result<ClubInfoResponseDto>(clubInfo);
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
        ClubBookUser byUserAndBook = cbuService.findByUserAndBookExceptClub(loginUser, book);//qdl 동적 쿼리 통한 리팩토링 고려
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

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{clubId}")
    public ResponseEntity<?> deleteClub(@PathVariable Long clubId) {
        Club club = clubService.findClub(clubId);
        cbuService.deleteAllClubBookUserByClub(club);

        clubService.deleteClub(clubId);

        return ResponseEntity.noContent().build();
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class Result<T> {
        private T data;
    }


    @Data
    static class CreateClubRequestDto {
        private String name;
        private ColorSet colorSet;
        private String description;

        public Club toEntity(User loginUser) {
            return Club.builder()
                    .name(this.name)
                    .colorSet(this.colorSet)
                    .level(1)
                    .presidentId(loginUser.getId())
                    .description(description)
                    .build();
        }
    }

    @Data
    static class UpdateClubRequestDto {
        private String name;
        private ColorSet colorSet;
        private Integer level;
        private Long presidentId;
        private String description;

        public Club toEntity() {
            return Club.builder()
                    .name(this.name)
                    .colorSet(this.colorSet)
                    .level(this.level)
                    .presidentId(this.presidentId)
                    .description(description)
                    .build();
        }
    }


    @Data
    static class addClubToUserBookRequestDto {
        private Long bookId;
    }

    @Data
    static class ClubResponseDto {
        private Long id;
        private String name;
        private ColorSet colorSet;
        private Integer level;
        private Long presidentId;
        private String description;
        private LocalDateTime createdDate;
        private LocalDateTime modifiedDate;

        public ClubResponseDto(Club clubRequest) {
            this.id = clubRequest.getId();
            this.name = clubRequest.getName();
            this.colorSet = clubRequest.getColorSet();
            this.level = clubRequest.getLevel();
            this.presidentId = clubRequest.getPresidentId();
            this.createdDate = clubRequest.getCreatedDate();
            this.modifiedDate = clubRequest.getModifiedDate();
            this.description = clubRequest.getDescription();
        }
    }

    @Data
    static class addUserToClubRequestDto{
        private String email;
    }

}
