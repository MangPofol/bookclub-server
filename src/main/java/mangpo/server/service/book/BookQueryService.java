package mangpo.server.service.book;

import lombok.RequiredArgsConstructor;
import mangpo.server.entity.Book;
import mangpo.server.entity.BookCategory;
import mangpo.server.entity.User;
import mangpo.server.repository.book.BookQueryRepository;
import mangpo.server.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//주로 화면에 맞춘 복잡한 조회
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookQueryService {

    private final BookQueryRepository bookQueryRepository;
    private final UserRepository userRepository;

    public List<Book> findBooksByEmailAndBookCategory(String email, BookCategory bookCategory){
        User user = userRepository.findByEmail(email).orElseThrow(()-> new IllegalStateException("존재하지 않는 유저입니다."));

        return bookQueryRepository.findByUserAndBookCategory(user, bookCategory);
    }

}
