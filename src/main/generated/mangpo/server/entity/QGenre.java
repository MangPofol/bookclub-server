package mangpo.server.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QGenre is a Querydsl query type for Genre
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QGenre extends EntityPathBase<Genre> {

    private static final long serialVersionUID = -1772318591L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QGenre genre = new QGenre("genre");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath type = createString("type");

    public final QUser user;

    public QGenre(String variable) {
        this(Genre.class, forVariable(variable), INITS);
    }

    public QGenre(Path<? extends Genre> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QGenre(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QGenre(PathMetadata metadata, PathInits inits) {
        this(Genre.class, metadata, inits);
    }

    public QGenre(Class<? extends Genre> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

