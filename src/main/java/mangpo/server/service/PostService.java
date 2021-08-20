package mangpo.server.service;

import lombok.RequiredArgsConstructor;
import mangpo.server.entity.Book;
import mangpo.server.entity.Post;
import mangpo.server.entity.User;
import mangpo.server.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public Long createPost(Post post){
        postRepository.save(post);
        return post.getId();
    }

    @Transactional
    public void updatePost(Long postId, Post postRequest){
        Post post = postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 포스트입니다."));

        post.setBook(postRequest.getBook());
        post.setType(postRequest.getType());
        post.setScope(postRequest.getScope());
        post.setIsIncomplete(postRequest.getIsIncomplete());
        post.setImgLocation(postRequest.getImgLocation());
        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());
    }

    @Transactional
    public void deletePost(Long id){
        postRepository.findById(id).orElseThrow(()->  new EntityNotFoundException("존재하지 않는 포스트입니다."));
        postRepository.deleteById(id);
    }
    public List<Post> findPostsByBook(Book book){
        return postRepository.findByBook(book);
    }


}
