package mangpo.server.repository.book;

import mangpo.server.entity.Book;
import mangpo.server.entity.BookCategory;
import mangpo.server.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByCategory(BookCategory category);
    void deleteByUserAndBook(User user, Book book);

//    Optional<Book> findByIsbn(String isbn);

    @Override
    @EntityGraph(attributePaths = ("posts"))
    Optional<Book> findById(Long id);

}
