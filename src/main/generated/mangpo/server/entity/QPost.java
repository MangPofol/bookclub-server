package mangpo.server.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPost is a Querydsl query type for Post
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QPost extends EntityPathBase<Post> {

    private static final long serialVersionUID = -1442367006L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPost post = new QPost("post");

    public final mangpo.server.entity.common.QBaseTimeEntity _super = new mangpo.server.entity.common.QBaseTimeEntity(this);

    public final QBook book;

    public final ListPath<Comment, QComment> comments = this.<Comment, QComment>createList("comments", Comment.class, QComment.class, PathInits.DIRECT2);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final StringPath hyperlink = createString("hyperlink");

    public final StringPath hyperlinkTitle = createString("hyperlinkTitle");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isIncomplete = createBoolean("isIncomplete");

    public final ListPath<Liked, QLiked> likedList = this.<Liked, QLiked>createList("likedList", Liked.class, QLiked.class, PathInits.DIRECT2);

    public final StringPath location = createString("location");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final ListPath<PostImageLocation, QPostImageLocation> postImageLocations = this.<PostImageLocation, QPostImageLocation>createList("postImageLocations", PostImageLocation.class, QPostImageLocation.class, PathInits.DIRECT2);

    public final StringPath readTime = createString("readTime");

    public final EnumPath<PostScope> scope = createEnum("scope", PostScope.class);

    public final StringPath title = createString("title");

    public QPost(String variable) {
        this(Post.class, forVariable(variable), INITS);
    }

    public QPost(Path<? extends Post> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPost(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPost(PathMetadata metadata, PathInits inits) {
        this(Post.class, metadata, inits);
    }

    public QPost(Class<? extends Post> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.book = inits.isInitialized("book") ? new QBook(forProperty("book"), inits.get("book")) : null;
    }

}

