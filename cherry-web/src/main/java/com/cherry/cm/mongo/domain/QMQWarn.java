package com.cherry.cm.mongo.domain;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.EntityPathBase;
import com.mysema.query.types.path.PathInits;
import com.mysema.query.types.path.StringPath;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/**
 * QMQWarn is a Querydsl query type for MQWarn
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QMQWarn extends EntityPathBase<MQWarn> {

    private static final long serialVersionUID = 54197246;

    private static final PathInits INITS = PathInits.DIRECT;

    public static final QMQWarn mQWarn = new QMQWarn("mQWarn");

    public final StringPath brandCode = createString("brandCode");

    public final StringPath errInfo = createString("errInfo");

    public final StringPath errType = createString("errType");

    public final QObjectId id;

    public final StringPath messageBody = createString("messageBody");

    public final StringPath orgCode = createString("orgCode");

    public final StringPath putTime = createString("putTime");

    public final StringPath tradeEntityCode = createString("tradeEntityCode");

    public final StringPath tradeNoIF = createString("tradeNoIF");

    public final StringPath tradeType = createString("tradeType");

    public QMQWarn(String variable) {
        this(MQWarn.class, forVariable(variable), INITS);
    }

    public QMQWarn(Path<? extends MQWarn> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QMQWarn(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QMQWarn(PathMetadata<?> metadata, PathInits inits) {
        this(MQWarn.class, metadata, inits);
    }

    public QMQWarn(Class<? extends MQWarn> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.id = inits.isInitialized("id") ? new QObjectId(forProperty("id")) : null;
    }

}

