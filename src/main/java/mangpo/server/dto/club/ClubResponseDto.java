package mangpo.server.dto.club;

import lombok.Data;
import mangpo.server.entity.Club;
import mangpo.server.entity.user.ColorSet;

import java.time.LocalDateTime;

@Data
public class ClubResponseDto {
    private Long id;
    private String name;
    private ColorSet colorSet;
    private Integer level;
    private Long presidentId;
    private String description;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public ClubResponseDto(Club clubRequest) {
        this.id = clubRequest.getId();
        this.name = clubRequest.getName();
        this.colorSet = clubRequest.getColorSet();
        this.level = clubRequest.getLevel();
        this.presidentId = clubRequest.getPresidentId();
        this.createdDate = clubRequest.getCreatedDate();
        this.modifiedDate = clubRequest.getModifiedDate();
        this.description = clubRequest.getDescription();
    }
}