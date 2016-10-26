package com.cherry.cm.mongo.domain;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.EntityPathBase;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.PathInits;
import com.mysema.query.types.path.StringPath;

import javax.annotation.Generated;

import static com.mysema.query.types.PathMetadataFactory.forVariable;


/**
 * QMachineConnRecord is a Querydsl query type for MachineConnRecord
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QMachineConnRecord extends EntityPathBase<MachineConnRecord> {

    private static final long serialVersionUID = 1682027504;

    private static final PathInits INITS = PathInits.DIRECT;

    public static final QMachineConnRecord machineConnRecord = new QMachineConnRecord("machineConnRecord");

    public final StringPath brandCode = createString("brandCode");

    public final StringPath firstConnectTime = createString("firstConnectTime");

    public final QObjectId id;

    public final StringPath lastConnectTime = createString("lastConnectTime");

    public final StringPath machineCode = createString("machineCode");

    public final NumberPath<Integer> machineInfoID = createNumber("machineInfoID", Integer.class);

    public final StringPath orgCode = createString("orgCode");

    public final StringPath recordDate = createString("recordDate");

    public QMachineConnRecord(String variable) {
        this(MachineConnRecord.class, forVariable(variable), INITS);
    }

    public QMachineConnRecord(Path<? extends MachineConnRecord> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QMachineConnRecord(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QMachineConnRecord(PathMetadata<?> metadata, PathInits inits) {
        this(MachineConnRecord.class, metadata, inits);
    }

    public QMachineConnRecord(Class<? extends MachineConnRecord> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.id = inits.isInitialized("id") ? new QObjectId(forProperty("id")) : null;
    }

}

