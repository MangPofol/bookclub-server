package mangpo.server.dto.club;

import lombok.Data;

//todo Create,Update Request 따로 두지말고 그냥 하나로 합칠까 고민
@Data
public class CreateClubDto {
    private String name;
    private String description;
}