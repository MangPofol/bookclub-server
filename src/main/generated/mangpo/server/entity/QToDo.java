package mangpo.server.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QToDo is a Querydsl query type for ToDo
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QToDo extends EntityPathBase<ToDo> {

    private static final long serialVersionUID = -1442249304L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QToDo toDo = new QToDo("toDo");

    public final StringPath content = createString("content");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QUser user;

    public QToDo(String variable) {
        this(ToDo.class, forVariable(variable), INITS);
    }

    public QToDo(Path<? extends ToDo> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QToDo(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QToDo(PathMetadata metadata, PathInits inits) {
        this(ToDo.class, metadata, inits);
    }

    public QToDo(Class<? extends ToDo> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

