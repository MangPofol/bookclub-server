package mangpo.server.repository.cbu;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import mangpo.server.dto.ClubBookUserSearchCondition;
import mangpo.server.entity.book.Book;
import mangpo.server.entity.cbu.ClubBookUser;
import mangpo.server.entity.club.Club;
import mangpo.server.entity.user.User;

import javax.persistence.EntityManager;
import java.util.List;

import static mangpo.server.entity.QClubBookUser.clubBookUser;
import static org.springframework.util.ObjectUtils.isEmpty;


public class ClubBookUserRepositoryCustomImpl implements ClubBookUserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ClubBookUserRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Boolean isDuplicate(ClubBookUserSearchCondition cbuSearchCondition) {
        List<ClubBookUser> result = queryFactory
                .selectFrom(clubBookUser)
                .where(clubEq(cbuSearchCondition.getClub()),
                        bookEq(cbuSearchCondition.getBook()),
                        userEq(cbuSearchCondition.getUser()))
                .fetch();

        if (result.isEmpty())
            return Boolean.FALSE;
        return Boolean.TRUE;
    }

    @Override
    public List<ClubBookUser> findAllBySearchCondition(ClubBookUserSearchCondition cbuSearchCondition) {
        return queryFactory
                .selectFrom(clubBookUser)
                .where(clubEq(cbuSearchCondition.getClub()),
                        bookEq(cbuSearchCondition.getBook()),
                        userEq(cbuSearchCondition.getUser()))
                .fetch();
    }


    private BooleanExpression clubEq(Club club) {
        return isEmpty(club) ? null : clubBookUser.club.eq(club);
    }

    private BooleanExpression bookEq(Book book) {
        return isEmpty(book) ? null : clubBookUser.book.eq(book);
    }

    private BooleanExpression userEq(User user) {
        return isEmpty(user) ? null : clubBookUser.user.eq(user);
    }
}
