package mangpo.server.dto;

import lombok.Data;
import mangpo.server.entity.Post;
import mangpo.server.entity.PostScope;

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
    private String hyperlinkTitle;
    private String hyperlink;
    private List<Long> clubIdListForScope;
    private List<String> postImgLocations;


    public Post toEntityExceptBook() {
        return Post.builder()
                .scope(this.scope)
                .isIncomplete(this.isIncomplete)
                .title(this.title)
                .content(this.content)
                .location(this.location)
                .readTime(this.readTime)
                .hyperlinkTitle(this.hyperlinkTitle)
                .hyperlink(this.hyperlink)
                .build();
    }
}