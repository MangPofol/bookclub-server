package mangpo.server.service.book;

import mangpo.server.dto.book.CreateBookDto;
import mangpo.server.entity.ClubBookUser;
import mangpo.server.entity.book.Book;
import mangpo.server.entity.book.BookCategory;
import mangpo.server.entity.book.BookInfo;
import mangpo.server.entity.post.Post;
import mangpo.server.entity.user.User;
import mangpo.server.service.ClubBookUserService;
import mangpo.server.service.post.PostService;
import mangpo.server.service.user.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@Transactional
@RunWith(MockitoJUnitRunner.class)
class BookComplexServiceTest {

    @InjectMocks
    private BookComplexService bookComplexService;

    @Mock
    private BookInfoService mockBookInfoService;
    @Mock
    private BookService mockBookService;
    @Mock
    private ClubBookUserService mockCbuService;
    @Mock
    private UserService mockUserService;
    @Mock
    private PostService mockPostService;

    private User user;
    private Post post;
    private ClubBookUser cbu;
    private Book book;
    private BookInfo bookInfo;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

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
        given(mockUserService.findUserFromToken()).willReturn(user);
        given(mockBookInfoService.createOrFindBookInfo(any())).willReturn(bookInfo);
        given(mockCbuService.createClubBookUser(any())).willReturn(cbu.getId());
        given(mockBookService.createBookWithValidation(any(),any(),any())).willReturn(book.getId());

        CreateBookDto createBookDto = new CreateBookDto("name", "isbn", BookCategory.AFTER);

        //when
        Long bookId = bookComplexService.createBookAndRelated(createBookDto);

        //then
        assertThat(bookId).isEqualTo(book.getId());

    }

    @Test
    void deleteBookAndRelated() {
        //given
        //when
        bookComplexService.deleteBookAndRelated(book.getId());

        //then
        Mockito.verify(mockBookService).findBookById(book.getId());
        Mockito.verify(mockCbuService).deleteAll(any());
        Mockito.verify(mockPostService).deleteAllWithCascade(any());
    }
}