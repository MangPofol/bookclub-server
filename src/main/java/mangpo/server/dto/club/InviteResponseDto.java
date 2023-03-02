package mangpo.server.dto.club;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InviteResponseDto {
    private Long inviteId;
    private String message;

    private ClubResponseDto clubResponseDto;
    private ClubPresidentInfo clubPresidentInfo;
}
