package mangpo.server.integration;

import mangpo.server.dto.book.CreateBookDto;
import mangpo.server.entity.book.Book;
import mangpo.server.entity.book.BookCategory;
import mangpo.server.entity.book.BookInfo;
import mangpo.server.entity.cbu.ClubBookUser;
import mangpo.server.entity.post.Post;
import mangpo.server.entity.user.Authority;
import mangpo.server.entity.user.User;
import mangpo.server.repository.book.BookRepository;
import mangpo.server.repository.user.AuthorityRepository;
import mangpo.server.service.book.BookComplexService;
import mangpo.server.service.book.BookInfoService;
import mangpo.server.service.book.BookService;
import mangpo.server.service.cbu.ClubBookUserService;
import mangpo.server.service.post.PostService;
import mangpo.server.service.user.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;

import java.util.List;

import static org.assertj.core.api.Assertions.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
@SpringBootTest
public class BookTest {

    @Autowired
    private BookService bookService;
    @Autowired
    private BookComplexService bookComplexService;
    @Autowired
    private UserService userService;
    @Autowired
    private AuthorityRepository authorityRepository;
    @Autowired
    private BookInfoService bookInfoService;
    @Autowired
    private ClubBookUserService cbuService;
    @Autowired
    private PostService postService;

    @Autowired
    private EntityManager em;

    private static User user;
    private static Post post;
    private static ClubBookUser cbu;
    //    private static Book book;
    private static BookInfo bookInfo;
    private static BookCategory bookCategory;

    @BeforeAll
    void setUp() throws Exception {
        user = User.builder()
                .password("1234")
                .build();

        post = Post.builder()
                .build();

        cbu = ClubBookUser.builder()
                .build();

//        book = Book.builder()
//                .build();

        bookInfo = BookInfo.builder()
                .name("book name")
                .isbn("isbn")
                .build();

        bookCategory = BookCategory.NOW;

        authorityRepository.save(new Authority("ROLE_NEED_EMAIL"));
        userService.createUser(user);
    }

    @BeforeEach
    void init() {
//        authorityRepository.save(new Authority("ROLE_NEED_EMAIL"));
//        userService.createUser(user);
    }
    @DisplayName("Book 및 관련 엔티티 생성 성공")
    @WithMockUser(username = "1", roles = {"USER"})
    @Test
    void createBookAndRelated() {
        //given
        CreateBookDto dto = CreateBookDto.builder()
                .name(bookInfo.getName())
                .isbn(bookInfo.getIsbn())
                .category(bookCategory)
                .build();
        //when
        Long bookId = bookComplexService.createBookAndRelated(dto);

        //then
        Book findBook = bookService.findBookById(bookId);
        assertThat(findBook.getId()).isEqualTo(bookId);
        assertThat(findBook.getBookInfo()).isNotNull();
    }

    @DisplayName("Book 및 관련 엔티티 삭제 성공")
    @WithMockUser(username = "1", roles = {"USER"})
    @Test
    void deleteBookAndRelated() {
        //given
        CreateBookDto dto = CreateBookDto.builder()
                .name(bookInfo.getName())
                .isbn(bookInfo.getIsbn())
                .category(bookCategory)
                .build();

        Long bookId = bookComplexService.createBookAndRelated(dto);
        Book findBook = bookService.findBookById(bookId);

        //when
        bookComplexService.deleteBookAndRelated(bookId);

        //then
        assertThatThrownBy(() ->  bookService.findBookById(bookId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("존재하지 않는 책입니다.");

        List<ClubBookUser> cbus = cbuService.findListByBook(findBook);
        assertThat(cbus).isEmpty();
    }
}
