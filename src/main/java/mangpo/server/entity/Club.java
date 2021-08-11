package mangpo.server.entity;

import lombok.Getter;
import mangpo.server.entity.common.BaseTimeEntity;

import javax.persistence.*;

@Entity
@Getter
public class Club extends BaseTimeEntity {

    @Id @GeneratedValue
    private Long id;

    @Column(name = "club_name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "club_color_set")
    private ColorSet colorSet;

    @Column(name = "club_level")
    private int level;

    @Column(name = "club_president")
    private Long presidentId;

}
