package mangpo.server.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import mangpo.server.entity.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static mangpo.server.entity.QClubBookUser.clubBookUser;


@Repository
@Transactional
public class BookQueryRepository {

    private final JPAQueryFactory queryFactory;

    public BookQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public Long deleteClubBookUserByUserAndBook(User userRequest, Book bookRequest){
        Long count = queryFactory
                .delete(clubBookUser)
                .where(clubBookUser.user.eq(userRequest),
                        clubBookUser.book.eq(bookRequest))
                .execute();

        return count;
    }



}
