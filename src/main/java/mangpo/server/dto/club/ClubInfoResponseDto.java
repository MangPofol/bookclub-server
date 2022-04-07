package mangpo.server.dto.club;

import lombok.Data;
import mangpo.server.dto.book.BookAndUserDto;
import mangpo.server.dto.post.PostResponseDto;
import mangpo.server.dto.user.UsersInClubDto;
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

    private List<UsersInClubDto> usersInfo;
    private List<BookAndUserDto> bookAndUserInfo;//userId,bookId,isbn
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
        usersInfo = user.stream()
                .map(UsersInClubDto::new)
                .collect(Collectors.toList());
    }

    public void setBookAndUserInfo(List<ClubBookUser> cbuList) {
        bookAndUserInfo = cbuList.stream()
                .map(BookAndUserDto::new)
                .collect(Collectors.toList());
    }
}
