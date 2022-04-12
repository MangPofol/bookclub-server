package mangpo.server.entity.user;

import lombok.*;
import mangpo.server.entity.post.Post;
import mangpo.server.entity.user.User;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Liked {

    @Id @GeneratedValue
    @Column(name = "liked_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private Boolean isLiked;

    public void doLikeToPost(Post post){
        this.post = post;
        post.getLikedList().add(this);
    }

    public void undoLikeToPost(Post post){
        post.getLikedList().remove(this);
    }

}
