package mangpo.server.dto.club;

import lombok.Data;
import mangpo.server.entity.Club;
import mangpo.server.entity.user.User;

//todo Create,Update Request 따로 두지말고 그냥 하나로 합칠까 고민
@Data
public class CreateClubDto {
    private String name;
    private String description;

    public Club toEntity(User loginUser) {
        return Club.builder()
                .name(this.name)
                .level(1)
                .presidentId(loginUser.getId())
                .description(description)
                .build();
    }
}