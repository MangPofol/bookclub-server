package mangpo.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import mangpo.server.entity.Club;
import mangpo.server.entity.ColorSet;

import javax.persistence.*;

@Data
@Builder
public class ClubRequestDto {

    private String name;
    private ColorSet colorSet;
    private Integer level;
    private Long presidentId;

    public Club toEntity(){
        return Club.builder()
                .colorSet(this.colorSet)
                .level(this.level)
                .name(this.name)
                .presidentId(this.presidentId)
                .build();
    }
}
