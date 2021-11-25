package mangpo.server.service.book;


import lombok.RequiredArgsConstructor;
import mangpo.server.entity.*;

import mangpo.server.repository.BookInfoRepository;
import mangpo.server.repository.book.BookRepository;
import mangpo.server.repository.ClubBookUserRepository;
import mangpo.server.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookInfoRepository bookInfoRepository;
    private final ClubBookUserRepository cbuRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long createBookWithValidation(Book book, String isbn, Long userId) {
        User loginUser = userRepository.findById(userId).orElseThrow(() -> new IllegalStateException("잘못된 유저 정보입니다."));
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
        List<ClubBookUser> listByUser = cbuRepository.findListByUserExceptClub(loginUser);

        Optional<ClubBookUser> any = listByUser.stream()
                .filter(m -> m.getUser().getId().equals(loginUser.getId()))
                .filter(m -> m.getBook().getBookInfo().getIsbn().equals(isbn))
                .findAny();

        if (any.isPresent())
            throw new IllegalStateException("이미 유저가 등록한 책입니다.");
    }

    @Transactional
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 책입니다."));
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
        Book book = bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 책입니다."));

        if (bookRequest.getCategory() != null)
            book.changeCategory(bookRequest.getCategory());
    }

    public Book findBook(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 책입니다."));
    }

    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    public List<Book> findByCategory(BookCategory bookCategory) {
        return bookRepository.findByCategory(bookCategory);
    }

}
