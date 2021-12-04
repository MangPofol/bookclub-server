package mangpo.server.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPostClubScope is a Querydsl query type for PostClubScope
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QPostClubScope extends EntityPathBase<PostClubScope> {

    private static final long serialVersionUID = 721096572L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPostClubScope postClubScope = new QPostClubScope("postClubScope");

    public final QClub club;

    public final StringPath clubName = createString("clubName");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QPost post;

    public QPostClubScope(String variable) {
        this(PostClubScope.class, forVariable(variable), INITS);
    }

    public QPostClubScope(Path<? extends PostClubScope> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPostClubScope(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPostClubScope(PathMetadata metadata, PathInits inits) {
        this(PostClubScope.class, metadata, inits);
    }

    public QPostClubScope(Class<? extends PostClubScope> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.club = inits.isInitialized("club") ? new QClub(forProperty("club")) : null;
        this.post = inits.isInitialized("post") ? new QPost(forProperty("post"), inits.get("post")) : null;
    }

}

