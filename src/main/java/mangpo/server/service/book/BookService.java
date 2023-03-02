package mangpo.server.service.book;


import lombok.RequiredArgsConstructor;
import mangpo.server.entity.book.Book;
import mangpo.server.entity.book.BookCategory;
import mangpo.server.entity.cbu.ClubBookUser;
import mangpo.server.entity.user.User;
import mangpo.server.repository.book.BookRepository;
import mangpo.server.service.cbu.ClubBookUserService;
import mangpo.server.service.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final ClubBookUserService cbuService;
    private final UserService userService;

    @Transactional
    public Long createBookWithValidation(Book book, String isbn, Long userId) {
        User loginUser = userService.findById(userId);
        validateDuplicateBook(isbn, loginUser);

        bookRepository.save(book);
        return book.getId();
    }

    private void validateDuplicateBook(String isbn, User loginUser) {
        List<ClubBookUser> cbuList = cbuService.findListByUserAndClubIsNullAndBookIsNotNull(loginUser);

        Optional<ClubBookUser> any = cbuList.stream()
                .filter(m -> m.getUser().getId().equals(loginUser.getId()))
                .filter(m -> m.getBook().getBookInfo().getIsbn().equals(isbn))
                .findAny();

        if (any.isPresent())
            throw new IllegalStateException("이미 유저가 등록한 책입니다.");
    }

    @Transactional
    public void deleteBook(Long id) {
        Book book = this.findBookById(id);
        bookRepository.delete(book);
    }

    @Transactional
    public void updateBook(Long id, Book bookRequest) {
        Book book = this.findBookById(id);

        if (bookRequest.getBookCategory() != null)
            book.changeCategory(bookRequest.getBookCategory());
    }

    public Book findBookById(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 책입니다."));
    }

    public Set<Book> findBooksByCurrentUserAndBookCategory(BookCategory category) {
        User user = userService.findUserFromToken();

        return cbuService.findByUserAndClubIsNull(user).stream()
                .map(ClubBookUser::getBook)
                .filter(m -> m.getBookCategory() == category)
                .collect(Collectors.toSet());
    }

    public Long findTotalBook() {
        return cbuService.countByUserAndClubIsNull(userService.findUserFromToken());
    }
}
