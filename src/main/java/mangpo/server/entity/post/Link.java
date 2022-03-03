package mangpo.server.entity.post;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Link {

    @Id @GeneratedValue
    @Column(name = "link_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(name = "hyperlink_title")
    private String hyperlinkTitle;

    private String hyperlink;

    public void addPost(Post post){
        this.post = post;
    }
}
