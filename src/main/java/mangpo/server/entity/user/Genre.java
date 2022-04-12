package mangpo.server.entity.user;

import lombok.*;
import mangpo.server.entity.user.User;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Genre {

    @Id @GeneratedValue
    @Column(name = "genre_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String type;


    public void addUser(User user) {
        if(this.user != null)
            this.user.getGenres().remove(this);

        this.user = user;
        user.getGenres().add(this);
    }
}
