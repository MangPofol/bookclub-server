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

    @Enumerated(EnumType.STRING)
    @Column(name = "club_color_set")
    private ColorSet colorSet;

    @Column(name = "club_level")
    private Integer level;

    @Column(name = "club_president")
    private Long presidentId;

    public void changeName(String name){ this.name = name; }
    public void changeColorSet(ColorSet colorSet){ this.colorSet = colorSet;}
    public void changeLevel(int level){ this.level = level;}
    public void changePresident(Long presidentId){ this.presidentId = presidentId;}


}
