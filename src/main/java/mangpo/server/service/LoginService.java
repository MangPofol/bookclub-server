package mangpo.server.service;

import lombok.RequiredArgsConstructor;
import mangpo.server.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserService userService;

    /**
     * @return null 로그인 실패
     */
    public User login(String email, String password) {
        List<User> usersByEmail = userService.findUsersByEmail(email);
        User user = usersByEmail.get(0);
        String userPassword = usersByEmail.get(0).getUserPassword();
        if (userPassword.equals(password))
            return user;
        return null;
    }
}