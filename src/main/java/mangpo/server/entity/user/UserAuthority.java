package mangpo.server.entity.user;

import lombok.*;
import mangpo.server.entity.user.Authority;
import mangpo.server.entity.user.User;

import javax.persistence.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class UserAuthority {

    @Id
    @GeneratedValue
    @Column(name = "user_authority_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "authority_id")
    private Authority authority;

    public void changeAuthority(Authority authority){
        this.authority = authority;
    }
}