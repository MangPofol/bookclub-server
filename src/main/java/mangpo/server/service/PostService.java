package mangpo.server.service;

import lombok.RequiredArgsConstructor;
import mangpo.server.controller.PostController;
import mangpo.server.entity.Book;
import mangpo.server.entity.Post;
import mangpo.server.entity.PostClubScope;
import mangpo.server.entity.User;
import mangpo.server.repository.BookRepository;
import mangpo.server.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final BookRepository bookRepository;

    @Transactional
    public Long createPost(Post post){
        postRepository.save(post);
        return post.getId();
    }

    @Transactional
    public void updatePost(Long postId, Post postRequest){
        Post post = postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 포스트입니다."));
        //changeXXX로 변환
        //TODO bean validation 필요

        post.setScope(postRequest.getScope());
        post.setIsIncomplete(postRequest.getIsIncomplete());
        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());
    }

    @Transactional
    public void deletePost(Long id){
        postRepository.findById(id).orElseThrow(()->  new EntityNotFoundException("존재하지 않는 포스트입니다."));
        postRepository.deleteById(id);
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
