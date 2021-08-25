package mangpo.server.service;


import lombok.RequiredArgsConstructor;
import mangpo.server.entity.Liked;
import mangpo.server.entity.Post;
import mangpo.server.repository.LikedRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LikedService {

    private final LikedRepository likedRepository;

    @Transactional
    public Long createLiked(Liked liked){
        Liked save = likedRepository.save(liked);
    }

    @Transactional
    public void updateLiked(Long likedId, Liked likedRequest){
        Liked liked = likedRepository.findById(likedId).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 좋아요 정보입니다."));

        liked.
    }

}
