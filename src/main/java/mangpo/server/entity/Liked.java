package mangpo.server.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Liked {

    @Id @GeneratedValue
    @Column(name = "liked_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private boolean isLiked;
}
