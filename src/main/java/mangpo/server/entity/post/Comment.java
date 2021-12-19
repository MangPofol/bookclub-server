package mangpo.server.entity.post;

import lombok.*;
import mangpo.server.entity.common.BaseTimeEntity;
import mangpo.server.entity.post.Post;
import mangpo.server.entity.user.User;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    private Long parentCommentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_name")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "comment_content")
    private String content;


    //==연관관계 편의 메소드==//
    public void addComment(Post post) {
        if(this.post != null)
            this.post.getComments().remove(this);

        this.post = post;
        post.getComments().add(this);
    }

    public void createWithUserAndPost(User user, Post post){

    }

}
