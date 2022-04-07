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


    public ClubInfoResponseDto getClubInfoByClubId(Long clubId) {
        Club club = clubService.findById(clubId);
        List<User> usersInClub = cbuService.findUsersByClub(club);
        List<ClubBookUser> cbuListByClub = cbuService.findClubBookUserByClub(club);

        //클럽의 책들
        List<Book> clubBooks = cbuListByClub.stream()
                .map(ClubBookUser::getBook)
                .collect(Collectors.toList());

        //클럽 책들의 포스트들
        List<Post> clubAllPosts = new ArrayList<>();
        for (Book book: clubBooks) {
            List<Post> bookPosts = postService.findPostsByBookId(book.getId());
            clubAllPosts.addAll(bookPosts);
        }

        //기준 이용 필터링
        //Trending Posts(memo) 노출 기준 : 좋아요 과반 + 댓글 클럽원수*2, 만족하는거 없으면 비워두기
        int usersSize = usersInClub.size();
        List<PostResponseDto> filteredPostsDto = filterWithPolicy(clubAllPosts, usersSize);

        ClubInfoResponseDto clubInfoResponseDto = new ClubInfoResponseDto();
        
        clubInfoResponseDto.setClubInfo(club);
        clubInfoResponseDto.setUsersInfo(usersInClub);
        clubInfoResponseDto.setBookAndUserInfo(cbuListByClub);
        clubInfoResponseDto.setTrendingPosts(filteredPostsDto);
        clubInfoResponseDto.setTotalUser(usersSize);
        clubInfoResponseDto.setTotalPosts(clubAllPosts.size());
        
        return clubInfoResponseDto;
    }

    private List<PostResponseDto> filterWithPolicy(List<Post> clubAllPosts, int usersSize) {
        return clubAllPosts.stream()
                .filter(m -> likedService.countByPost(m) > usersSize / 2)
                .filter(m -> commentService.countByPost(m) > usersSize * 2L)
                .map(PostResponseDto::new)
                .collect(Collectors.toList());
    }
}
