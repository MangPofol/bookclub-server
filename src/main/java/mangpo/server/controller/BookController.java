package mangpo.server.controller;

import lombok.*;
import mangpo.server.entity.Book;
import mangpo.server.entity.BookCategory;
import mangpo.server.entity.ClubBookUser;
import mangpo.server.entity.User;
import mangpo.server.service.BookService;
import mangpo.server.service.ClubBookUserService;
import mangpo.server.service.UserService;
import mangpo.server.session.SessionConst;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final ClubBookUserService clubBookUserService;
    private final UserService userService;

    @GetMapping("/{email}/{category}")
    public Result getBooksByEmailAndCategory(@PathVariable String email ,@PathVariable BookCategory category){
        List<User> usersByEmail = userService.findUsersByEmail(email);
        List<ClubBookUser> listByUser = clubBookUserService.findListByUser(usersByEmail.get(0));

        List<BookResponseDto> books = listByUser.stream()
                .filter(m -> m.getBook().getCategory().equals(category))
                .map(m -> m.getBook())
                .map(m ->  new BookResponseDto(m.getId(),m.getName(),m.getIsbn(),m.getCategory()))
                .collect(Collectors.toList());

        //같은책인데 다른걸로 뜨는거 처리: 클라이언트에서 하나만 보이게 표시하도록 부탁
        return new Result(books);
    }

//    @SessionAttribute(name = SessionConst.LOGIN_USER, required = false) User loginUser,
    @PostMapping
    public ResponseEntity<CreateBookDto> createBook( @RequestBody CreateBookDto createBookDto){

        User loginUser = userService.findUser(1L);//mock

        Book newBook = Book.builder()
                .isbn(createBookDto.isbn)
                .name(createBookDto.name)
                .category(createBookDto.category)
                .build();

        Long bookId = bookService.createBook(newBook);

        ClubBookUser clubBookUser = ClubBookUser.builder()
                .book(newBook)
                .user(loginUser)
                .build();

        Long clubBookUserId = clubBookUserService.createClubBookUser(clubBookUser);

        return new ResponseEntity(createBookDto, HttpStatus.CREATED);
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
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class CreateBookDto {
        private String name;
        private String isbn;
        private BookCategory category;
    }
}
