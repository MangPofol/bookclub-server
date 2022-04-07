package mangpo.server.dto.club;

import lombok.Data;
import mangpo.server.dto.book.BookAndUserDto;
import mangpo.server.dto.post.PostResponseDto;
import mangpo.server.dto.user.UserResponseDto;
import mangpo.server.entity.*;
import mangpo.server.entity.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ClubInfoResponseDto {

    private Long id;
    private String name;
    private Integer level;
    private Long presidentId;
    private String description;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    //클럽 내부 전체 카운드
    private Integer totalUser = 0;
    private Integer totalPosts = 0;
    private Integer totalBooks = 0;
    private Integer totalComments = 0;
    private Integer totalLikes = 0;


    private List<UserResponseDto> userResponseDtos;
    private List<BookAndUserDto> bookAndUserDtos;//userId,bookId,isbn
    private List<PostResponseDto> trendingPosts = new ArrayList<>();

    public void setClubInfo(Club club) {
        this.id = club.getId();
        this.name = club.getName();
        this.level = club.getLevel();
        this.presidentId = club.getPresidentId();
        this.description = club.getDescription();
        this.createdDate = club.getCreatedDate();
        this.modifiedDate = club.getModifiedDate();
    }


//    public void setHotMemo(List<Post> posts) {
//        for (Post p : posts) {
//            hotMemo.put(p.getId(), p.getTitle());
//        }
//
//    }
//
//    public void setHotTopic(List<Post> posts) {
//        for (Post p : posts) {
//            hotTopic.put(p.getId(), p.getTitle());
//        }
//    }

    public void setUsersInfo(List<User> user) {
        this.userResponseDtos = user.stream()
                .map(UserResponseDto::new)
                .collect(Collectors.toList());
    }

    public void setBookAndUserDtos(List<ClubBookUser> cbuList) {
        this.bookAndUserDtos = cbuList.stream()
                .map(BookAndUserDto::new)
                .collect(Collectors.toList());
    }

    public void addLike(int cnt){
        this.totalLikes += cnt;
    }
    public void addComment(int cnt){
        this.totalComments += cnt;
    }

}
