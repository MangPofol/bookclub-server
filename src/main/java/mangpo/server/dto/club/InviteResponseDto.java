package mangpo.server.dto.club;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mangpo.server.dto.user.UserResponseDto;
import mangpo.server.entity.club.Invite;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InviteResponseDto {
    private Long inviteId;
    private String message;

    private ClubResponseDto clubResponseDto;
    private ClubPresidentInfo clubPresidentInfo;

//
//    public InviteResponseDto(Invite invite){
//        this.inviteId = invite.getId();
//        this.message = invite.getMessage();
//
//        this.clubResponseDto = new ClubResponseDto(invite.getClub());
//    }
}
