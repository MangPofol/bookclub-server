package mangpo.server.dto.club;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mangpo.server.dto.user.UserResponseDto;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ClubUserInfoDto {
    private UserResponseDto userResponseDto;

    //클럽 내부 특정 유저 카운트
    private Integer totalPosts;
    private Integer totalBooks;
    private Integer totalComments;

    //유저가 클럽에 가입한 날짜
    private LocalDateTime invitedDate;
}
