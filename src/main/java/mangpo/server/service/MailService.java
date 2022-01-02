package mangpo.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;
    private static final String FROM_ADDRESS = "ourpageapp@gmail.com";

    public void lostPw(String userEmail, String randomPw) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(userEmail);
        message.setFrom(MailService.FROM_ADDRESS);
        message.setSubject("[중요] OurPage에서 비밀번호 분실 관련 안내말씀 드립니다.");
        message.setText(
                "비밀번호 분실로 인해 요청하신 임시 비밀번호를 발송해 드립니다.\n" +
                        randomPw + "\n 임시 비밀번호를 이용해 로그인 하신 후 비밀번호를 즉시 변경해주세요."
        );

        mailSender.send(message);
    }

    public void validateEmail(String userEmail, String randomCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(userEmail);
        message.setFrom(MailService.FROM_ADDRESS);
        message.setSubject("[중요] OurPage에서 이메일 인증 관련 안내말씀 드립니다.");
        message.setText(
                "이메일 인증을 위한 코드를 발송해 드립니다.\n" +
                        randomCode + "\n 위 인증 코드를 앱으로 돌아가서 입력해 주세요."
        );

        mailSender.send(message);
    }
}