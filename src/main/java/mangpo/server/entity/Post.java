package mangpo.server.entity;

import lombok.Getter;
import mangpo.server.entity.common.BaseTimeEntity;

import javax.persistence.*;

@Entity
@Getter
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

    private boolean isIncomplete;

    @Column(name = "post_img_location")
    private String imgLocation;

    @Column(name = "post_title")
    private String title;

    @Column(name = "post_content")
    private String content;

}
