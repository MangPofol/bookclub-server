package mangpo.server.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mangpo.server.dto.auth.AuthorityDto;
import mangpo.server.entity.Genre;
import mangpo.server.entity.Sex;
import mangpo.server.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {
    private String email;
    private String password;
    private Sex sex;
    private LocalDateTime birthdate;
    private String nickname;
    private String introduce;
    private String style;
    private String goal;
    private String profileImgLocation;
    private List<String> genres;

    public User toEntityExceptId(){
        User user = User.builder()
                .email(this.email)
                .password(this.password)
                .sex(this.sex)
                .birthdate(this.birthdate)
                .nickname(this.nickname)
                .introduce(this.introduce)
                .style(this.style)
                .goal(this.goal)
                .profileImgLocation(this.profileImgLocation)
                .build();

        for (String s : this.getGenres()) {
            Genre genre = Genre.builder()
                    .type(s)
                    .build();
            genre.addUser(user);
        }

        return user;
    }
}
