package mangpo.server.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import mangpo.server.dto.*;
import mangpo.server.dto.club.ClubInfoResponseDto;
import mangpo.server.dto.club.ClubResponseDto;
import mangpo.server.dto.club.CreateClubDto;
import mangpo.server.dto.club.UpdateClubDto;
import mangpo.server.entity.*;
import mangpo.server.entity.book.Book;
import mangpo.server.entity.post.Post;
import mangpo.server.entity.user.User;
import mangpo.server.repository.ClubQueryRepository;
import mangpo.server.service.*;
import mangpo.server.service.book.BookService;
import mangpo.server.service.club.ClubService;
import mangpo.server.service.post.PostClubScopeService;
import mangpo.server.service.user.UserService;
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
    private final PostClubScopeService pcsService;

    @GetMapping("/{clubId}")
    public Result<ClubInfoResponseDto> getClubInfoByClubId(@PathVariable Long clubId) {



        Club club = clubService.findById(clubId);
        List<User> usersInClub = cbuService.findUsersByClub(club);

        List<ClubBookUser> cbuByClub = cbuService.findClubBookUserByClub(club);

        ClubInfoResponseDto clubInfoResponseDto = new ClubInfoResponseDto();

        List<Post> trendingPostByClub = clubQueryRepository.findTrendingPostByClub(club, usersInClub.size());

        clubInfoResponseDto.setTrendingPost(trendingPostByClub);
        clubInfoResponseDto.setClubInfo(club);
        clubInfoResponseDto.setUsersInClubDtoList(usersInClub);
        clubInfoResponseDto.setBookAndUserDtoList(cbuByClub);

        return new Result<ClubInfoResponseDto>(clubInfoResponseDto);
    }

    @GetMapping
    public Result<List<ClubResponseDto>> getClubsByUser(@RequestParam Long userId) {
        List<Club> clubsByUser = clubService.getClubsByUser(userId);

        List<ClubResponseDto> collect = clubsByUser.stream()
                .map(ClubResponseDto::new)
                .collect(Collectors.toList());

        return new Result<List<ClubResponseDto>>(collect);
    }

    @PostMapping
    public ResponseEntity<ClubResponseDto> createClub(@RequestBody CreateClubDto createClubDto, UriComponentsBuilder builder) {
        Long clubId = clubService.createClub(createClubDto);

        UriComponents uriComponents =
                builder.path("/clubs/{clubId}").buildAndExpand(clubId);
        ClubResponseDto clubResponseDto = new ClubResponseDto(clubService.findById(clubId));

        return ResponseEntity.created(uriComponents.toUri()).body(clubResponseDto);
    }

    //컨트롤 uri
    //클럽화면에서 클럽에 책 추가하는 기능
    //user,book만이 존재하는 ClubBookUser 엔티티의 정보를 이용해 새로운 ClubBookUser 엔티티를 만든다
    @PostMapping("/{clubId}/add-book")
    public ResponseEntity<?> addClubToUserBook(@PathVariable Long clubId, @RequestBody AddClubToUserBookRequestDto requestDto) {
        clubService.addClubToUserBook(clubId, requestDto);

//        User user = userService.findUserFromToken();
//
//        Book book = bookService.findBookById(requestDto.bookId);
////        ClubBookUser byUserAndBook = cbuService.findByUserAndBookExceptClub(loginUser, book);//qdl 동적 쿼리 통한 리팩토링 고려
//        ClubBookUserSearchCondition cbuSearchCond = new ClubBookUserSearchCondition();
//        cbuSearchCond.setUser(user);
//        cbuSearchCond.setBook(book);
//
//        ClubBookUser byUserAndBook = cbuService.findAllByCondition(cbuSearchCond).get(0);
//        Club byId = clubService.findById(clubId);
//
//        ClubBookUser build = ClubBookUser.builder()
//                .user(byUserAndBook.getUser())
//                .book(byUserAndBook.getBook())
//                .club(byId)
//                .build();
//
//        cbuService.createClubBookUser(build);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{clubId}/add-user")
    public ResponseEntity<?> addUserToClub(@PathVariable Long clubId, @RequestBody AddUserToClubRequestDto requestDto) {
        clubService.addUserToClub(clubId, requestDto);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{clubId}")
    public ResponseEntity<?> updateClub(@PathVariable Long clubId, @RequestBody UpdateClubDto updateClubDto) {
        clubService.updateClub(clubId, updateClubDto);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{clubId}")
    public ResponseEntity<?> deleteClub(@PathVariable Long clubId) {
        clubService.deleteClubAndRelated(clubId);

        return ResponseEntity.noContent().build();
    }


}
