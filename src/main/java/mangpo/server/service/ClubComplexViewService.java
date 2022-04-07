package mangpo.server.service;

import lombok.RequiredArgsConstructor;
import mangpo.server.dto.ClubUserCountInfoDto;
import mangpo.server.dto.club.ClubInfoResponseDto;
import mangpo.server.dto.post.PostResponseDto;
import mangpo.server.entity.Club;
import mangpo.server.entity.ClubBookUser;
import mangpo.server.entity.book.Book;
import mangpo.server.entity.post.Post;
import mangpo.server.entity.user.User;
import mangpo.server.service.club.ClubService;
import mangpo.server.service.post.PostService;
import mangpo.server.service.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ClubComplexViewService {

    private final UserService userService;
    private final ClubService clubService;
    private final ClubBookUserService cbuService;
    private final PostService postService;
    private final LikedService likedService;
    private final CommentService commentService;


    //리팩토링 고려
    public ClubInfoResponseDto getClubInfoByClubId(Long clubId) {
        Club club = clubService.findById(clubId);
        List<User> usersInClub = cbuService.findUsersByClub(club);
        List<ClubBookUser> cbuListByClub = cbuService.findListByClubAndUserIsNotNullAndBookIsNotNull(club);

        //클럽의 책들
        List<Book> clubBooks = cbuListByClub.stream()
                .map(ClubBookUser::getBook)
                .collect(Collectors.toList());

        //클럽 책들의 포스트들
        List<Post> clubAllPosts = new ArrayList<>();
        for (Book b : clubBooks) {
            List<Post> bookPosts = postService.findListByBook(b);
            clubAllPosts.addAll(bookPosts);
        }


        ClubInfoResponseDto res = new ClubInfoResponseDto();

        //기준 이용 필터링
        //Trending Posts(memo) 노출 기준 : 좋아요 과반 + 댓글 클럽원수*2, 만족하는거 없으면 비워두기
        int usersSize = usersInClub.size();
        List<PostResponseDto> filteredPostsDto = filterWithPolicy(clubAllPosts, usersSize, res);


        res.setClubInfo(club);
        res.setBookAndUserDtos(cbuListByClub);
        res.setTrendingPosts(filteredPostsDto);
        res.setUsersInfo(usersInClub);

        res.setTotalUser(usersSize);
        res.setTotalPosts(clubAllPosts.size());
        res.setTotalBooks(clubBooks.size());

        return res;
    }

    //like,comment count도 함
    private List<PostResponseDto> filterWithPolicy(List<Post> clubAllPosts, int usersSize, ClubInfoResponseDto res) {
        return clubAllPosts.stream()
                .filter(p -> {
                    Integer cnt = likedService.countByPost(p);
                    res.addLike(cnt);
                    return cnt > usersSize / 2;
                })
                .filter(p -> {
                    Integer cnt = commentService.countByPost(p);
                    res.addComment(cnt);
                    return cnt > usersSize * 2L;
                })
                .map(PostResponseDto::new)
                .collect(Collectors.toList());
    }

    public ClubUserCountInfoDto findClubUserCountInfo(Long clubId, Long userId) {
        Club club = clubService.findById(clubId);
        User user = userService.findById(userId);
        List<ClubBookUser> cbu = cbuService.findByUserAndClubAndBookIsNotNull(user, club);

        int totalBooks = cbu.size();

        List<Book> books = cbu.stream()
                .map(m -> m.getBook())
                .collect(Collectors.toList());

        List<Post> allPosts = new ArrayList<>();
        for (Book b : books) {
            List<Post> posts = postService.findListByBook(b);
            allPosts.addAll(posts);
        }
        int totalPosts = allPosts.size();

        int totalComments = 0;
        for (Post p : allPosts) {
            totalComments += commentService.countByPost(p);
        }

        return ClubUserCountInfoDto.builder()
                .totalPosts(totalPosts)
                .totalComments(totalComments)
                .totalBooks(totalBooks)
                .build();
    }
}
