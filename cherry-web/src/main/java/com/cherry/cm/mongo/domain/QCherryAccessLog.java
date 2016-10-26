package com.cherry.cm.mongo.domain;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.EntityPathBase;
import com.mysema.query.types.path.PathInits;
import com.mysema.query.types.path.StringPath;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/**
 * QCherryAccessLog is a Querydsl query type for CherryAccessLog
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QCherryAccessLog extends EntityPathBase<CherryAccessLog> {

    private static final long serialVersionUID = 799520339;

    private static final PathInits INITS = PathInits.DIRECT;

    public static final QCherryAccessLog cherryAccessLog = new QCherryAccessLog("cherryAccessLog");

    public final StringPath comment = createString("comment");

    public final StringPath content = createString("content");

    public final QObjectId id;

    public final StringPath ip = createString("ip");

    public final StringPath level = createString("level");

    public final StringPath logid = createString("logid");

    public final StringPath time = createString("time");

    public final StringPath user = createString("user");

    public final StringPath userAgent = createString("userAgent");

    public QCherryAccessLog(String variable) {
        this(CherryAccessLog.class, forVariable(variable), INITS);
    }

    public QCherryAccessLog(Path<? extends CherryAccessLog> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QCherryAccessLog(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QCherryAccessLog(PathMetadata<?> metadata, PathInits inits) {
        this(CherryAccessLog.class, metadata, inits);
    }

    public QCherryAccessLog(Class<? extends CherryAccessLog> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.id = inits.isInitialized("id") ? new QObjectId(forProperty("id")) : null;
    }

}

