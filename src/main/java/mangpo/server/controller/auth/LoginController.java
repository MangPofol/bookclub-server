//package mangpo.server.controller.auth;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import lombok.RequiredArgsConstructor;
//import mangpo.server.entity.User;
//import mangpo.server.service.LoginService;
//import mangpo.server.session.SessionConst;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.SessionAttribute;
//import org.springframework.web.bind.support.SessionStatus;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;
//
//
//@Controller
//@RequiredArgsConstructor
//public class LoginController {
//
//    private final LoginService loginService;
//
//    @PostMapping("/login")
//    public ResponseEntity<String> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletRequest request) {
//
//        User loginUser = loginService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword());
//
//        if (loginUser == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//
//        //로그인 성공 처리
//        //세션이 있으면 있는 세션 반환, 없으면 신규 세션을 생성
//        HttpSession session = request.getSession();
//        //세션에 로그인 회원 정보 보관
//        session.setAttribute(SessionConst.LOGIN_USER, loginUser);
//
//        return ResponseEntity.ok("로그인 성공");
//    }
////
////    @PostMapping("/logout")
////    public String logout(HttpServletRequest request) {
////        HttpSession session = request.getSession(false);
////        if (session != null) {
////            session.invalidate();
////        }
////        return "redirect:/";
////    }
//
//    @PostMapping("/logout")
//    public ResponseEntity<String> logout(@SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser, SessionStatus sessionStatus) {
//
//        //세션에 유저 데이터가 없으면
//        if (loginUser == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//
//        sessionStatus.setComplete();
//        return ResponseEntity.ok("로그아웃 성공");
//    }
//
//    @Data
//    @AllArgsConstructor
//    @NoArgsConstructor
//    static class LoginRequestDto{
//        String email;
//        String password;
//    }
//}
