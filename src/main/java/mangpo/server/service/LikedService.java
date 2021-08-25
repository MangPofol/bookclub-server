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
        likedRepository.save(liked);
        return liked.getId();
    }

    @Transactional
    public void deleteLiked(Liked liked){
        likedRepository.delete(liked);
    }

}
