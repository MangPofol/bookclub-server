package mangpo.server.dto.club;

import lombok.Data;

@Data
public class InviteRequestDto {
    private String userEmailToInvite;
    private Long clubId;
    private String message;
}
