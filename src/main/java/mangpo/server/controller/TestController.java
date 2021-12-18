package mangpo.server.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mangpo.server.repository.PostRepository;
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

//    @GetMapping
//    public Long get() {
//        bookService.findBookById()
//        postRepository.countByBook()
//
////        Optional<Long> currentUserId = SecurityUtil.getCurrentUserId();
////        return currentUserId.get();
//    }
}
