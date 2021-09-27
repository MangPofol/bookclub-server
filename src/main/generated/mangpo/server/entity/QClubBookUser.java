package mangpo.server.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QClubBookUser is a Querydsl query type for ClubBookUser
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QClubBookUser extends EntityPathBase<ClubBookUser> {

    private static final long serialVersionUID = -1467010964L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QClubBookUser clubBookUser = new QClubBookUser("clubBookUser");

    public final mangpo.server.entity.common.QBaseTimeEntity _super = new mangpo.server.entity.common.QBaseTimeEntity(this);

    public final QBook book;

    public final QClub club;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final QUser user;

    public QClubBookUser(String variable) {
        this(ClubBookUser.class, forVariable(variable), INITS);
    }

    public QClubBookUser(Path<? extends ClubBookUser> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QClubBookUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QClubBookUser(PathMetadata metadata, PathInits inits) {
        this(ClubBookUser.class, metadata, inits);
    }

    public QClubBookUser(Class<? extends ClubBookUser> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.book = inits.isInitialized("book") ? new QBook(forProperty("book")) : null;
        this.club = inits.isInitialized("club") ? new QClub(forProperty("club")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

