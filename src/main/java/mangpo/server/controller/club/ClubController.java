package mangpo.server.controller.club;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mangpo.server.dto.Result;
import mangpo.server.dto.club.*;
import mangpo.server.entity.club.Club;
import mangpo.server.service.club.ClubService;
import mangpo.server.service.club.ClubViewService;
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
    private final ClubViewService clubViewService;

    @GetMapping("/{clubId}")
    public Result<ClubInfoResponseDto> getClubInfoByClubId(@PathVariable Long clubId) {
        ClubInfoResponseDto clubInfoResponseDto = clubViewService.getClubInfoByClubId(clubId);
        return new Result<>(clubInfoResponseDto);
    }

    @GetMapping
    public Result<List<ClubResponseDto>> getClubsByUser(@RequestParam Long userId) {
        List<Club> clubsByUser = clubService.getClubsByUser(userId);

        List<ClubResponseDto> collect = clubsByUser.stream()
                .map(ClubResponseDto::new)
                .collect(Collectors.toList());

        return new Result<>(collect);
    }

    @PostMapping
    public ResponseEntity<ClubResponseDto> createClub(@RequestBody CreateClubDto createClubDto, UriComponentsBuilder builder) {
        Long clubId = clubService.createClub(createClubDto);

        UriComponents uriComponents =
                builder.path("/clubs/{clubId}").buildAndExpand(clubId);
        ClubResponseDto clubResponseDto = new ClubResponseDto(clubService.findById(clubId));

        return ResponseEntity.created(uriComponents.toUri()).body(clubResponseDto);
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
    public Result<ClubUserInfoDto> getClubUserInfo(@PathVariable Long clubId, @PathVariable Long userId) {
        ClubUserInfoDto res = clubViewService.findClubUserInfo(clubId, userId);
        return new Result<>(res);

    }

    //invite
//    자동화
    @PostMapping("/{clubId}/users")
    public ResponseEntity<?> addUserToClub(@PathVariable Long clubId) {
        clubService.addUserToClub(clubId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{clubId}/users/{userId}")
    public ResponseEntity<?> deleteUserFromClub(@PathVariable Long clubId, @PathVariable Long userId) {
        clubService.deleteUserFromClub(clubId, userId);
        return ResponseEntity.noContent().build();
    }

}
