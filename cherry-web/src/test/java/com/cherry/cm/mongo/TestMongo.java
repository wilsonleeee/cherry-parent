package com.cherry.cm.mongo;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.cherry.CherryJunitBase;
import com.cherry.cm.mongo.domain.MemRuleExecRecord;
import com.cherry.cm.mongo.domain.QMemRuleExecRecord;
import com.cherry.cm.mongo.repositories.MemRuleExecRecordRepository;
import com.mysema.query.BooleanBuilder;

public class TestMongo extends CherryJunitBase {
	
	@Before
    public void setUp() throws Exception {
        
    }
    
    @After
    public void tearDown() throws Exception {
        
    }

	@Test
	public void test() {
		
		MemRuleExecRecordRepository memRuleExecRecordRepository = applicationContext.getBean(MemRuleExecRecordRepository.class);
		MemRuleExecRecord memRuleExecRecord = new MemRuleExecRecord();
		// 组织代码 
		memRuleExecRecord.setOrgCode("MGPORG");
		// 品牌代码
		memRuleExecRecord.setBrandCode("mgp");
		// 会员ID
		memRuleExecRecord.setMemberInfoId(0);
		// 会员卡号
		memRuleExecRecord.setMemCode("test001");
		// 履历区分
		memRuleExecRecord.setRecordKbn(0);
		// 业务日期
		memRuleExecRecord.setTicketDate("2012-01-01 10:10:10.000");
		MemRuleExecRecord saveResult = memRuleExecRecordRepository.save(memRuleExecRecord);
		
		QMemRuleExecRecord qMemRuleExecRecord = QMemRuleExecRecord.memRuleExecRecord;
		BooleanBuilder booleanBuilder = new BooleanBuilder();
		booleanBuilder.and(qMemRuleExecRecord.orgCode.eq("MGPORG"));
		booleanBuilder.and(qMemRuleExecRecord.brandCode.eq("mgp"));
		booleanBuilder.and(qMemRuleExecRecord.memberInfoId.eq(0));
		
		Page<MemRuleExecRecord> result = memRuleExecRecordRepository.findAll(booleanBuilder, new PageRequest(0, 10));
		if(result != null) {
			Assert.assertEquals(true, result.getTotalElements() == 1);
			Assert.assertEquals(true, "test001".equals(result.getContent().get(0).getMemCode()));
		} else {
			Assert.fail();
		}
		memRuleExecRecordRepository.delete(saveResult);
	}

}
