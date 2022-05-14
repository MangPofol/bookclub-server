package mangpo.server.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mangpo.server.dto.club.ClubPresidentInfo;
import mangpo.server.dto.club.ClubResponseDto;
import mangpo.server.dto.club.InviteRequestDto;
import mangpo.server.dto.club.InviteResponseDto;
import mangpo.server.dto.Result;
import mangpo.server.entity.user.User;
import mangpo.server.service.InviteViewService;
import mangpo.server.service.club.InviteService;
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
@RequestMapping("/invites")
public class InviteController {

    private final InviteService inviteService;
    private final InviteViewService inviteViewService;

    @GetMapping
    public Result<List<InviteResponseDto>> getInviteList(){
        List<InviteResponseDto> res = inviteViewService.getInviteList();
        return new Result<>(res);
    }

    @PostMapping
    public ResponseEntity<?> createInvite(@RequestBody InviteRequestDto inviteRequestDto, UriComponentsBuilder builder) {
        Long inviteId = inviteService.createInvite(inviteRequestDto);

        UriComponents uriComponents =
                builder.path("/invites/{inviteId}").buildAndExpand(inviteId);

        return ResponseEntity.created(uriComponents.toUri()).body(inviteRequestDto);
    }

    @DeleteMapping("/{inviteId}")
    public ResponseEntity<?> deleteInvite( @PathVariable Long inviteId){
        inviteService.deleteById(inviteId);
        return ResponseEntity.noContent().build();
    }
}
