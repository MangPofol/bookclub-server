package mangpo.server.service.book;

import mangpo.server.dto.book.CreateBookDto;
import mangpo.server.entity.cbu.ClubBookUser;
import mangpo.server.entity.book.Book;
import mangpo.server.entity.book.BookCategory;
import mangpo.server.entity.book.BookInfo;
import mangpo.server.entity.post.Post;
import mangpo.server.entity.user.User;
import mangpo.server.service.cbu.ClubBookUserService;
import mangpo.server.service.post.PostService;
import mangpo.server.service.user.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@Transactional
@ExtendWith(MockitoExtension.class)
class BookComplexServiceTest {

    @InjectMocks
    private BookComplexService bookComplexService;

    @Mock
    private BookInfoService bookInfoService;
    @Mock
    private BookService bookService;
    @Mock
    private ClubBookUserService cbuService;
    @Mock
    private UserService userService;
    @Mock
    private PostService postService;

    private static User user;
    private static Post post;
    private static ClubBookUser cbu;
    private static Book book;
    private static BookInfo bookInfo;

    @BeforeAll
    static void setUp() throws Exception {
        user = User.builder()
                .id(1L)
                .build();

        post = Post.builder()
                .id(1L)
                .build();

        cbu = ClubBookUser.builder()
                .id(1L)
                .build();

        book = Book.builder()
                .id(1L)
                .build();

        bookInfo = BookInfo.builder()
                .id(1L)
                .build();
    }

    @Test
    void createBookAndRelated() {
        //given
        given(userService.findUserFromToken()).willReturn(user);
        given(bookInfoService.createOrFindBookInfo(any())).willReturn(bookInfo);
        given(cbuService.createClubBookUser(any())).willReturn(cbu.getId());
        given(bookService.createBookWithValidation(any(),any(),any())).willReturn(book.getId());

        CreateBookDto createBookDto = new CreateBookDto("name", "isbn", BookCategory.AFTER);

        //when
        Long bookId = bookComplexService.createBookAndRelated(createBookDto);

        //then
        assertThat(bookId).isEqualTo(book.getId());
        then(bookService).should(times(1)).createBookWithValidation(any(),any(),any());
    }

    @Test
    void deleteBookAndRelated() {
        //given
        given(bookService.findBookById(any())).willReturn(book);

        //when
        bookComplexService.deleteBookAndRelated(book.getId());

        //then
        then(bookService).should(times(1)).findBookById(any());
        then(cbuService).should(times(1)).deleteAll(any());
        then(postService).should(times(1)).deleteAllWithCascade(any());
    }
}