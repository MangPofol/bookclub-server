package mangpo.server.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import mangpo.server.entity.*;
import mangpo.server.repository.ClubQueryRepository;
import mangpo.server.service.BookService;
import mangpo.server.service.ClubBookUserService;
import mangpo.server.service.ClubService;
import mangpo.server.session.SessionConst;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
@RequestMapping("/clubs")
public class ClubController {

    private final ClubService clubService;
    private final ClubBookUserService cbuService;
    private final BookService bookService;
    private final ClubQueryRepository clubQueryRepository;

    @GetMapping("/{clubId}")
    public Result<List<ClubResponseDto>> getClubsByUser(@PathVariable Long clubId, @SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser){
        List<Club> distinctClub = clubQueryRepository.findDistinctClub(loginUser);
        List<ClubResponseDto> collect = distinctClub.stream()
                .map(ClubResponseDto::new)
                .collect(Collectors.toList());

        return new Result(collect);
    }

    @PostMapping
    public ResponseEntity<?> createClub(@SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser,
                                        @RequestBody CreateClubRequestDto createClubRequestDto, UriComponentsBuilder builder){

        Club club = createClubRequestDto.toEntity(loginUser);
        Long clubId = clubService.createClub(club);

        ClubBookUser cbu = ClubBookUser.builder()
                .club(club)
                .user(loginUser)
                .build();

        cbuService.createClubBookUser(cbu);

        UriComponents uriComponents =
                builder.path("/clubs/{clubId}").buildAndExpand(clubId);

        return ResponseEntity.created(uriComponents.toUri()).build();
    }

    //컨트롤 uri
    //클럽화면에서 클럽에 책 추가하는 기능
    //user,book만이 존재하는 ClubBookUser 엔티티의 정보를 이용해 새로운 ClubBookUser 엔티티를 만든다
    @PostMapping("/{clubId}/add-book")
    public ResponseEntity<?> addClubToUserBook(@SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser,
                                     @PathVariable Long clubId, @RequestBody addClubToUserBookRequestDto requestDto){

        Book book = bookService.findBook(requestDto.bookId);
        ClubBookUser byUserAndBook = cbuService.findByUserAndBookExceptClub(loginUser, book);
        Club byId = clubService.findClub(clubId);

        ClubBookUser build = ClubBookUser.builder()
                .user(byUserAndBook.getUser())
                .book(byUserAndBook.getBook())
                .club(byId)
                .build();

        cbuService.createClubBookUser(build);

        return ResponseEntity.ok().build();
    }





    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class Result<T>{
        private T data;
    }


    @Data
    static class CreateClubRequestDto {
        private String name;
        private ColorSet colorSet;

        public Club toEntity(User loginUser){
            return Club.builder()
                    .name(this.name)
                    .colorSet(this.colorSet)
                    .level(1)
                    .presidentId(loginUser.getId())
                    .build();
        }
    }

    @Data
    static class addClubToUserBookRequestDto{
        private Long bookId;
    }

    @Data
    static class ClubResponseDto{
        private Long id;
        private String name;
        private ColorSet colorSet;
        private Integer level;
        private Long presidentId;
        private LocalDateTime createdDate;
        private LocalDateTime modifiedDate;

        public ClubResponseDto(Club clubRequest){
            this.id = clubRequest.getId();
            this.name = clubRequest.getName();
            this.colorSet = clubRequest.getColorSet();
            this.level = clubRequest.getLevel();
            this.presidentId = clubRequest.getPresidentId();
            this.createdDate = clubRequest.getCreatedDate();
            this.modifiedDate = clubRequest.getModifiedDate();
        }
    }

}
