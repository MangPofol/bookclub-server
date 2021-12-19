package mangpo.server.service.post;

import lombok.RequiredArgsConstructor;
import mangpo.server.entity.Club;
import mangpo.server.entity.post.Post;
import mangpo.server.entity.post.PostClubScope;
import mangpo.server.repository.PostClubScopeRepository;
import mangpo.server.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostClubScopeService {

    private final PostClubScopeRepository pcsRepository;

    @Transactional
    public Long createPCS(PostClubScope pcs){
        pcsRepository.save(pcs);
        return pcs.getId();
    }

    @Transactional
    public void deleteAllPcsByPost(Post post){
        pcsRepository.deleteAllByPost(post);
    }

    @Transactional
    public void deleteAllPcsByClub(Club club){
        pcsRepository.deleteAllByClub(club);
    }

    public PostClubScope findPCS(Long id){
        return pcsRepository.findById(id).orElseThrow(()->  new EntityNotFoundException("존재하지 않는 공개범위입니다."));
    }

    public List<PostClubScope> findListByPost(Post post){
        return pcsRepository.findAllByPost(post);
    }

    public List<PostClubScope> findListByClub(Club club){
        return pcsRepository.findAllByClub(club);
    }

}
