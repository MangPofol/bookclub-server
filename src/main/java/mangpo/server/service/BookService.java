package mangpo.server.service;


import lombok.RequiredArgsConstructor;
import mangpo.server.entity.Book;

import mangpo.server.entity.BookCategory;
import mangpo.server.entity.User;
import mangpo.server.exeption.NotExistBookException;
import mangpo.server.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    @Transactional
    public Long createBook(Book book){
        validateDuplicateBook(book);
        bookRepository.save(book);
        return book.getId();
    }

    private void validateDuplicateBook(Book book) {
        Book findBook = bookRepository.findByIsbn(book.getIsbn());
        if (findBook != null){
            throw new IllegalStateException("이미 등록된 책입니다.");
        }
    }

    @Transactional
    public void deleteBook(Long id){
        Book book = bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 책입니다."));
        bookRepository.delete(book);
    }

    @Transactional
    public void updateBook(Long id, Book bookRequest){
        Book book = bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 책입니다."));
        book.updateBook(bookRequest);
    }

    public Book findBook(Long id){
        return bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 책입니다."));
    }

    public List<Book> findBooks(){
        return bookRepository.findAll();
    }

    public List<Book> findByBookCategory(BookCategory bookCategory){
        return bookRepository.findByCategory(bookCategory);
    }

}
