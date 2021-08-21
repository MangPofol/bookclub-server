package mangpo.server.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import mangpo.server.entity.Book;
import mangpo.server.entity.ClubBookUser;
import mangpo.server.entity.QClubBookUser;
import mangpo.server.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.EntityManager;
import java.util.List;

import static mangpo.server.entity.QClubBookUser.clubBookUser;

public class ClubBookUserRepositoryCustomImpl implements ClubBookUserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ClubBookUserRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


//    @Query("select cbu form ClubBookUser cbu where cbu.user = :user and cbu.book =:book and cbu.club is null")
    public ClubBookUser findByUserAndBook(User user, Book book){
        return queryFactory
                .selectFrom(clubBookUser)
                .where(clubBookUser.user.eq(user),
                        clubBookUser.book.eq(book),
                        clubBookUser.club.isNull())
                .fetchOne();
    }
}
