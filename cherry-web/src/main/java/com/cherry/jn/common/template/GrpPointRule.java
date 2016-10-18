/*	
 * @(#)GrpPointRule.java     1.0 2011/11/02		
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
package com.cherry.jn.common.template;

import java.util.List;
import java.util.Map;

import com.cherry.cp.common.dto.RuleBodyDTO;
import com.cherry.cp.common.util.CreateRule;

/**
 * 活动组积分规则处理
 * 
 * @author hub
 * @version 1.0 2011.11.02
 */
public class GrpPointRule extends CreateRule{
	
	/**
	 * 积分优先级规则策略
	 * 
	 * @param paramMap
	 *            参数集合
	 * @param tempMap
	 *            模板Map
	 * @param ruleBodyList
	 *            规则内容List
	 * @throws Exception 
	 * 
	 */
	public void G00000000038_Rule(Map<String, Object> paramMap, Map<String, Object> tempMap, List<RuleBodyDTO> ruleBodyList) throws Exception {
		RuleBodyDTO ruleBody = new RuleBodyDTO();
		ruleBody.setRuleSalience(-100);
		ruleBody.setGroupCode((String) tempMap.get("groupCode"));
		ruleBodyList.add(ruleBody);
	}

}
