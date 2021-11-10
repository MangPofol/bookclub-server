package mangpo.server.service;

import mangpo.server.entity.Book;
import mangpo.server.entity.BookCategory;
import mangpo.server.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class BookServiceTest {

    @Autowired BookService bookService;
    @Autowired BookRepository bookRepository;
    @Autowired EntityManager em;

    @Test
    void 책_생성_정상() {
        //given
        Book book = Book.builder()
                .name("죽은 시인의 사회")
                .category(BookCategory.NOW)
                .isbn("1234")
                .build();


        //when
        Long bookId = bookService.createBook(book);


        //then
        assertThat(book).isEqualTo(bookRepository.findById(bookId).get());
    }

    @Test
    void 책_조회_단일_성공() {
        //given
        Book book1 = Book.builder()
                .name("죽은 시인의 사회")
                .category(BookCategory.NOW)
                .isbn("1234")
                .build();

        Long bookId = bookService.createBook(book1);
        //when
        Book result = bookService.findBook(bookId);

        //then
        assertThat(book1.getId()).isEqualTo(result.getId());
    }

    @Test
    void 책_조회_리스트() {
        //given
        Book book1 = Book.builder()
                .name("죽은 시인의 사회")
                .category(BookCategory.NOW)
                .isbn("1234")
                .build();

        //given
        Book book2 = Book.builder()
                .name("군주론")
                .category(BookCategory.AFTER)
                .isbn("4321")
                .build();

        Long clubId1 = bookService.createBook(book1);
        Long clubId2 = bookService.createBook(book2);
        //when
        List<Book> books = bookService.findAllBooks();


        //then
        assertThat(books.size()).isEqualTo(2);
        assertThat(books).contains(book1,book2);
    }

    @Test
    void 책_정보_삭제() {
        //given
        Book book1 = Book.builder()
                .name("죽은 시인의 사회")
                .category(BookCategory.NOW)
                .isbn("1234")
                .build();

        Long bookId = bookService.createBook(book1);
        //when
        bookService.deleteBook(bookId);

        em.flush();
        em.clear();

        //then
        assertThatThrownBy(() -> bookService.findBook(bookId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("존재하지 않는 책입니다.");
    }

    @Test
    void 책_조회_카테고리() {
        //given
        Book book1 = Book.builder()
                .name("죽은 시인의 사회")
                .category(BookCategory.NOW)
                .isbn("1")
                .build();

        Book book2 = Book.builder()
                .name("죽은 시인의 수학")
                .category(BookCategory.NOW)
                .isbn("2")
                .build();

        Book book3 = Book.builder()
                .name("죽은 시인의 과학")
                .category(BookCategory.AFTER)
                .isbn("3")
                .build();

        Long bookId1 = bookService.createBook(book1);
        Long bookId2 = bookService.createBook(book2);
        Long bookId3 = bookService.createBook(book3);

        //when
        List<Book> result = bookService.findByCategory(BookCategory.NOW);

        //then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result).contains(book1,book2);
    }
}