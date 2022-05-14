package mangpo.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mangpo.server.dto.post.PostResponseDto;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrendingPostDto {

    private String nickname;
    private String profileImgLocation;

    private String bookName;

    private PostResponseDto postResponseDto;
}
