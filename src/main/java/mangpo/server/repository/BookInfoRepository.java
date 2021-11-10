package mangpo.server.repository;

import mangpo.server.entity.BookInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookInfoRepository extends JpaRepository<BookInfo, Long> {

    Optional<BookInfo> findByIsbn(String isbn);
}
