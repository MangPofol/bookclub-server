package mangpo.server.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mangpo.server.entity.user.Sex;
import mangpo.server.entity.user.User;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsersInClubDto {

    private Long id;
    private String email;
    private String password;
    private String nickname;
    private Sex sex;
    private LocalDateTime birthdate;
    private String profileImgLocation;

    public UsersInClubDto(User user){
        this.id = user.getId();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.nickname = user.getNickname();
        this.sex = user.getSex();
        this.birthdate = user.getBirthdate();
        this.profileImgLocation = user.getProfileImgLocation();
    }


}
