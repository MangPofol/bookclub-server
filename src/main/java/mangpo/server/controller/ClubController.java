package mangpo.server.controller;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import mangpo.server.dto.*;
import mangpo.server.dto.club.ClubInfoResponseDto;
import mangpo.server.dto.club.ClubResponseDto;
import mangpo.server.dto.club.CreateClubDto;
import mangpo.server.dto.club.UpdateClubDto;
import mangpo.server.entity.*;
import mangpo.server.service.*;
import mangpo.server.service.club.ClubService;
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
    private final ClubComplexViewService clubComplexViewService;

    @GetMapping("/{clubId}")
    public Result<ClubInfoResponseDto> getClubInfoByClubId(@PathVariable Long clubId) {
        ClubInfoResponseDto clubInfoResponseDto = clubComplexViewService.getClubInfoByClubId(clubId);
        return new Result<>(clubInfoResponseDto);
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

    @GetMapping("/{clubId}/users/{userId}/info")
    public Result<ClubUserCountInfoDto> getClubUserCountInfo(@PathVariable Long clubId, @PathVariable Long userId){
        ClubUserCountInfoDto res = clubComplexViewService.findClubUserCountInfo(clubId, userId);
        return new Result<>(res);
    }


}
