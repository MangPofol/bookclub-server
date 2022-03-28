package mangpo.server.entity;

import lombok.*;
import mangpo.server.entity.common.BaseTimeEntity;

import javax.persistence.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Club extends BaseTimeEntity {

    @Id @GeneratedValue
    private Long id;

    @Column(name = "club_name")
    private String name;

    @Column(name = "club_level")
    private Integer level;

    @Column(name = "club_president")
    private Long presidentId;

    @Column(name = "club_description")
    private String description;

    public void update(Club request) {
        if(request.getName() != null)
            this.name = request.name;
        if(request.getLevel() != null)
            this.level = request.level;
        if(request.getPresidentId() != null)
            this.presidentId = request.presidentId;
        if(request.getDescription() != null)
            this.description = request.description;
    }
}
