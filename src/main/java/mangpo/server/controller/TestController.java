package mangpo.server.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mangpo.server.service.common.FcmServcie;
import mangpo.server.repository.post.PostRepository;
import mangpo.server.service.book.BookService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {

    private final BookService bookService;
    private final PostRepository postRepository;
    private final FcmServcie fcmServcie;

    @GetMapping(value = "/fcmtest.do")
    public String fcmTest() throws Exception {
        String tokenId="e-5ZPqkSSKKb0H8vsyXhQJ:APA91bGdE9yHaInvoTUxW-C4XG6N3tfHQ-6HMhTjO0IcRbRR36vgkGVunlfhCCOPmn6zf4lyx1vpZTnNMuj83eRmZebEiWyd0kjKVCZt0aIP2ls7YhlSR8eKziSOHPxbettR_mAoQerx";

//        String tokenId="fI2o_mwWRaqcln-G5sb9x4:APA91bEYqfMXo4VTjqucvO7N_vGqG2OeIizwhCmDpHQhKk7H97tNwW_t3JK46JKco8jUyF2OAw4aLtJPaYeFIQyRsN6JC5YIZapx4M-E_mSwbNzJRpM3z9h-qC59GzNgyI2VJkk5Vn6E";
        String title="제목입니다";
        String content="내용입니다";

        fcmServcie.send_FCM(tokenId, title, content);

        return "test";
    }
}
