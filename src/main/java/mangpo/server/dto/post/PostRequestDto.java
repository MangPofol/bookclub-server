package mangpo.server.dto.post;

import lombok.Data;
import mangpo.server.entity.post.Post;
import mangpo.server.entity.post.PostScope;

import java.util.List;

@Data
public class PostRequestDto {
    private Long bookId;
    private PostScope scope;
    private Boolean isIncomplete;
    private String title;
    private String content;
    private String location;
    private String readTime;
    private List<LinkRequestDto> linkRequestDtos;
    private List<Long> clubIdListForScope;
    private List<String> postImgLocations;


    public Post toEntityExceptBookAndUser() {
        return Post.builder()
                .scope(this.scope)
                .isIncomplete(this.isIncomplete)
                .title(this.title)
                .content(this.content)
                .location(this.location)
                .readTime(this.readTime)
                .build();
    }
}