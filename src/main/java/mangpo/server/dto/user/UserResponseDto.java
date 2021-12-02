package mangpo.server.dto.user;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mangpo.server.entity.Genre;
import mangpo.server.entity.Sex;
import mangpo.server.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    private Long userId;
    private String email;
    private Sex sex;
    private LocalDateTime birthdate;
    private String nickname;
    private String introduce;
    private String style;
    private String goal;
    private String profileImgLocation;
    private List<String> genres;
    private Boolean isDormant;

    public UserResponseDto(User user) {
        this.userId = user.getId();
        this.email = user.getEmail();
        this.sex = user.getSex();
        this.birthdate = user.getBirthdate();
        this.nickname = user.getNickname();
        this.introduce = user.getIntroduce();
        this.style = user.getIntroduce();
        this.goal = user.getGoal();
        this.profileImgLocation = user.getProfileImgLocation();
        this.isDormant = user.getIsDormant();

        this.genres = user.getGenres().stream()
                .map(Genre::getType)
                .collect(Collectors.toList());
    }
}
