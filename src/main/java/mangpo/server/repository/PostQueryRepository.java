//package mangpo.server.repository;
//
//
//import com.querydsl.jpa.impl.JPAQueryFactory;
//import mangpo.server.entity.*;
//import org.springframework.stereotype.Repository;
//
//import javax.persistence.EntityManager;
//import java.util.List;
//
//import static mangpo.server.entity.QBook.book;
//import static mangpo.server.entity.QClub.club;
//import static mangpo.server.entity.QClubBookUser.clubBookUser;
//
//@Repository
////@RequiredArgsConstructor
//public class PostQueryRepository {
//
//
//    private final JPAQueryFactory queryFactory;
//
//    public PostQueryRepository(EntityManager em) {
//        this.queryFactory = new JPAQueryFactory(em);
//    }
//
//
//    public List<Book> findListByUser(User user) {
//        return queryFactory
//                .selectDistinct(book)
//                .from(clubBookUser)
//                .join(clubBookUser.book, book)
//                .where(QUser.user.eq(user),
//                        club.isNull())
//                .fetch();
//    }
//}
