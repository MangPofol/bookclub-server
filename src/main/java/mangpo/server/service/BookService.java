package mangpo.server.service;


import lombok.RequiredArgsConstructor;
import mangpo.server.entity.Book;

import mangpo.server.exeption.NotExistBookException;
import mangpo.server.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    @Transactional
    public void createBook(Book book){
        bookRepository.save(book);
    }

    @Transactional
    public void deleteBook(Long id){
        Book book = bookRepository.findById(id).orElseThrow(() -> new NotExistBookException("존재하지 않는 책입니다."));
        bookRepository.delete(book);
    }

    public Book findBook(Long id){
        return bookRepository.findById(id).orElseThrow(() -> new NotExistBookException("존재하지 않는 책입니다."));
    }

    public List<Book> findBooks(){
        return bookRepository.findAll();
    }
}
