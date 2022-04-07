package mangpo.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ClubUserCountInfoDto {
    //클럽 내부 특정 유저 카운트
    private Integer totalPosts;
    private Integer totalBooks;
    private Integer totalComments;
}
