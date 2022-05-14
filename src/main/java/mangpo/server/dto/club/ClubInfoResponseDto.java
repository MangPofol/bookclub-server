package mangpo.server.dto.club;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mangpo.server.dto.TrendingPostDto;
import mangpo.server.dto.book.BookAndUserDto;
import mangpo.server.dto.user.UserResponseDto;
import mangpo.server.entity.cbu.ClubBookUser;
import mangpo.server.entity.user.User;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class ClubInfoResponseDto {

    //클럽 내부 전체 카운드
//    @Builder.Default
    private int totalUser = 0;
//    @Builder.Default
    private int totalPosts = 0;
//    @Builder.Default
    private int totalBooks = 0;
//    @Builder.Default
    private int totalComments = 0;
//    @Builder.Default
    private int totalLikes = 0;

    private ClubResponseDto clubResponseDto;

    private List<UserResponseDto> userResponseDtos;
    private List<BookAndUserDto> bookAndUserDtos;//userId,bookId,isbn
    private List<TrendingPostDto> trendingPostDtos;

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
