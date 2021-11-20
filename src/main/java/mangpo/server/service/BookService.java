package mangpo.server.service;


import lombok.RequiredArgsConstructor;
import mangpo.server.entity.Book;

import mangpo.server.entity.BookCategory;
import mangpo.server.entity.BookInfo;
import mangpo.server.repository.BookRepository;
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

    @Transactional
    public Long createBook(Book book){
        bookRepository.save(book);
        return book.getId();
    }

    @Transactional
    public void deleteBook(Long id){
        Book book = bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 책입니다."));
        bookRepository.delete(book);
    }

    @Transactional
    public void updateBook(Long id, Book bookRequest){
        Book book = bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 책입니다."));

        if (bookRequest.getCategory() != null)
            book.changeCategory(bookRequest.getCategory());
    }

    public Book findBook(Long id){
        return bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 책입니다."));
    }

    public List<Book> findAllBooks(){
        return bookRepository.findAll();
    }

    public List<Book> findByCategory(BookCategory bookCategory){
        return bookRepository.findByCategory(bookCategory);
    }

}
