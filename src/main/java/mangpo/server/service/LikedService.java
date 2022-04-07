package mangpo.server.service;


import lombok.RequiredArgsConstructor;
import mangpo.server.entity.Liked;
import mangpo.server.entity.post.Post;
import mangpo.server.repository.LikedRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Transactional
    public void deleteByPost(Post post){
        likedRepository.deleteByPost(post);
    }

    public List<Liked> findAllByPost(Post post){
        return likedRepository.findAllByPost(post);
    }

    @Transactional
    public void deleteAll(List<Liked> likedList){
        likedRepository.deleteAll(likedList);
    }

    public Integer countByPost(Post post){
        return likedRepository.countByPost(post);
    }

}
