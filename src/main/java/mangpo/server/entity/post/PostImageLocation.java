package mangpo.server.entity.post;

import lombok.*;
import mangpo.server.entity.post.Post;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostImageLocation {

    @Id @GeneratedValue
    @Column(name = "post_image_location_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private String imgLocation;

}
