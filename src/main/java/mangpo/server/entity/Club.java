package mangpo.server.entity;

import lombok.*;
import mangpo.server.entity.common.BaseTimeEntity;
import mangpo.server.entity.user.ColorSet;

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

    @Column(name = "club_description")
    private String description;

    //todo: 이거 메소드 하나로 묶고 거기서 널체크
    public void changeName(String name){ this.name = name; }
    public void changeColorSet(ColorSet colorSet){ this.colorSet = colorSet;}
    public void changeLevel(int level){ this.level = level;}
    public void changePresident(Long presidentId){ this.presidentId = presidentId;}
    public void changeDescription(String description){this.description = description;}


}
