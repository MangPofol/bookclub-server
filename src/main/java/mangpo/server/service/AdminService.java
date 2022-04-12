package mangpo.server.service;

import lombok.RequiredArgsConstructor;
import mangpo.server.entity.cbu.ClubBookUser;
import mangpo.server.entity.post.Post;
import mangpo.server.entity.user.User;
import mangpo.server.repository.book.BookRepository;
import mangpo.server.repository.cbu.ClubBookUserRepository;
import mangpo.server.repository.club.InviteRepository;
import mangpo.server.repository.post.CommentRepository;
import mangpo.server.repository.post.PostClubScopeRepository;
import mangpo.server.repository.post.PostRepository;
import mangpo.server.repository.user.LikedRepository;
import mangpo.server.repository.user.UserAuthorityRepository;
import mangpo.server.repository.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;


@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class AdminService {

    private final CommentRepository commentRepository;
    private final LikedRepository likedRepository;
    private final UserAuthorityRepository userAuthorityRepository;
    private final ClubBookUserRepository cbuRepository;
    private final PostClubScopeRepository pcsRepository;
    private final PostRepository postRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final InviteRepository inviteRepository;


    @Transactional
    public void deleteUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 유저입니다."));

        inviteRepository.deleteByUser(user);

        commentRepository.deleteAllByUser(user);
        likedRepository.deleteAllByUser(user);
        userAuthorityRepository.deleteAllByUser(user);


        Post post = postRepository.findByUser(user).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 책입니다."));
        pcsRepository.deleteAllByPost(post);
        postRepository.deleteAllByUser(user);

        List<ClubBookUser> cbuList = cbuRepository.findListByUserAndClubIsNull(user);
        cbuRepository.deleteAllByUser(user);

        for (ClubBookUser cbu: cbuList) {
            bookRepository.delete(cbu.getBook());
        }

        userRepository.delete(user);
    }
}
