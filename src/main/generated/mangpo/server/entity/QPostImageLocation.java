package mangpo.server.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPostImageLocation is a Querydsl query type for PostImageLocation
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QPostImageLocation extends EntityPathBase<PostImageLocation> {

    private static final long serialVersionUID = -748177554L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPostImageLocation postImageLocation = new QPostImageLocation("postImageLocation");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imgLocation = createString("imgLocation");

    public final QPost post;

    public QPostImageLocation(String variable) {
        this(PostImageLocation.class, forVariable(variable), INITS);
    }

    public QPostImageLocation(Path<? extends PostImageLocation> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPostImageLocation(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPostImageLocation(PathMetadata metadata, PathInits inits) {
        this(PostImageLocation.class, metadata, inits);
    }

    public QPostImageLocation(Class<? extends PostImageLocation> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.post = inits.isInitialized("post") ? new QPost(forProperty("post"), inits.get("post")) : null;
    }

}

