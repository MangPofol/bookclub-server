package mangpo.server.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
public class Club {

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

    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
