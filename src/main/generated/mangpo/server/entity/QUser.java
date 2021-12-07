package mangpo.server.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -1442214643L;

    public static final QUser user = new QUser("user");

    public final mangpo.server.entity.common.QBaseTimeEntity _super = new mangpo.server.entity.common.QBaseTimeEntity(this);

    public final DateTimePath<java.time.LocalDateTime> birthdate = createDateTime("birthdate", java.time.LocalDateTime.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final StringPath email = createString("email");

    public final ListPath<Genre, QGenre> genres = this.<Genre, QGenre>createList("genres", Genre.class, QGenre.class, PathInits.DIRECT2);

    public final StringPath goal = createString("goal");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath introduce = createString("introduce");

    public final BooleanPath isDormant = createBoolean("isDormant");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final StringPath profileImgLocation = createString("profileImgLocation");

    public final EnumPath<Sex> sex = createEnum("sex", Sex.class);

    public final StringPath style = createString("style");

    public final ListPath<ToDo, QToDo> todos = this.<ToDo, QToDo>createList("todos", ToDo.class, QToDo.class, PathInits.DIRECT2);

    public final ListPath<UserAuthority, QUserAuthority> userAuthorityList = this.<UserAuthority, QUserAuthority>createList("userAuthorityList", UserAuthority.class, QUserAuthority.class, PathInits.DIRECT2);

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

