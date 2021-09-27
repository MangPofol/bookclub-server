package mangpo.server.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QClub is a Querydsl query type for Club
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QClub extends EntityPathBase<Club> {

    private static final long serialVersionUID = -1442757128L;

    public static final QClub club = new QClub("club");

    public final mangpo.server.entity.common.QBaseTimeEntity _super = new mangpo.server.entity.common.QBaseTimeEntity(this);

    public final EnumPath<ColorSet> colorSet = createEnum("colorSet", ColorSet.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final StringPath description = createString("description");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> level = createNumber("level", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final StringPath name = createString("name");

    public final NumberPath<Long> presidentId = createNumber("presidentId", Long.class);

    public QClub(String variable) {
        super(Club.class, forVariable(variable));
    }

    public QClub(Path<? extends Club> path) {
        super(path.getType(), path.getMetadata());
    }

    public QClub(PathMetadata metadata) {
        super(Club.class, metadata);
    }

}

