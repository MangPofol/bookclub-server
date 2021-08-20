package mangpo.server.service;

import mangpo.server.entity.*;
import mangpo.server.exeption.NotExistBookException;
import mangpo.server.repository.PostRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PostServiceTest {

    @Autowired PostService postService;
    @Autowired PostRepository postRepository;
    @Autowired BookService bookService;
    @Autowired EntityManager em;

    @Test
    void 게시물_생성() {
        //given
        Book book = Book.builder()
                .name("죽은 시인의 국어")
                .isbn("123죽시국123")
                .category(BookCategory.NOW)
                .build();

        bookService.createBook(book);

        Post post = Post.builder()
                .book(book)
                .type(PostType.MEMO)
                .scope(PostScope.PUBLIC)
                .isIncomplete(Boolean.TRUE)
                .imgLocation("123죽시국이미지123")
                .title("죽시국을 읽고")
                .content("죽시국 짱")
                .build();

        //when
        Long postId = postService.createPost(post);
        //then
        assertThat(post).isEqualTo(postRepository.findById(postId).get());
    }

    @Test
    void 게시물_단건_조회() {
        //given
        Book book = Book.builder()
                .name("죽은 시인의 국어")
                .isbn("123죽시국123")
                .category(BookCategory.NOW)
                .build();


        Long bookId = bookService.createBook(book);

        Post post = Post.builder()
                .type(PostType.MEMO)
                .scope(PostScope.PUBLIC)
                .isIncomplete(Boolean.TRUE)
                .imgLocation("123죽시국이미지123")
                .title("죽시국을 읽고")
                .content("죽시국 짱")
                .build();

        post.addBook(book);

        Long postId = postService.createPost(post);

        //when
        Post findPost = postService.findPost(postId);

        //then
        assertThat(post).isEqualTo(findPost);
    }

    @Test
    void 게시물_리스트_조회() {
        //given
        Book book = Book.builder()
                .name("죽은 시인의 국어")
                .isbn("123죽시국123")
                .category(BookCategory.NOW)
                .build();

        Long bookId = bookService.createBook(book);

        Post post = Post.builder()
                .type(PostType.MEMO)
                .scope(PostScope.PUBLIC)
                .isIncomplete(Boolean.TRUE)
                .imgLocation("123죽시국이미지123")
                .title("죽시국을 읽고")
                .content("죽시국 짱")
                .build();

        post.addBook(book);

        Long postId = postService.createPost(post);

        //when
        List<Post> posts = postService.findPostsByBookId(bookId);

        //then
        assertThat(posts.size()).isEqualTo(1);
        assertThat(posts.get(0)).isEqualTo(post);
    }

    @Test
    void 게시물_수정() {
        //given
        Book book = Book.builder()
                .name("죽은 시인의 국어")
                .isbn("123죽시국123")
                .category(BookCategory.NOW)
                .build();

        Long bookId = bookService.createBook(book);

        Post post = Post.builder()
                .type(PostType.MEMO)
                .scope(PostScope.PUBLIC)
                .isIncomplete(Boolean.TRUE)
                .imgLocation("123죽시국이미지123")
                .title("죽시국을 읽고")
                .content("죽시국 짱")
                .build();

        post.addBook(book);

        Long postId = postService.createPost(post);

        System.out.println("postId = " + postId);

        //when
        Post postRequest = Post.builder()
                .type(PostType.TOPIC)
                .scope(PostScope.PUBLIC)
                .isIncomplete(Boolean.TRUE)
                .imgLocation("123죽시국이미지123")
                .title("죽시국을 읽고")
                .content("죽시국 노잼")
                .build();

        postService.updatePost(postId,postRequest);

        em.flush();
        em.clear();

        Post postUpdated = postService.findPost(postId);

        //then
        assertThat(postUpdated.getContent()).isEqualTo("죽시국 노잼");
        assertThat(postUpdated.getType()).isEqualTo(PostType.TOPIC);
        assertThat(postUpdated.getId()).isEqualTo(postId);
    }

    @Test
    void 게시물_삭제() {
        //given
        Book book = Book.builder()
                .name("죽은 시인의 국어")
                .isbn("123죽시국123")
                .category(BookCategory.NOW)
                .build();

        Long bookId = bookService.createBook(book);

        Post post = Post.builder()
                .book(book)
                .type(PostType.MEMO)
                .scope(PostScope.PUBLIC)
                .isIncomplete(Boolean.TRUE)
                .imgLocation("123죽시국이미지123")
                .title("죽시국을 읽고")
                .content("죽시국 짱")
                .build();

        Long postId = postService.createPost(post);

        //when
        postService.deletePost(postId);

        //then
        assertThatThrownBy(() -> postService.findPost(postId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("존재하지 않는 포스트입니다.");
    }
}