package mangpo.server.dto.club;
import lombok.Data;
import mangpo.server.entity.Club;

@Data
public class UpdateClubDto {
    private String name;
    private Integer level;
    private Long presidentId;
    private String description;

    public Club toEntity() {
        return Club.builder()
                .name(this.name)
                .level(this.level)
                .presidentId(this.presidentId)
                .description(description)
                .build();
    }
}