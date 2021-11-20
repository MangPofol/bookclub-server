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
    @Column(name = "post_scope")
    private PostScope scope;

    private Boolean isIncomplete;

    @Column(name = "post_title")
    private String title;

    @Column(name = "post_content")
    private String content;

    private String location;
    private String readTime;
    @Column(name = "hyperlink_title")
    private String hyperlinkTitle;
    private String hyperlink;

    @Builder.Default
    @OneToMany(mappedBy = "post")
    private List<Liked> likedList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostImageLocation> postImageLocations = new ArrayList<>();

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
