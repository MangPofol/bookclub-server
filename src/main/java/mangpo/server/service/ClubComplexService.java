package mangpo.server.service;

import lombok.RequiredArgsConstructor;
import mangpo.server.dto.club.ClubInfoResponseDto;
import mangpo.server.dto.post.PostResponseDto;
import mangpo.server.entity.Club;
import mangpo.server.entity.ClubBookUser;
import mangpo.server.entity.book.Book;
import mangpo.server.entity.post.Post;
import mangpo.server.entity.user.User;
import mangpo.server.service.club.ClubService;
import mangpo.server.service.post.PostService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ClubComplexService {

    private final ClubService clubService;
    private final ClubBookUserService cbuService;
    private final PostService postService;
    private final LikedService likedService;
    private final CommentService commentService;

    //TODO: 최적화, 코드 개선 필요
    public ClubInfoResponseDto getClubInfoByClubId(Long clubId) {
        Club club = clubService.findById(clubId);
        List<User> usersInClub = cbuService.findUsersByClub(club);
        List<ClubBookUser> cbuByClub = cbuService.findClubBookUserByClub(club);

        ClubInfoResponseDto clubInfoResponseDto = new ClubInfoResponseDto();

        clubInfoResponseDto.setClubInfo(club);
        clubInfoResponseDto.setUsersInfo(usersInClub);
        clubInfoResponseDto.setBookAndUserInfo(cbuByClub);

        //기준:좋아요 과반 + 댓글 클럽원수*2, 만족하는거 없으면 비워두기
        //cbu 의 book의
        int usersSize = usersInClub.size();

        List<Book> books = cbuByClub.stream()
                .map(ClubBookUser::getBook)
                .collect(Collectors.toList());

        List<Post> clubPosts = new ArrayList<>();
        for (Book book: books) {
            List<Post> list = postService.findPostsByBookId(book.getId());
            clubPosts.addAll(list);
        }

        List<PostResponseDto> filteredPostsDto = clubPosts.stream()
                .filter(m -> likedService.countByPost(m) > usersSize / 2)
                .filter(m -> commentService.countByPost(m) > usersSize * 2L)
                .map(PostResponseDto::new)
                .collect(Collectors.toList());

        clubInfoResponseDto.setTrendingPosts(filteredPostsDto);


//        List<Post> trendingPostByClub = clubQueryRepository.findTrendingPostByClub(club, usersInClub.size());
        return clubInfoResponseDto;
    }
}
