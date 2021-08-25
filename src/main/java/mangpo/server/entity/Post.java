package mangpo.server.entity;

import lombok.*;
import mangpo.server.entity.common.BaseTimeEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter
public class Post extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @Enumerated(EnumType.STRING)
    @Column(name = "post_type")
    private PostType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "post_scope")
    private PostScope scope;

    private Boolean isIncomplete;

    @Column(name = "post_img_location")
    private String imgLocation;

    @Column(name = "post_title")
    private String title;

    @Column(name = "post_content")
    private String content;

    @Builder.Default
    @OneToMany(mappedBy = "post")
    private List<Liked> likedList = new ArrayList<>();

    public void changeBook(Book book){
        this.book = book;
    }

    //==연관관계 편의 메소드==//
    public void addBook(Book book) {
        if(this.book != null)
            this.book.getPosts().remove(this);

        this.book = book;
        book.getPosts().add(this);
    }




}
