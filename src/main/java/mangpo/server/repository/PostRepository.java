package mangpo.server.repository;

import mangpo.server.entity.Book;
import mangpo.server.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long> {

    List<Post> findByBook(Book book);
}
