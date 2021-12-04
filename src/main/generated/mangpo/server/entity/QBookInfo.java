package mangpo.server.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QBookInfo is a Querydsl query type for BookInfo
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QBookInfo extends EntityPathBase<BookInfo> {

    private static final long serialVersionUID = -927750279L;

    public static final QBookInfo bookInfo = new QBookInfo("bookInfo");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath isbn = createString("isbn");

    public final StringPath name = createString("name");

    public QBookInfo(String variable) {
        super(BookInfo.class, forVariable(variable));
    }

    public QBookInfo(Path<? extends BookInfo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBookInfo(PathMetadata metadata) {
        super(BookInfo.class, metadata);
    }

}

