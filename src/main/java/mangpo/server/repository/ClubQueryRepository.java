package mangpo.server.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import mangpo.server.entity.Club;
import mangpo.server.entity.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static mangpo.server.entity.QClubBookUser.clubBookUser;

@Repository
public class ClubQueryRepository {


    private final JPAQueryFactory queryFactory;

    public ClubQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<Club> findDistinctClub(User userRequest){
        return queryFactory
                .select(clubBookUser.club).distinct()
                .from(clubBookUser)
                .where(clubBookUser.user.eq(userRequest))
                .fetch();

    }

}
