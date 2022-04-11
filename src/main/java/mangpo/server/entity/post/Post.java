package mangpo.server.entity.post;

import lombok.*;
import mangpo.server.dto.post.PostRequestDto;
import mangpo.server.entity.*;
import mangpo.server.entity.book.Book;
import mangpo.server.entity.common.BaseTimeEntity;
import mangpo.server.entity.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

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

    @Builder.Default
    @OneToMany(mappedBy = "post")
    private List<Liked> likedList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostImageLocation> postImageLocations = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Link> links = new ArrayList<>();

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

    public void addUser(User user){
        this.user = user;
    }


    //book은 업데이트 하지 않음.
    public void update(PostRequestDto p){
        this.scope = p.getScope();
        this.isIncomplete = p.getIsIncomplete();
        this.title = p.getTitle();
        this.content = p.getContent();
        this.location = p.getLocation();
        this.readTime = p.getReadTime();
    }

    public void addLink(Link link){
        this.links.add(link);
        link.addPost(this);
    }


}
