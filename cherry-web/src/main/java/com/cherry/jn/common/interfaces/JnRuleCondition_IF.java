/*	
 * @(#)JnRuleCondition_IF.java     1.0 2011/05/16		
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
package com.cherry.jn.common.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cp.common.dto.CampRuleConditionDTO;

/**
 * 会员入会规则条件明细
 * 
 * @author hub
 * @version 1.0 2011.05.16
 */
public interface JnRuleCondition_IF {
	
	/**
	 * 创建会员入会规则条件明细List
	 * 
	 * 
	 * @param List
	 *            模板List
	 * @return 会员入会规则条件明细List
	 *  
	 */
	public List<CampRuleConditionDTO> createJnRuleConditionList (List<Map<String, Object>> camTempList);
}
