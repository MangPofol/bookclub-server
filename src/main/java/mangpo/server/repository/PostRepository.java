package mangpo.server.repository;

import mangpo.server.entity.book.Book;
import mangpo.server.entity.post.Post;
import mangpo.server.entity.user.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post,Long> {

    @EntityGraph(attributePaths = ("comments"))
    List<Post> findByBook(Book book);

    @EntityGraph(attributePaths = ("postImageLocations"))
    Optional<Post> findFetchById(Long id);

    long countByBook(Book book);
    Post findByUser(User user);
    void deleteAllByUser(User user);
    
    List<Post> findListByBook(Book b);
}
