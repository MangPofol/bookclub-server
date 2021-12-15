package mangpo.server.controller;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import mangpo.server.dto.*;
import mangpo.server.dto.book.BookResponseDto;
import mangpo.server.dto.book.CreateBookDto;
import mangpo.server.dto.book.UpdateBookDto;
import mangpo.server.entity.*;
import mangpo.server.service.book.BookComplexService;
import mangpo.server.service.book.BookQueryService;
import mangpo.server.service.book.BookService;
import mangpo.server.session.SessionConst;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final BookComplexService bookComplexService;
    private final BookQueryService bookQueryService;


    @GetMapping//Todo fetchjoin
    public Result<List<BookResponseDto>> getBooksByEmailAndCategory(@RequestParam String email, @RequestParam BookCategory category) {
        List<Book> books = bookQueryService.findBooksByEmailAndBookCategory(email, category);

        List<BookResponseDto> collect = books.stream()
                .map(BookResponseDto::new)
                .collect(Collectors.toList());

        return new Result(collect);
    }

    @PostMapping
    public ResponseEntity<CreateBookDto> createBook(@RequestBody CreateBookDto createBookDto, UriComponentsBuilder b) {

        Long bookId = bookComplexService.createBookAndRelated(createBookDto);

        UriComponents uriComponents = b.path("/books/{bookId}").buildAndExpand(bookId);
        return ResponseEntity.created(uriComponents.toUri()).body(createBookDto);
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

//    @PostMapping("/{bookId}/do-like")
//    public ResponseEntity<?> doLikeBook(@PathVariable Long bookId, @SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser){
//        Book book = bookService.findBook(bookId);
//
//        Liked liked = Liked.builder()
//                .user(loginUser)
//                .isLiked(true)
//                .build();
//        liked.doLikeToBook(book);
//        likedService.createLiked(liked);
//
//        return ResponseEntity.noContent().build();
//    }
//
//    @PostMapping("/{bookId}/undo-like")
//    public ResponseEntity<?> undoLikeBook(@PathVariable Long bookId, @SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser){
//        Book book = bookService.findBook(bookId);
//
//        List<Liked> collect = book.getLikedList().stream()
//                .filter(l -> l.getUser().getId() == loginUser.getId())
//                .collect(Collectors.toList());
//
//        Liked liked = collect.get(0);
//        liked.undoLikeToBook(book);
//        likedService.deleteLiked(liked);
//
//        return ResponseEntity.noContent().build();
//    }


}
