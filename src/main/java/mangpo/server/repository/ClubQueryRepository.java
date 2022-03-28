package mangpo.server.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import mangpo.server.entity.*;
import mangpo.server.entity.post.Post;
import mangpo.server.entity.user.User;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static mangpo.server.entity.QClubBookUser.clubBookUser;
import static mangpo.server.entity.QLiked.liked;
import static mangpo.server.entity.book.QBook.book;
import static mangpo.server.entity.post.QComment.comment;
import static mangpo.server.entity.post.QPost.post;

@Slf4j
@Repository
public class ClubQueryRepository {


    private final JPAQueryFactory queryFactory;

    public ClubQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

//    public List<Club> findDistinctClub(User userRequest) {
//        return queryFactory
//                .select(clubBookUser.club).distinct()
//                .from(clubBookUser)
//                .where(clubBookUser.club.isNotNull(), clubBookUser.user.eq(userRequest))
//                .fetch();
//
//    }


    //갈아엎기
    //기준:좋아요 과반 + 댓글 클럽원수*2, 만족하는거 없으면 비워두기
    public List<Post> findTrendingPostByClub(Club clubRequest, int memberSize) {

//        List<Book> books = queryFactory
//                .select(book)
//                .from(clubBookUser)
//                .join(clubBookUser.book, book)
//                .where(clubBookUser.club.eq(clubRequest),
//                        clubBookUser.book.isNotNull(),
//                        clubBookUser.user.isNotNull())
//                .fetch();

        SaveTime saveTime = new SaveTime();
        saveTime.setSearchEndDate( LocalDateTime.now());
        saveTime.setSearchStartDate(LocalDateTime.now().minusWeeks(2));

        log.info("searchStartDate={}",  saveTime.getSearchStartDate());
        log.info("searchEndDate={}", saveTime.getSearchEndDate());


//        List<Post> posts = queryFactory
//                .select(post)
//                .from(post)
//                .where(post.createdDate.between(SaveTime.searchStartDate, SaveTime.searchEndDate),
//                        post.type.eq(PostType.MEMO),
//                        post.book.in(books))
//                .fetch();

        List<Post> posts = queryFactory
                .select(post)
                .from(post)
                .where(post.createdDate.between(saveTime.getSearchStartDate(), saveTime.getSearchEndDate()),
                        post.book.in(JPAExpressions
                                .select(book)
                                .from(clubBookUser)
                                .join(clubBookUser.book, book)
                                .where(clubBookUser.club.eq(clubRequest),
                                        clubBookUser.book.isNotNull(),
                                        clubBookUser.user.isNotNull())))
                .fetch();

        log.info("posts={}",  posts);

        List<Tuple> likedList = queryFactory
                .select(liked.post, liked.count())
                .from(liked).distinct()
                .join(liked.post, post)
                .where(liked.post.in(posts))
                .groupBy(liked.post)
                .fetch();

        log.info("likedList={}",  likedList);

        List<Tuple> comments = queryFactory
                .select(comment.post, comment.count())
                .from(comment).distinct()
                .join(comment.post, post)
                .where(comment.post.in(posts))
                .groupBy(comment.post)
                .fetch();
        log.info("comments={}",  comments);



        double memberCeil = Math.ceil(memberSize / 2.0);

        List<Post> postFilteredFromLikedList = likedList.stream()
                .filter(m -> m.get(liked.count()) >= memberCeil)
                .map(m -> m.get(liked.post))
                .collect(Collectors.toList());
        log.info("postFilteredFromLikedList={}",  postFilteredFromLikedList);

        List<Post> postFilteredFromComments = comments.stream()
                .filter(m -> m.get(comment.count()) >= memberSize * 2L)
                .map(m -> m.get(comment.post))
                .collect(Collectors.toList());
        log.info("postFilteredFromComments={}",  postFilteredFromComments);

        List<Post> intersection = postFilteredFromLikedList.stream()
                .filter(postFilteredFromComments::contains)
                .collect(Collectors.toList());
        log.info("intersection={}",  intersection);

        if (intersection.size() > 3) {
            Collections.shuffle(intersection);
            List<Post> result = new ArrayList<>();
            result.add(intersection.get(0));
            result.add(intersection.get(1));
            result.add(intersection.get(2));
            log.info("result={}",  result);

            return result;
        }
//        log.info("intersection={}",  intersection);
//        log.info("intersectionGetId={}",  intersection.get(0).getId());

        return intersection;
    }

    @Data
    class SaveTime {
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        public LocalDateTime searchStartDate;

        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        public LocalDateTime searchEndDate;
    }
}
