package mangpo.server.controller;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import mangpo.server.dto.*;
import mangpo.server.dto.book.BookResponseDto;
import mangpo.server.dto.book.CreateBookDto;
import mangpo.server.dto.book.UpdateBookDto;
import mangpo.server.entity.book.Book;
import mangpo.server.entity.book.BookCategory;
import mangpo.server.service.book.BookComplexService;
import mangpo.server.service.book.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final BookComplexService bookComplexService;


//    @GetMapping//Todo fetchjoin
//    public Result<List<BookResponseDto>> getBooksByEmailAndCategory(@RequestParam String email, @RequestParam BookCategory category) {
//        List<Book> books = bookQueryService.findBooksByEmailAndBookCategory(email, category);
//
//        List<BookResponseDto> collect = books.stream()
//                .map(BookResponseDto::new)
//                .collect(Collectors.toList());
//
//        return new Result(collect);
//    }

    @GetMapping
    public Result<List<BookResponseDto>> getBooksByCurrentUserAndBookCategory(@RequestParam BookCategory category) {
        Set<Book> books = bookService.findBooksByCurrentUserAndBookCategory(category);

        List<BookResponseDto> collect = books.stream()
                .map(BookResponseDto::new)
                .collect(Collectors.toList());

        return new Result<>(collect);
    }

    @PostMapping
    public ResponseEntity<BookResponseDto> createBook(@RequestBody CreateBookDto createBookDto, UriComponentsBuilder b) {
        Long bookId = bookComplexService.createBookAndRelated(createBookDto);

        UriComponents uriComponents = b.path("/books/{bookId}").buildAndExpand(bookId);

        BookResponseDto bookResponseDto = new BookResponseDto(bookService.findBookById(bookId));
        return ResponseEntity.created(uriComponents.toUri()).body(bookResponseDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Long id, @RequestBody UpdateBookDto updateBookDto) {
        Book bookRequest = updateBookDto.toEntityExceptIdAndPosts(updateBookDto);
        bookService.updateBook(id, bookRequest);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<?> deleteBook(@PathVariable Long bookId) {
        bookComplexService.deleteBookAndRelated(bookId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/total-book")
    public Result<?> getTotalBook() {
        Long totalBook = bookService.findTotalBook();
        return new Result<>(totalBook);
    }
}
