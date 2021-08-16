//package mangpo.server.controller;
//
//import lombok.RequiredArgsConstructor;
//import mangpo.server.entity.User;
//import mangpo.server.service.LoginService;
//import mangpo.server.session.SessionConst;
//import mangpo.server.session.SessionManager;
//import org.springframework.stereotype.Controller;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestParam;
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
//    private final SessionManager sessionManager;
//
//    @PostMapping("/login")
//    public String login(@RequestParam String email, @RequestParam String password, HttpServletRequest request) {
//
//        User loginMember = loginService.login(form.getLoginId(), form.getPassword());
//
//        if (loginMember == null) {
//            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
//            return "login/loginForm";
//        }
//
//        //로그인 성공 처리
//        //세션이 있으면 있는 세션 반환, 없으면 신규 세션을 생성
//        HttpSession session = request.getSession();
//        //세션에 로그인 회원 정보 보관
//        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);
//
//        return "redirect:" + redirectURL;
//
//    }
//}
