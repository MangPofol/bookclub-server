package mangpo.server.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mangpo.server.dto.Result;
import mangpo.server.dto.book.BookResponseDto;
import mangpo.server.entity.Book;
import mangpo.server.entity.BookCategory;
import mangpo.server.util.SecurityUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {


    @GetMapping//Todo fetchjoin
    public Long getUserId() {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        return currentUserId;

//        Optional<Long> currentUserId = SecurityUtil.getCurrentUserId();
//        return currentUserId.get();
    }
}
