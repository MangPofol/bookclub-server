package mangpo.server.dto;

import lombok.Data;

@Data
public class InviteRequestDto {
    private String userEmailToInvite;
    private Long clubId;
    private String message;
}
