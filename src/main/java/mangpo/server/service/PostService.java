package mangpo.server.service;

import lombok.RequiredArgsConstructor;
import mangpo.server.dto.PostRequestDto;
import mangpo.server.entity.*;
import mangpo.server.repository.*;
import mangpo.server.repository.book.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final BookRepository bookRepository;
    private final LikedRepository likedRepository;
    private final PostClubScopeRepository pcsRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public Long createPost(Post post){
        postRepository.save(post);
        return post.getId();
    }

    @Transactional
    public void updatePost(Long postId, PostRequestDto postRequestDto){
        Post post = postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 포스트입니다."));
        post.update(postRequestDto);
    }

    @Transactional
    public void deletePost(Long id){
        Post post = postRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 포스트입니다."));

        List<Liked> likedList = likedRepository.findAllByPost(post);
        likedRepository.deleteAll(likedList);

        List<PostClubScope> pcsList = pcsRepository.findAllByPost(post);
        pcsRepository.deleteAll(pcsList);

        List<Comment> commentList = commentRepository.findAllByPost(post);
        commentRepository.deleteAll(commentList);

        postRepository.deleteById(id);
    }

    @Transactional
    public void deleteAll(List<Post> posts){
        postRepository.deleteAll(posts);
    }

    public void deleteAllWithCascade(List<Post> posts){
        for (Post post : posts) {
            deletePost(post.getId());
        }
    }

    public Post findPost(Long id){
        return postRepository.findById(id).orElseThrow(()->  new EntityNotFoundException("존재하지 않는 포스트입니다."));
    }

    public Post findPostFetchJoinImgLoc(Long id){
        return postRepository.findFetchById(id).orElseThrow(()->  new EntityNotFoundException("존재하지 않는 포스트입니다."));
    }

    public List<Post> findPostsByBookId(Long bookId){
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 책입니다."));
        List<Post> byBook = postRepository.findByBook(book);

        return byBook;
    }


}
