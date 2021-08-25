package mangpo.server.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    public void doLikeToPost(Post post){
        this.post = post;
        post.getLikedList().add(this);
    }

    public void undoLikeToPost(Post post){
        post.getLikedList().remove(this);
    }

    public void doLikeToBook(Book book){
        this.book = book;
        book.getLikedList().add(this);
    }

    public void undoLikeToBook(Book book){
        book.getLikedList().remove(this);
    }
}
