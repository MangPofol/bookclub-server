package mangpo.server.controller;

import lombok.*;
import mangpo.server.entity.*;
import mangpo.server.repository.BookQueryRepository;
import mangpo.server.service.BookService;
import mangpo.server.service.ClubBookUserService;
import mangpo.server.service.LikedService;
import mangpo.server.service.UserService;
import mangpo.server.session.SessionConst;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final ClubBookUserService cbuService;
    private final UserService userService;
    private final BookQueryRepository bookQueryRepository;
    private final LikedService likedService;

    @GetMapping//Todo fetchjoin, get query with projection
    public Result<List<BookResponseDto>> getBooksByEmailAndCategory(@RequestParam String email ,@RequestParam BookCategory category){
        User userByEmail = userService.findUserByEmail(email);
        List<ClubBookUser> listByUser = cbuService.findListByUser(userByEmail);


        //listByUser 에서 책만 뽑기
        List<Book> booksExtracted = listByUser.stream()
                .filter(m -> m.getBook().getCategory().equals(category))
                .map(m -> m.getBook())
                .collect(Collectors.toList());

        HashSet<Book> bookHashSet = new HashSet<>(booksExtracted);

        List<BookResponseDto> collect = bookHashSet.stream()
                .map(BookResponseDto::new)
                .collect(Collectors.toList());

        return new Result(collect);
    }


    @PostMapping
    public ResponseEntity<BookRequestDto> createBook(@SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser,
                                                     @RequestBody BookRequestDto bookRequestDto, UriComponentsBuilder b){
        Book newBook = Book.builder()
                .isbn(bookRequestDto.isbn)
                .name(bookRequestDto.name)
                .category(bookRequestDto.category)
                .build();

        Long bookId = bookService.createBook(newBook);

        ClubBookUser cbu = ClubBookUser.builder()
                .book(newBook)
                .user(loginUser)
                .build();

        cbuService.createClubBookUser(cbu);

        UriComponents uriComponents =
                b.path("/books/{bookId}").buildAndExpand(bookId);

//        return ResponseEntity.noContent().build().created(uriComponents.toUri()).build();

        return ResponseEntity.created(uriComponents.toUri()).body(bookRequestDto);
    }


    @PatchMapping("/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Long id, @RequestBody BookRequestDto bookRequestDto){
        Book bookRequest = bookRequestDto.toEntityExceptIdAndPosts(bookRequestDto);
        bookService.updateBook(id, bookRequest);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<?> deleteBook(@PathVariable Long bookId, @SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser){
        Book bookRequest = bookService.findBook(bookId);
        bookQueryRepository.deleteClubBookUserByUserAndBook(loginUser, bookRequest);
        bookService.deleteBook(bookId);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{bookId}/do-like")
    public ResponseEntity<?> doLike(@PathVariable Long bookId, @SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser){
        Book book = bookService.findBook(bookId);

        Liked liked = Liked.builder()
                .user(loginUser)
                .isLiked(true)
                .build();
        liked.doLikeToBook(book);
        likedService.createLiked(liked);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{bookId}/undo-like")
    public ResponseEntity<?> undoLike(@PathVariable Long bookId, @SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser){
        Book book = bookService.findBook(bookId);

        List<Liked> collect = book.getLikedList().stream()
                .filter(l -> l.getUser() == loginUser)
                .collect(Collectors.toList());
        Liked liked = collect.get(0);
        liked.undoLikeToBook(book);
        likedService.deleteLiked(liked);

        return ResponseEntity.noContent().build();
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
    static class BookResponseDto {
        private Long id;
        private String name;
        private String isbn;
        private BookCategory category;
        private LocalDateTime createdDate;
        private LocalDateTime modifiedDate;
//        private List<Liked> likedList;

        public BookResponseDto(Book book){
            this.id = book.getId();
            this.name = book.getName();
            this.isbn = book.getIsbn();
            this.category = book.getCategory();
            this.createdDate = book.getCreatedDate();
            this.modifiedDate = book.getModifiedDate();
        }
    }



    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class BookRequestDto {
        private String name;
        private String isbn;
        private BookCategory category;


        public Book toEntityExceptIdAndPosts(BookRequestDto bookRequestDto){
            return Book.builder()
                    .name(this.name)
                    .isbn(this.isbn)
                    .category(this.category)
                    .build();
        }
    }

}
