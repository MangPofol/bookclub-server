package mangpo.server.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mangpo.server.controller.PostController;
import mangpo.server.entity.Post;
import mangpo.server.entity.PostImageLocation;
import mangpo.server.entity.PostScope;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public  class PostResponseDto {
    private Long id;
    private PostScope scope;
    private Boolean isIncomplete;
    private String imgLocation;
    private String title;
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private String location;
    private String readTime;
    private String hyperlink;

    private List<String> postImgLocations;
    private HashMap<Long, String> postScopeClub = new HashMap<>();
    private List<LikedResponseDto> likedList;
    private List<CommentResponseDto> commentsDto;

    //        @QueryProjection
    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.scope = post.getScope();
        this.isIncomplete = post.getIsIncomplete();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.location = post.getLocation();
        this.readTime = post.getReadTime();
        this.hyperlink = post.getHyperlink();
        this.createdDate = post.getCreatedDate();
        this.modifiedDate = post.getModifiedDate();

        this.postImgLocations = post.getPostImageLocations().stream()
                .map(PostImageLocation::getImgLocation)
                .collect(Collectors.toList());

        this.likedList = post.getLikedList()
                .stream()
                .map(m -> new LikedResponseDto(m.getUser().getNickname(), m.getIsLiked()))
                .collect(Collectors.toList());

        this.commentsDto = post.getComments()
                .stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
    }

    public void addPostScopeClub(Long id, String clubName) {
        this.postScopeClub.put(id,clubName);
    }
}
