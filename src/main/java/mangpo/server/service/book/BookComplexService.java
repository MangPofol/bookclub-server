package mangpo.server.service.book;

import lombok.RequiredArgsConstructor;
import mangpo.server.dto.ClubBookUserSearchCondition;
import mangpo.server.dto.book.CreateBookDto;
import mangpo.server.entity.*;
import mangpo.server.service.ClubBookUserService;
import mangpo.server.service.post.PostService;
import mangpo.server.service.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//book이 주축인 서비스 중계용 서비스. 여러 연관관계 보유. book 삭제시 ClubBookUser 삭제되야하는것 등의 처리.
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookComplexService {

    private final BookInfoService bookInfoService;
    private final BookService bookService;
    private final ClubBookUserService cbuService;
    private final UserService userService;
    private final PostService postService;

    @Transactional
    public Long createBookAndRelated(CreateBookDto createBookDto) {
        User user = userService.findUserFromToken();

        BookInfo bookInfo = BookInfo.builder()
                .name(createBookDto.getName())
                .isbn(createBookDto.getIsbn())
                .build();
        bookInfo = bookInfoService.createOrFindBookInfo(bookInfo);

        Book newBook = Book.builder()
                .bookCategory(createBookDto.getCategory())
                .bookInfo(bookInfo)
                .build();
        Long bookId = bookService.createBookWithValidation(newBook, bookInfo.getIsbn(), user.getId());

        ClubBookUser cbu = ClubBookUser.builder()
                .book(newBook)
                .user(user)
                .build();
        cbuService.createClubBookUser(cbu);

        return bookId;
    }

    @Transactional
    public void deleteBookAndRelated(Long bookId) {
        Book deleteBook = bookService.findBookById(bookId);

        ClubBookUserSearchCondition clubBookUserSearchCondition = new ClubBookUserSearchCondition();
        clubBookUserSearchCondition.setBook(deleteBook);
        List<ClubBookUser> allByCondition = cbuService.findAllByCondition(clubBookUserSearchCondition);
        cbuService.deleteAll(allByCondition);
//        bookService.deleteByUserAndBook(userId, bookId);

        List<Post> postsByBookId = postService.findPostsByBookId(bookId);
        postService.deleteAllWithCascade(postsByBookId);

        bookService.deleteBook(bookId);
    }
}
