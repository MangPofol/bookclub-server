package mangpo.server.service;

import lombok.RequiredArgsConstructor;
import mangpo.server.dto.club.ClubPresidentInfo;
import mangpo.server.dto.club.ClubResponseDto;
import mangpo.server.dto.club.InviteResponseDto;
import mangpo.server.entity.user.User;
import mangpo.server.service.club.InviteService;
import mangpo.server.service.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InviteViewService {

    private final InviteService inviteService;
    private final UserService userService;

    public List<InviteResponseDto> getInviteList() {
        return inviteService.findListByUser().stream()
                .map(invite -> {

                    User president = userService.findById(invite.getClub().getPresidentId());

                    ClubPresidentInfo clubPresidentInfo = new ClubPresidentInfo();
                    clubPresidentInfo.setPresidentNickname(president.getNickname());
                    clubPresidentInfo.setProfileImgLocation(president.getProfileImgLocation());

                    return InviteResponseDto.builder()
                            .inviteId(invite.getId())
                            .message(invite.getMessage())
                            .clubResponseDto(new ClubResponseDto(invite.getClub()))
                            .clubPresidentInfo(clubPresidentInfo)
                            .build();
                })
                .collect(Collectors.toList());
    }
}
