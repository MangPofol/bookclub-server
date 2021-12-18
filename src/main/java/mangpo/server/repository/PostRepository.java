package mangpo.server.repository;

import mangpo.server.entity.Book;
import mangpo.server.entity.Post;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post,Long> {

    @EntityGraph(attributePaths = ("comments"))
    List<Post> findByBook(Book book);

    @EntityGraph(attributePaths = ("postImageLocations"))
    Optional<Post> findFetchById(Long id);

    long count();
}
