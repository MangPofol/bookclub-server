package mangpo.server.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mangpo.server.entity.Book;
import mangpo.server.entity.BookInfo;
import mangpo.server.service.BookInfoService;
import mangpo.server.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/update-db")
class ModifyDBController {

    private final BookService bookService;
    private final BookInfoService bookInfoService;
    private final EntityManager em;

    @PostMapping
    public ResponseEntity<?> createBookInfo() {
        List<Book> books = bookService.findAllBooks();

        for (Book book : books) {
            BookInfo bookInfo = BookInfo.builder()
                    .name(book.getName())
                    .isbn(book.getIsbn())
                    .build();
            try{
                bookInfoService.createBookInfo(bookInfo);
                bookService.setBookInfo(book,bookInfo);
            }catch (IllegalStateException e){
                log.info("IE Exception");
                BookInfo byIsbn = bookInfoService.findByIsbn(book.getIsbn());
                bookService.setBookInfo(book,byIsbn);
            }
        }
        return ResponseEntity.noContent().build();
    }
}
