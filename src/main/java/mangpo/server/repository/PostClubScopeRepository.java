package mangpo.server.repository;

import mangpo.server.entity.Club;
import mangpo.server.entity.Post;
import mangpo.server.entity.PostClubScope;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostClubScopeRepository extends JpaRepository<PostClubScope,Long> {

    void deleteAllByPost(Post post);
    void deleteAllByClub(Club club);

    @EntityGraph(attributePaths = "club")
    List<PostClubScope> findByPost(Post post);
}
