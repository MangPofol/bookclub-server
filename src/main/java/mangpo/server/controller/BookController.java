package mangpo.server.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import mangpo.server.entity.Book;
import mangpo.server.entity.BookCategory;
import mangpo.server.service.BookService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    @GetMapping("/{bookCategory}")
    public Result getMyBooks(@PathVariable("bookCategory") BookCategory category){
        List<Book> books = bookService.findByBookCategory(category);

        List<MyBooksResponseDto> collect = books.stream()
                .map(m -> new MyBooksResponseDto(m.getId(),m.getName(),m.getIsbn(),m.getCategory()))
                .collect(Collectors.toList());

        return new Result(collect);
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class Result<T>{
        private T data;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class MyBooksResponseDto {
        private Long id;
        private String name;
        private String isbn;
        private BookCategory category;
    }
}
