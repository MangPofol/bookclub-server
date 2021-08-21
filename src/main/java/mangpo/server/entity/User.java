package mangpo.server.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mangpo.server.entity.common.BaseTimeEntity;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_email")
    private String email;

    @Column(name = "user_pw")
    private String password;

    private String nickname;

    @Enumerated(EnumType.STRING)
    private Sex sex;

    private LocalDateTime birthdate;

    private String profileImgLocation;



    public void changeUserPassword(String password){
        this.password = password;
    }
    public void changeEmail(String email){
        this.email = email;
    }

}
