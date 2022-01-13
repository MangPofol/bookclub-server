package mangpo.server.service.book;

import lombok.RequiredArgsConstructor;
import mangpo.server.entity.book.BookInfo;
import mangpo.server.repository.BookInfoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookInfoService {

    private final BookInfoRepository bookInfoRepository;

    @Transactional
    public Long createBookInfo(BookInfo bookInfo){
        validateDuplicateBookInfo(bookInfo.getIsbn());
        bookInfoRepository.save(bookInfo);
        return bookInfo.getId();
    }

    public void validateDuplicateBookInfo(String isbn) {
        if(isbn == null)
            throw new IllegalStateException("isbn 정보가 없습니다.");

        Optional<BookInfo> findBookInfo = bookInfoRepository.findByIsbn(isbn);

        if (findBookInfo.isPresent()){
            throw new IllegalStateException("이미 등록된 책 정보입니다.");
        }
    }

    @Transactional
    public void deleteBookInfo(Long id){
        BookInfo bookInfo = this.findBookInfoById(id);
        bookInfoRepository.delete(bookInfo);
    }

    public BookInfo findBookInfoById(Long id){
        return bookInfoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 책 정보입니다."));
    }
    public BookInfo findBookInfoByIsbn(String isbn){
        return bookInfoRepository.findByIsbn(isbn).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 책 정보입니다."));
    }

    public BookInfo createOrFindBookInfo(BookInfo bookInfo) {
        try {
            createBookInfo(bookInfo);
        }catch (IllegalStateException e){
            bookInfo = this.findBookInfoByIsbn(bookInfo.getIsbn());
        }
        return bookInfo;
    }
}
