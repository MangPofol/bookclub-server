package mangpo.server.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import mangpo.server.entity.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.EntityManager;
import java.util.List;

import static mangpo.server.entity.QBook.book;
import static mangpo.server.entity.QClubBookUser.clubBookUser;
import static mangpo.server.entity.QUser.user;
import static org.springframework.util.ObjectUtils.isEmpty;

public class ClubBookUserRepositoryCustomImpl implements ClubBookUserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ClubBookUserRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


    //    @Query("select cbu form ClubBookUser cbu where cbu.user = :user and cbu.book =:book and cbu.club is null")
    public ClubBookUser findByUserAndBook(User user, Book book) {
        return queryFactory
                .selectFrom(clubBookUser)
                .where(clubBookUser.user.eq(user),
                        clubBookUser.book.eq(book),
                        clubBookUser.club.isNull())
                .fetchOne();
    }

    //book 정보 없는 조회
    public List<User> findUsersByClub(Club club) {
        return queryFactory
                .selectDistinct(user)
                .from(clubBookUser)
                .join(clubBookUser.user, user)
                .where(clubBookUser.user.isNotNull(),
                        clubBookUser.book.isNull(),
                        clubBookUser.club.eq(club))
                .fetch();
    }

    //club 정보 없는 조회
    public List<ClubBookUser> findClubBookUserByClub(Club club) {
        return queryFactory
                .selectFrom(clubBookUser)
                .join(clubBookUser.book, book).fetchJoin()
                .where(clubBookUser.user.isNotNull(),
                        clubBookUser.club.eq(club),
                        clubBookUser.book.isNotNull())
                .fetch();
    }

    @Override
    public Boolean isDuplicate(ClubBookUser clubBookUserRequest) {
        List<ClubBookUser> result = queryFactory
                .selectFrom(clubBookUser)
                .where(clubEq(clubBookUserRequest.getClub()),
                        bookEq(clubBookUserRequest.getBook()),
                        userEq(clubBookUserRequest.getUser()))
                .fetch();

        if(result.isEmpty())
            return Boolean.FALSE;
        return Boolean.TRUE;
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
