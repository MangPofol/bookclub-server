package mangpo.server.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QLiked is a Querydsl query type for Liked
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QLiked extends EntityPathBase<Liked> {

    private static final long serialVersionUID = -1767585109L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QLiked liked = new QLiked("liked");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isLiked = createBoolean("isLiked");

    public final QPost post;

    public final QUser user;

    public QLiked(String variable) {
        this(Liked.class, forVariable(variable), INITS);
    }

    public QLiked(Path<? extends Liked> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QLiked(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QLiked(PathMetadata metadata, PathInits inits) {
        this(Liked.class, metadata, inits);
    }

    public QLiked(Class<? extends Liked> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.post = inits.isInitialized("post") ? new QPost(forProperty("post"), inits.get("post")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

