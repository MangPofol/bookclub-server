package mangpo.server.dto.club;

import lombok.Data;
import mangpo.server.entity.Club;

import java.time.LocalDateTime;

@Data
public class ClubResponseDto {
    private Long id;
    private String name;
    private Integer level;
    private Long presidentId;
    private String description;
    private LocalDateTime lastAddBookDate;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public ClubResponseDto(Club clubRequest) {
        this.id = clubRequest.getId();
        this.name = clubRequest.getName();
        this.level = clubRequest.getLevel();
        this.presidentId = clubRequest.getPresidentId();
        this.createdDate = clubRequest.getCreatedDate();
        this.lastAddBookDate = clubRequest.getLastAddBookDate();
        this.modifiedDate = clubRequest.getModifiedDate();
        this.description = clubRequest.getDescription();
    }
}