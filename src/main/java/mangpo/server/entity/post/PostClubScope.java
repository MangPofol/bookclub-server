package mangpo.server.entity.post;


import lombok.*;
import mangpo.server.entity.club.Club;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostClubScope {

    @Id @GeneratedValue
    @Column(name = "post_club_scope_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    private String clubName;


    public void changeClubName(String newClubName){
        this.clubName = newClubName;
    }
}
