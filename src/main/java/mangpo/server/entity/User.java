package mangpo.server.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mangpo.server.dto.UserRequestDto;
import mangpo.server.entity.common.BaseTimeEntity;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Enumerated(EnumType.STRING)
    private Sex sex;

    private LocalDateTime birthdate;
    private String nickname;
    private String introduce;
    private String style;
    private String goal;
    private String profileImgLocation;

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Genre> genres = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ToDo> todos = new ArrayList<>();

    @Builder.Default
    private Boolean isDormant = Boolean.FALSE;//휴면회원

    public void update(UserRequestDto userRequest){
        this.email = userRequest.getEmail();
        this.password = userRequest.getPassword();
        this.sex = userRequest.getSex();
        this.birthdate = userRequest.getBirthdate();
        this.nickname = userRequest.getNickname();
        this.introduce = userRequest.getIntroduce();
        this.style = userRequest.getStyle();
        this.goal = userRequest.getGoal();
        this.profileImgLocation = userRequest.getProfileImgLocation();

        this.genres.clear();

        for (String s : userRequest.getGenres()) {
            Genre genre = Genre.builder()
                    .type(s)
                    .build();
            genre.addUser(this);
        }
    }

    public void changeIsDormant(){
        if (isDormant) {
            this.isDormant = Boolean.FALSE;
            return;
        }
        this.isDormant = Boolean.TRUE;
    }

    public void changeEmail(String email){
        this.email = email;
    }
}
