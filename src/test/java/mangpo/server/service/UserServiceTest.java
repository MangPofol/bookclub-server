package mangpo.server.service;

import mangpo.server.dto.UserRequestDto;
import mangpo.server.entity.User;
import mangpo.server.exeption.NotExistUserException;
import mangpo.server.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
public class UserServiceTest {

    @Autowired UserService userService;
    @Autowired UserRepository userRepository;
    @Autowired EntityManager em;


    @Test
     void 회원가입_정상() {
        //given
        User user = User.builder()
                .userPassword("1234")
                .email("email@gmail.com")
                .build();

        //when
        Long userId = userService.join(user);

        //then
        assertThat(user).isEqualTo(userRepository.findById(userId).get());
    }

    @Test()
    void 회원가입_중복_실패() {
        //given
        User user1 = User.builder()
                .userPassword("1234")
                .email("email@gmail.com")
                .build();

        User user2 = User.builder()
                .userPassword("4321")
                .email("email@gmail.com")
                .build();

        //when
        Long userId1 = userService.join(user1);

        //then
        assertThatThrownBy(() -> userService.join(user2))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("이미 사용중인 이메일입니다.");

    }


    @Test
    void 회원조회_단일_성공() {
        //given
        User user1 = User.builder()
                .userPassword("1234")
                .email("email@gmail.com")
                .build();

        Long userId = userService.join(user1);
        //when
        User findUser = userService.findUser(userId);

        //then
        assertThat(user1.getId()).isEqualTo(findUser.getId());
    }

    @Test
    void 회원조회_리스트() {
        //given
        User user1 = User.builder()
                .userPassword("1234")
                .email("email@gmail.com")
                .build();

        User user2 = User.builder()
                .userPassword("4321")
                .email("email@naver.com")
                .build();

        Long userId1 = userService.join(user1);
        Long userId2 = userService.join(user2);
        //when
        List<User> users = userService.findUsers();


        //then
        assertThat(users.size()).isEqualTo(2);
        assertThat(users).contains(user1,user2);
    }

    @Test
    void 회원_정보_수정() {
        //given
        User user1 = User.builder()
                .userPassword("1234")
                .email("email@gmail.com")
                .build();

        Long userId1 = userService.join(user1);

        em.flush();
        em.clear();

        User user = userService.findUser(userId1);

        UserRequestDto userRequestDto = UserRequestDto.builder()
                .userPassword("4321")
                .email("email@naver.com")
                .build();

        //when
        userService.updateUser(user.getId(), userRequestDto);

        em.flush();
        em.clear();

        User user2 = userService.findUser(userId1);

        //then
        assertThat(user2.getUserPassword()).isEqualTo("4321");
        assertThat(user2.getEmail()).isEqualTo("email@naver.com");
    }

    @Test
    void 회원_정보_삭제() {
        //given
        User user1 = User.builder()
                .userPassword("1234")
                .email("email@gmail.com")
                .build();

        Long userId1 = userService.join(user1);
        //when
        userService.deleteUser(userId1);

        em.flush();
        em.clear();

        //then
        assertThatThrownBy(() -> userService.findUser(userId1))
                .isInstanceOf(NotExistUserException.class)
                .hasMessageContaining("존재하지 않는 유저입니다.");
    }

}