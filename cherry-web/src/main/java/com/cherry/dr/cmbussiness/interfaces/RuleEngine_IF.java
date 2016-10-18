/*	
 * @(#)RuleEngine_IF.java     1.0 2011/05/12	
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
package com.cherry.dr.cmbussiness.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.dr.cmbussiness.dto.core.CampBaseDTO;

/**
 * 会员活动规则共通IF
 * 
 * @author hub
 * @version 1.0 2011.05.12
 */
public interface RuleEngine_IF {
	
	/**
	 * 会员活动batch处理
	 * 
	 * @param CampBaseDTO
	 * 			会员活动基础 DTO
	 * @return campRuleIF
	 * 			会员活动规则共通IF
	 * 
	 * @throws Exception
	 * 
	 */
	public CampBaseDTO executeRuleBatch(CampBaseDTO campBaseDTO, CampRuleIF campRuleIF) throws Exception;
	
	/**
	 * 会员活动batch处理(多个实体)
	 * 
	 * @param CampBaseDTO
	 * 			会员活动基础 DTO
	 * @return campRuleIF
	 * 			会员活动规则共通IF
	 * 
	 * @throws Exception
	 * 
	 */
	public List<CampBaseDTO> executeRuleMulti(List<CampBaseDTO> campBaseList, CampRuleIF campRuleIF) throws Exception;
	
	/**
	 * 取得组规则库
	 * 
	 * @param orgCode
	 * 			组织代码
	 * @param brandCode
	 * 			品牌代码
	 * @param campaignType
	 * 			会员活动类型
	 * @return Map
	 * 			组规则库
	 * 
	 */
	public Map<String, Object> getGroupRule(String orgCode, String brandCode, String campaignType);
	
}
