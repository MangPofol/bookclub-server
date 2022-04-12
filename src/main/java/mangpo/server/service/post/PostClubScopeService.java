package mangpo.server.service.post;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mangpo.server.dto.post.PostResponseDto;
import mangpo.server.entity.club.Club;
import mangpo.server.entity.post.Post;
import mangpo.server.entity.post.PostClubScope;
import mangpo.server.entity.post.PostScope;
import mangpo.server.repository.post.PostClubScopeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
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

    public List<PostClubScope> findListWithClubByPost(Post post){
        return pcsRepository.findListWithClubByPost(post);
    }

    public List<PostClubScope> findListWithPostByPost(Post post){
        return pcsRepository.findListWithPostByPost(post);
    }

    public List<PostClubScope> findListByClub(Club club){
        return pcsRepository.findAllByClub(club);
    }

    public List<PostClubScope> findAllByPost(Post post){
        return pcsRepository.findListWithClubByPost(post);
    }

    @Transactional
    public void deleteAll(List<PostClubScope> pcsList){
        pcsRepository.deleteAll(pcsList);
    }


    public List<PostResponseDto> createPostResponseDtoList(List<Post> posts ) {
        List<PostResponseDto> collect = new ArrayList<>();

        for (Post post : posts) {
            PostResponseDto postResponseDto = new PostResponseDto(post);
            PostScope scope = post.getScope();

            if (scope.equals(PostScope.CLUB)){
                List<PostClubScope> listByPost = findListWithClubByPost(post);

                for (PostClubScope pcs : listByPost) {
                    postResponseDto.addClubIdListForScope(pcs.getClub().getId());
                }
            }
            collect.add(postResponseDto);
        }
        return collect;
    }

    public PostResponseDto createPostResponseDto(Post post) {
            PostResponseDto postResponseDto = new PostResponseDto(post);
            PostScope scope = post.getScope();

            if (scope.equals(PostScope.CLUB)){
                List<PostClubScope> listByPost = findListWithClubByPost(post);
                for (PostClubScope pcs : listByPost) {
                    postResponseDto.addClubIdListForScope(pcs.getClub().getId());
                }
            }

        return postResponseDto;
    }

}
