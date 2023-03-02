package mangpo.server.dto.post;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mangpo.server.dto.LikedResponseDto;
import mangpo.server.dto.comment.CommentResponseDto;
import mangpo.server.entity.post.Post;
import mangpo.server.entity.post.PostImageLocation;
import mangpo.server.entity.post.PostScope;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDto {
    private Long postId;
    private PostScope scope;
    private Boolean isIncomplete;
    private String title;
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private String location;
    private String readTime;

    private List<LinkResponseDto> linkResponseDtos = new ArrayList<>();

    private List<String> postImgLocations = new ArrayList<>();
    private List<Long> clubIdListForScope = new ArrayList<>();

    private List<LikedResponseDto> likedList = new ArrayList<>();
    private List<CommentResponseDto> commentsDto = new ArrayList<>();

    public PostResponseDto(Post post) {
        this.postId = post.getId();
        this.scope = post.getScope();
        this.isIncomplete = post.getIsIncomplete();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.location = post.getLocation();
        this.readTime = post.getReadTime();
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

        this.linkResponseDtos = post.getLinks()
                .stream()
                .map(LinkResponseDto::new)
                .collect(Collectors.toList());

    }


    public void addClubIdListForScope(Long clubId) {
        this.clubIdListForScope.add(clubId);
    }
}
