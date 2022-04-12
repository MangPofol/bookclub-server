package mangpo.server.repository.post;

import mangpo.server.entity.post.Link;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LinkRepository extends JpaRepository<Link,Long> {
}
