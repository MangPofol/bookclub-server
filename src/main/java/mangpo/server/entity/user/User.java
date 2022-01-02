package mangpo.server.entity.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mangpo.server.dto.UpdateUserDto;
import mangpo.server.entity.common.BaseTimeEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue
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

    private String emailValidCode;


    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<UserAuthority> userAuthorityList = new ArrayList<>();

    public void update(UpdateUserDto updateUserDto) {
        this.email = updateUserDto.getEmail();
        this.sex = updateUserDto.getSex();
        this.birthdate = updateUserDto.getBirthdate();
        this.nickname = updateUserDto.getNickname();
        this.introduce = updateUserDto.getIntroduce();
        this.style = updateUserDto.getStyle();
        this.goal = updateUserDto.getGoal();
        this.profileImgLocation = updateUserDto.getProfileImgLocation();

        this.genres.clear();

        for (String s : updateUserDto.getGenres()) {
            Genre genre = Genre.builder()
                    .type(s)
                    .build();
            genre.addUser(this);
        }
    }

    public void changeIsDormant() {
        if (isDormant) {
            this.isDormant = Boolean.FALSE;
            return;
        }
        this.isDormant = Boolean.TRUE;
    }

    public void changeEmail(String email) {
        this.email = email;
    }

    //    public void changeAuthorities(Set<Authority> authorities){
//        this.authorities = authorities;
//    }
    public void changePw(String password) {
        this.password = password;
    }
    public void changeEmailValidCode(String emailValidCode) {
        this.emailValidCode = emailValidCode;
    }

}
