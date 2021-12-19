package mangpo.server.service.book;


import lombok.RequiredArgsConstructor;
import mangpo.server.entity.*;

import mangpo.server.entity.book.Book;
import mangpo.server.entity.book.BookCategory;
import mangpo.server.entity.user.User;
import mangpo.server.repository.BookInfoRepository;
import mangpo.server.repository.book.BookRepository;
import mangpo.server.repository.ClubBookUserRepository;
import mangpo.server.repository.UserRepository;
import mangpo.server.service.ClubBookUserService;
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
//
//    @Transactional
//    public void createBookAndCBU(CreateBookDto createBookDto, Long userId) {
//        BookInfo bookInfo = BookInfo.builder()
//                .name(createBookDto.getName())
//                .isbn(createBookDto.getIsbn())
//                .build();
//        bookInfo = createOrFindBookInfo(bookInfo);
//
//        bookInfo = bookInfoService.createOrFindBookInfo(bookInfo);
////        bookInfo = createOrFindBookInfo(bookInfo);
//
//        Book newBook = Book.builder()
//                .category(createBookDto.getCategory())
//                .bookInfo(bookInfo)
//                .build();
//        Long bookId = bookService.createBook(newBook,bookInfo.getIsbn(),loginUser.getId());
//
//        ClubBookUser cbu = ClubBookUser.builder()
//                .book(newBook)
//                .user(loginUser)
//                .build();
//        cbuService.createClubBookUser(cbu);
//
//
//
//        bookInfoRepository.save(bookInfo);
//
//
//    }

    private void validateDuplicateBook(String isbn, User loginUser) {
        List<ClubBookUser> listByUser = cbuService.findListByUserExceptClub(loginUser);
//        List<ClubBookUser> listByUser = cbuRepository.findListByUserExceptClub(loginUser);

        Optional<ClubBookUser> any = listByUser.stream()
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

//    @Transactional
//    public void deleteByUserAndBook(Long userId, Long bookId) {
//        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 유저입니다."));
//        Book book = bookRepository.findById(bookId).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 책입니다."));
//        bookRepository.deleteByUserAndBook(user,book);
//    }

    @Transactional
    public void updateBook(Long id, Book bookRequest) {
        Book book = this.findBookById(id);

        if (bookRequest.getBookCategory() != null)
            book.changeCategory(bookRequest.getBookCategory());
    }

    public Book findBookById(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 책입니다."));
    }

    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    public List<Book> findByCategory(BookCategory bookCategory) {
        return bookRepository.findByBookCategory(bookCategory);
    }


    public Set<Book> findBooksByCurrentUserAndBookCategory(BookCategory category) {
        User user = userService.findUserFromToken();

        Set<Book> books = cbuService.findByUserAndClubIsNull(user).stream()
                .map(m -> m.getBook())
                .filter(m -> m.getBookCategory() == category)
                .collect(Collectors.toSet());

       return books;
    }
}
