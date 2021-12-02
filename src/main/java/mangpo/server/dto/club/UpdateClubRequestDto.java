package mangpo.server.dto.club;

import lombok.Data;
import mangpo.server.entity.Club;
import mangpo.server.entity.ColorSet;

@Data
public class UpdateClubRequestDto {
    private String name;
    private ColorSet colorSet;
    private Integer level;
    private Long presidentId;
    private String description;

    public Club toEntity() {
        return Club.builder()
                .name(this.name)
                .colorSet(this.colorSet)
                .level(this.level)
                .presidentId(this.presidentId)
                .description(description)
                .build();
    }
}