//package mangpo.server.service;
//
//import lombok.RequiredArgsConstructor;
//import mangpo.server.entity.user.User;
//import mangpo.server.service.user.UserService;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//@Transactional(readOnly = true)
//@RequiredArgsConstructor
//public class LoginService {
//
//    private final UserService userService;
//
//    /**
//     * @return null 로그인 실패
//     */
//    public User login(String email, String password) {
//        User user = userService.findUserByEmail(email);
//        String userPassword = user.getPassword();
//        if (userPassword.equals(password))
//            return user;
//        return null;
//    }
//}