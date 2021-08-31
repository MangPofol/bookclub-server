//package mangpo.server.repository;
//
//
//import com.querydsl.jpa.impl.JPAQueryFactory;
//import mangpo.server.controller.PostController;
//import mangpo.server.controller.QPostController_PostResponseDto;
//import mangpo.server.entity.*;
//import org.springframework.stereotype.Repository;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.persistence.EntityManager;
//import java.util.List;
//
//import static mangpo.server.entity.QBook.book;
//import static mangpo.server.entity.QClubBookUser.clubBookUser;
//import static mangpo.server.entity.QLiked.liked;
//import static mangpo.server.entity.QPost.post;
//
//@Repository
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
//
//    public List<PostController.PostResponseDto> findPostsByBookId(Long bookId) {
//        return queryFactory.
//                select(new QPostController_PostResponseDto(post)).
//                from(post).
//                rightJoin(liked).on(liked.post.eq(post)).
//                where().
//                fetch();
//    }
//}
