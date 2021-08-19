package mangpo.server.repository;

import mangpo.server.entity.Book;
import mangpo.server.entity.BookCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByCategory(BookCategory category);
    Book findByIsbn(String isbn);

}
