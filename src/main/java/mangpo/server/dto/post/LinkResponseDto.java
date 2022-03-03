package mangpo.server.dto.post;

import lombok.Data;
import mangpo.server.entity.post.Link;

@Data
public class LinkResponseDto {

    private Long linkId;
    private String hyperLink;
    private String hyperlinkTitle;

    public LinkResponseDto(Link link){
        this.linkId = link.getId();
        this.hyperLink = link.getHyperlink();
        this.hyperlinkTitle = link.getHyperlinkTitle();
    }
}
