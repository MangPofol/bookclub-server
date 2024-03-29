package mangpo.server.repository.book;

import mangpo.server.entity.book.Book;
import mangpo.server.entity.book.BookCategory;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByBookCategory(BookCategory bookCategory);

    @Override
    @EntityGraph(attributePaths = ("posts"))
    Optional<Book> findById(Long id);

    @EntityGraph(attributePaths = ("bookCategory"))
    Optional<Book> findByIdAndBookCategory(Long bookId, BookCategory bookCategory);
}
