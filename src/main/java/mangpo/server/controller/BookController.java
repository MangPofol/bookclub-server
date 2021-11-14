package mangpo.server.controller;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import mangpo.server.dto.BookResponseDto;
import mangpo.server.dto.ClubBookUserSearchCondition;
import mangpo.server.entity.*;
import mangpo.server.repository.BookQueryRepository;
import mangpo.server.service.*;
import mangpo.server.session.SessionConst;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {
//Todo 반환형 전부 Result로 감싸기: CommentController 참고
    private final BookService bookService;
    private final ClubBookUserService cbuService;
    private final UserService userService;
    private final BookQueryRepository bookQueryRepository;
    private final BookInfoService bookInfoService;
    private final PostService postService;

    @GetMapping//Todo fetchjoin
    public Result<List<BookResponseDto>> getBooksByEmailAndCategory(@RequestParam String email , @RequestParam BookCategory category){
        User userByEmail = userService.findUserByEmail(email);
        List<Book> byUserAndBook = bookQueryRepository.findByUserAndBook(userByEmail, category);

          List<BookResponseDto> collect = byUserAndBook.stream()
                .map(BookResponseDto::new)
                .collect(Collectors.toList());

        return new Result(collect);
    }


    @PostMapping
    public ResponseEntity<CreateBookDto> createBook(@SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser,
                                                    @RequestBody CreateBookDto createBookDto, UriComponentsBuilder b){
        BookInfo bookInfo = BookInfo.builder()
                .name(createBookDto.name)
                .isbn(createBookDto.isbn)
                .build();

        try {
            bookInfoService.createBookInfo(bookInfo);
        }catch (IllegalStateException e){
            log.info("bookInfoService = {}",e.toString());
            BookInfo byIsbn = bookInfoService.findByIsbn(bookInfo.getIsbn());
            bookInfo = byIsbn;
        }

        Book newBook = Book.builder()
                .category(createBookDto.category)
                .bookInfo(bookInfo)
                .build();

        validateDuplicateBook(bookInfo.getIsbn(),loginUser);
        Long bookId = bookService.createBook(newBook);

        ClubBookUser cbu = ClubBookUser.builder()
                .book(newBook)
                .user(loginUser)
                .build();
        cbuService.createClubBookUser(cbu);

        UriComponents uriComponents =
                b.path("/books/{bookId}").buildAndExpand(bookId);

//        return ResponseEntity.noContent().build().created(uriComponents.toUri()).build();

        return ResponseEntity.created(uriComponents.toUri()).body(createBookDto);
    }

    private void validateDuplicateBook(String isbn, User loginUser) {
        List<ClubBookUser> listByUser = cbuService.findListByUserExceptClub(loginUser);

        Optional<ClubBookUser> any = listByUser.stream()
                .filter(m->m.getUser().getId().equals(loginUser.getId()))
                .filter(m -> m.getBook().getBookInfo().getIsbn().equals(isbn))
                .findAny();

        if (any.isPresent())
            throw new IllegalStateException("이미 유저가 등록한 책입니다.");
    }


    @PatchMapping("/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Long id, @RequestBody UpdateBookDto updateBookDto){
        Book bookRequest = updateBookDto.toEntityExceptIdAndPosts(updateBookDto);
        bookService.updateBook(id, bookRequest);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<?> deleteBook(@PathVariable Long bookId, @SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser){
        Book bookRequest = bookService.findBook(bookId);

        ClubBookUserSearchCondition clubBookUserSearchCondition = new ClubBookUserSearchCondition();
        clubBookUserSearchCondition.setBook(bookRequest);
        List<ClubBookUser> allByCondition = cbuService.findAllByCondition(clubBookUserSearchCondition);
        cbuService.deleteAll(allByCondition);

        List<Post> postsByBookId = postService.findPostsByBookId(bookId);
        postService.deleteAllWithCascade(postsByBookId);

        bookQueryRepository.deleteByUserAndBook(loginUser, bookRequest);
        bookService.deleteBook(bookId);

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

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class Result<T>{
        private T data;
    }


//    @Data
//    @AllArgsConstructor
//    @NoArgsConstructor
//    static class LikedResponseDto {
//        private String userNickname;
//        private Boolean isLiked;
//    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class CreateBookDto {
        private String name;
        private String isbn;
        private BookCategory category;
//        public Book toEntityExceptIdAndPosts(CreateBookDto createBookDto){
//            return Book.builder()
//                    .name(this.name)
//                    .isbn(this.isbn)
//                    .category(this.category)
//                    .build();
//        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class UpdateBookDto {
        private BookCategory category;

        public Book toEntityExceptIdAndPosts(UpdateBookDto updateBookDto){
            return Book.builder()
                    .category(this.category)
                    .build();
        }
    }
}
