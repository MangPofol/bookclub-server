package mangpo.server.repository.book;

import com.querydsl.jpa.impl.JPAQueryFactory;
import mangpo.server.entity.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static mangpo.server.entity.QBook.book;
import static mangpo.server.entity.QBookInfo.bookInfo;
import static mangpo.server.entity.QClubBookUser.clubBookUser;


@Repository
@Transactional
public class BookQueryRepository {

    private final JPAQueryFactory queryFactory;

    public BookQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<Book> findByUserAndBookCategory(User userRequest, BookCategory bookCategory) {
        List<Book> listBook = queryFactory.
                selectDistinct(book).
                from(clubBookUser).
                join(clubBookUser.book, book).
//                join(book, book.bookInfo).fetchJoin().
                where(clubBookUser.user.eq(userRequest),
                        clubBookUser.book.isNotNull(),
                        clubBookUser.book.category.eq(bookCategory)).
                fetch();

        return listBook;
    }


}
