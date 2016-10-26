package com.cherry.cm.mongo.domain;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QMemRuleExecRecord is a Querydsl query type for MemRuleExecRecord
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QMemRuleExecRecord extends EntityPathBase<MemRuleExecRecord> {

    private static final long serialVersionUID = 1425582911;

    public static final QMemRuleExecRecord memRuleExecRecord = new QMemRuleExecRecord("memRuleExecRecord");

    public final StringPath baCode = createString("baCode");

    public final StringPath billCode = createString("billCode");

    public final StringPath billType = createString("billType");

    public final StringPath brandCode = createString("brandCode");

    public final NumberPath<Integer> brandInfoId = createNumber("brandInfoId", Integer.class);

    public final StringPath calcDate = createString("calcDate");

    public final StringPath changeType = createString("changeType");

    public final StringPath channel = createString("channel");

    public final StringPath countercode = createString("countercode");

    public final StringPath createdBy = createString("createdBy");

    public final StringPath createPGM = createString("createPGM");

    public final StringPath createTime = createString("createTime");

    public final StringPath employeeId = createString("employeeId");

    public final StringPath id = createString("id");

    public final NumberPath<Integer> memberInfoId = createNumber("memberInfoId", Integer.class);

    public final StringPath memCode = createString("memCode");

    public final NumberPath<Integer> modifyCount = createNumber("modifyCount", Integer.class);

    public final StringPath newValue = createString("newValue");

    public final StringPath oldValue = createString("oldValue");

    public final StringPath organizationId = createString("organizationId");

    public final NumberPath<Integer> organizationInfoId = createNumber("organizationInfoId", Integer.class);

    public final StringPath orgCode = createString("orgCode");

    public final NumberPath<Integer> reason = createNumber("reason", Integer.class);

    public final NumberPath<Integer> reCalcCount = createNumber("reCalcCount", Integer.class);

    public final NumberPath<Integer> recordKbn = createNumber("recordKbn", Integer.class);

    public final StringPath ruleIds = createString("ruleIds");

    public final StringPath subCampaignCodes = createString("subCampaignCodes");

    public final StringPath ticketDate = createString("ticketDate");

    public final StringPath updatedBy = createString("updatedBy");

    public final StringPath updatePGM = createString("updatePGM");

    public final StringPath updateTime = createString("updateTime");

    public final StringPath validFlag = createString("validFlag");

    public QMemRuleExecRecord(String variable) {
        super(MemRuleExecRecord.class, forVariable(variable));
    }

    public QMemRuleExecRecord(Path<? extends MemRuleExecRecord> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMemRuleExecRecord(PathMetadata<?> metadata) {
        super(MemRuleExecRecord.class, metadata);
    }

}

