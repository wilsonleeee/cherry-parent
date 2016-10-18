/*	
 * @(#)BINBEDRJON04_BL.java     1.0 2011/08/25	
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
package com.cherry.dr.jon.bl;

import javax.annotation.Resource;

import com.cherry.dr.cmbussiness.dto.core.CampBaseDTO;
import com.cherry.dr.cmbussiness.interfaces.BINBEDRCOM01_IF;
import com.cherry.dr.cmbussiness.interfaces.CampRuleExec_IF;
import com.cherry.dr.cmbussiness.interfaces.CampRuleIF;
import com.cherry.dr.cmbussiness.interfaces.RuleEngine_IF;
import com.cherry.dr.cmbussiness.util.DroolsConstants;

/**
 * 累计金额清零规则执行 BL
 * 
 * @author hub
 * @version 1.0 2011.08.25
 */
public class BINBEDRJON04_BL implements CampRuleExec_IF{
	
	@Resource
	private RuleEngine_IF ruleEngineIF;

	@Resource
	private CampRuleIF binbedrjon04Rule;

	@Resource
	private BINBEDRCOM01_IF binbedrcom01BL;
	
	/**
	 * 规则执行后处理
	 * 
	 * @param map
	 *            参数集合
	 * 
	 * @throws Exception
	 * 
	 */
	@Override
	public void afterExec(CampBaseDTO campBaseDTO) throws Exception {
		// 插入规则执行履历表:累计金额
		binbedrcom01BL.addRuleExecRecord(campBaseDTO, DroolsConstants.RECORDKBN_1, campBaseDTO.isMatchRule());
		// 插入规则执行履历表:可兑换金额(化妆次数用)
		binbedrcom01BL.addRuleExecRecord(campBaseDTO, DroolsConstants.RECORDKBN_4, campBaseDTO.isMatchRule());
		// 规则匹配成功
		if (campBaseDTO.isMatchRule()) {		
			// 所有规则
			campBaseDTO.addAllRules(campBaseDTO.getRuleIds(), DroolsConstants.RECORDKBN_0);
			campBaseDTO.emptyRuleIds();
		}
	}
	
	/**
	 * 规则执行前处理
	 * 
	 * @param map
	 *            参数集合
	 * 
	 * @throws Exception
	 * 
	 */
	@Override
	public void beforExec(CampBaseDTO campBaseDTO) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 累计金额清零规则执行
	 * 
	 * @param map
	 *            参数集合
	 * 
	 * @throws Exception
	 * 
	 */
	@Override
	public void ruleExec(CampBaseDTO campBaseDTO) throws Exception {
		campBaseDTO.emptySubCampCodes();
		campBaseDTO.emptyRuleIds();
		// 会员活动规则batch处理
		ruleEngineIF.executeRuleBatch(campBaseDTO, binbedrjon04Rule);
		// 规则执行后处理
		afterExec(campBaseDTO);
	}

}
