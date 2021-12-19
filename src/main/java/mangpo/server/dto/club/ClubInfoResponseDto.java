package mangpo.server.dto.club;

import lombok.Data;
import mangpo.server.dto.book.BookAndUserDto;
import mangpo.server.dto.user.UsersInClubDto;
import mangpo.server.entity.*;
import mangpo.server.entity.post.Post;
import mangpo.server.entity.user.ColorSet;
import mangpo.server.entity.user.User;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ClubInfoResponseDto {

    private Long id;
    private String name;
    private ColorSet colorSet;
    private Integer level;
    private Long presidentId;
    private String description;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    private HashMap<Long, String> hotMemo = new HashMap<>();//id,title
    private HashMap<Long, String> hotTopic = new HashMap<>();//id,title

    private List<UsersInClubDto> userInfo;
    private List<BookAndUserDto> bookAndUserInfo;//userId,bookId,isbn

    public void setClubInfo(Club club) {
        this.id = club.getId();
        this.name = club.getName();
        this.colorSet = club.getColorSet();
        this.level = club.getLevel();
        this.presidentId = club.getPresidentId();
        this.description = club.getDescription();
        this.createdDate = club.getCreatedDate();
        this.modifiedDate = club.getModifiedDate();
    }

    public void setTrendingPost(List<Post> posts) {
        for (Post p : posts) {
            hotTopic.put(p.getId(), p.getTitle());
        }
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

    public void setUsersInClubDtoList(List<User> user) {
        List<UsersInClubDto> collect = user.stream()
                .map(UsersInClubDto::new)
                .collect(Collectors.toList());

        userInfo = collect;
    }

    public void setBookAndUserDtoList(List<ClubBookUser> cbuList) {
        List<BookAndUserDto> collect = cbuList.stream()
                .map(BookAndUserDto::new)
                .collect(Collectors.toList());

        bookAndUserInfo = collect;
    }
}
