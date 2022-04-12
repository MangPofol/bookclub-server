package mangpo.server.repository.post;

import mangpo.server.entity.club.Club;
import mangpo.server.entity.post.Post;
import mangpo.server.entity.post.PostClubScope;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostClubScopeRepository extends JpaRepository<PostClubScope,Long> {

    void deleteAllByPost(Post post);

    void deleteAllByClub(Club club);

    @EntityGraph(attributePaths = "club")
    List<PostClubScope> findListWithClubByPost(Post post);

    @EntityGraph(attributePaths = "post")
    List<PostClubScope> findListWithPostByPost(Post post);

    @EntityGraph(attributePaths = "club")
    List<PostClubScope> findAllByClub(Club club);

}
