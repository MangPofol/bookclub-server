package mangpo.server.dto.club;

import lombok.Data;
import mangpo.server.entity.Club;
import mangpo.server.entity.ColorSet;
import mangpo.server.entity.User;

//todo Create,Update Request 따로 두지말고 그냥 하나로 합칠까 고민
@Data
public class CreateClubRequestDto {
    private String name;
    private ColorSet colorSet;
    private String description;

    public Club toEntity(User loginUser) {
        return Club.builder()
                .name(this.name)
                .colorSet(this.colorSet)
                .level(1)
                .presidentId(loginUser.getId())
                .description(description)
                .build();
    }
}