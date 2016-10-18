/*	
 * @(#)BINBEDRPOI04_BL.java     1.0 2013/05/13	
 * 		
 * Copyright (c) 2010 SHANGHAI BINGKUN DIGITAL TECHNOLOGY CO.,LTD		
 * All rights reserved		
 * 		
 * This software is the confidential and proprietary information of 		
 * SHANGHAI BINGKUN.("Confidential Information").  You shall not		
 * disclose such Confidential Information and shall use it only in		
 * accordance with the terms of the license agreement you entered into		
 * with SHANGHAI BINGKUN.		
 */
package com.cherry.dr.point.bl;

import java.util.List;

import javax.annotation.Resource;

import com.cherry.dr.cmbussiness.dto.core.CampBaseDTO;
import com.cherry.dr.cmbussiness.interfaces.CampRuleBatchExec_IF;
import com.cherry.dr.cmbussiness.interfaces.RuleEngine_IF;
import com.cherry.dr.cmbussiness.util.DroolsConstants;

/**
 * 积分清零规则执行 BL
 * 
 * @author hub
 * @version 1.0 2013.05.13
 */
public class BINBEDRPOI04_BL implements CampRuleBatchExec_IF{
	
	@Resource
	private RuleEngine_IF ruleEngineIF;

	@Override
	public void ruleExec(List<CampBaseDTO> campBaseList) throws Exception {
		// 规则执行之前清空规则匹配记录
		for (CampBaseDTO campBaseDTO : campBaseList) {
			campBaseDTO.initFact(DroolsConstants.CAMPAIGN_TYPE8888);
		}
		// 会员活动batch处理(多个实体)
		ruleEngineIF.executeRuleMulti(campBaseList, null);
	}

	@Override
	public void beforExec(List<CampBaseDTO> campBaseList) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterExec(List<CampBaseDTO> campBaseList) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
