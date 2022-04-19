package mangpo.server.service.book;

import mangpo.server.entity.cbu.ClubBookUser;
import mangpo.server.entity.book.Book;
import mangpo.server.entity.book.BookCategory;
import mangpo.server.entity.book.BookInfo;
import mangpo.server.entity.user.User;
import mangpo.server.repository.book.BookRepository;
import mangpo.server.service.cbu.ClubBookUserService;
import mangpo.server.service.user.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@Transactional
@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;
    @Mock
    private ClubBookUserService cbuService;
    @Mock
    private UserService userService;

    private static User user1;
    private static User user2;

    private ClubBookUser cbu;

    @BeforeAll
    static void setUp() throws Exception {

        user1 = User.builder()
                .id(1L)
                .build();

        user2 = User.builder()
                .id(2L)
                .build();
    }

    // 유저 : 책 2개 다른 isbn
    @Test
    void createBookWithValidation_정상() {
        //given

        //등록할 책
        BookInfo bookInfo1 = BookInfo.builder()
                .id(1L)
                .isbn("123")
                .build();

        Book book1 = Book.builder()
                .id(1L)
                .bookCategory(BookCategory.BEFORE)
                .bookInfo(bookInfo1)
                .build();


        //유저가 등록해놓은 다른 책
        BookInfo bookInfo2 = BookInfo.builder()
                .id(2L)
                .isbn("1234")
                .build();

        Book book2 = Book.builder()
                .id(2L)
                .bookCategory(BookCategory.BEFORE)
                .bookInfo(bookInfo2)
                .build();


        ClubBookUser cbu1 = ClubBookUser.builder()
                .id(1L)
                .user(user1)
                .book(book2)
                .build();


        List<ClubBookUser> cbuList = new ArrayList<>();
        cbuList.add(cbu1);

        given(userService.findById(any())).willReturn(user1);
        given(bookRepository.save(any())).willReturn(book1);
        given(cbuService.findListByUserAndClubIsNullAndBookIsNotNull(any())).willReturn(cbuList);

        //when
        Long bookId = bookService.createBookWithValidation(book1, book1.getBookInfo().getIsbn(), user1.getId());

        //then
        assertThat(bookId).isEqualTo(book1.getId());
        then(bookRepository).should(times(1)).save(book1);
    }

    // 유저 : 책 2개 같은 isbn
    @Test
    void createBookWithValidation_중복() {
        //given

        //등록할 책
        BookInfo bookInfo1 = BookInfo.builder()
                .id(1L)
                .isbn("123")
                .build();

        Book book1 = Book.builder()
                .id(1L)
                .bookCategory(BookCategory.BEFORE)
                .bookInfo(bookInfo1)
                .build();


        //유저가 이미 등록해놓은 책
        Book book2 = Book.builder()
                .id(2L)
                .bookCategory(BookCategory.BEFORE)
                .bookInfo(bookInfo1)
                .build();

        ClubBookUser cbu1 = ClubBookUser.builder()
                .id(1L)
                .user(user1)
                .book(book2)
                .build();


        List<ClubBookUser> cbuList = new ArrayList<>();
        cbuList.add(cbu1);

        given(userService.findById(any())).willReturn(user1);
        given(cbuService.findListByUserAndClubIsNullAndBookIsNotNull(any())).willReturn(cbuList);


        //when then
        assertThatThrownBy(() -> bookService.createBookWithValidation(book1, book1.getBookInfo().getIsbn(), user1.getId()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("이미 유저가 등록한 책입니다.");

        then(bookRepository).should(never()).save(book1);
    }

    //통합으로
    // 유저 : 책 3개, 2 BEFORE 1 NOW
    @Test
    void findBooksByCurrentUserAndBookCategory() {
        //given
        Book book1 = Book.builder()
                .id(1L)
                .bookCategory(BookCategory.BEFORE)
                .build();

        Book book2 = Book.builder()
                .id(2L)
                .bookCategory(BookCategory.BEFORE)
                .build();

        Book book3 = Book.builder()
                .id(3L)
                .bookCategory(BookCategory.NOW)
                .build();

        ClubBookUser cbu1 = ClubBookUser.builder()
                .id(1L)
                .user(user1)
                .book(book1)
                .build();

        ClubBookUser cbu2 = ClubBookUser.builder()
                .id(2L)
                .user(user1)
                .book(book2)
                .build();

        ClubBookUser cbu3 = ClubBookUser.builder()
                .id(3L)
                .user(user1)
                .book(book3)
                .build();

        List<ClubBookUser> cbuList = new ArrayList<>();
        cbuList.add(cbu1);
        cbuList.add(cbu2);
        cbuList.add(cbu3);

        given(userService.findUserFromToken()).willReturn(user1);
        given(cbuService.findByUserAndClubIsNull(any())).willReturn(cbuList);

        //when
        Set<Book> books = bookService.findBooksByCurrentUserAndBookCategory(BookCategory.BEFORE);

        //then
        assertThat(books)
                .hasSize(2)
                .contains(book1,book2)
                .doesNotContain(book3);
    }
}