package mangpo.server.entity.book;

import lombok.*;
import mangpo.server.entity.common.BaseTimeEntity;
import mangpo.server.entity.post.Post;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "book_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_info_id")
    private BookInfo bookInfo;

    @Enumerated(EnumType.STRING)
    @Column(name = "book_category")
    private BookCategory bookCategory;

    @Builder.Default
    @OneToMany(mappedBy = "book")
    private List<Post> posts = new ArrayList<>();

    public void changeCategory(BookCategory bookCategory) {
        this.bookCategory = bookCategory;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                '}';
    }
}
