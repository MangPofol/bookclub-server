package mangpo.server.service.book;

import lombok.RequiredArgsConstructor;
import mangpo.server.dto.book.CreateBookDto;
import mangpo.server.entity.book.Book;
import mangpo.server.entity.book.BookInfo;
import mangpo.server.entity.cbu.ClubBookUser;
import mangpo.server.entity.post.Post;
import mangpo.server.entity.user.User;
import mangpo.server.service.cbu.ClubBookUserService;
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
//Todo: 테스트 작성시 mcok 해서 하는게 의미 있나? 그냥 통합 테스트 하는게 맞는거 아닌가?
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
        Book book = bookService.findBookById(bookId);

        List<ClubBookUser> listByBook = cbuService.findListByBook(book);
        cbuService.deleteAll(listByBook);

        List<Post> postsByBookId = postService.findListByBookId(bookId);
        postService.deleteAllWithCascade(postsByBookId);

        bookService.deleteBook(bookId);
    }
}
