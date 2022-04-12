package mangpo.server.dto.club;

import lombok.Data;
import mangpo.server.entity.club.Invite;

@Data
public class InviteResponseDto {
    private Long inviteId;
    private String userEmailToInvite;
    private Long clubId;
    private String message;

    public InviteResponseDto(Invite invite){
        this.inviteId = invite.getId();
        this.userEmailToInvite = invite.getUser().getEmail();
        this.clubId = invite.getClub().getId();
        this.message = invite.getMessage();
    }
}
