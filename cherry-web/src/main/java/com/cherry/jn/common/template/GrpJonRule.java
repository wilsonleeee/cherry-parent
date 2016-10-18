/*	
 * @(#)GrpJonRule.java     1.0 2011/11/02		
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

import java.util.HashMap;
import java.util.Map;

import com.cherry.cp.common.dto.RuleBodyDTO;
import com.cherry.cp.common.util.CreateRule;

/**
 * 活动组入会规则处理
 * 
 * @author hub
 * @version 1.0 2011.11.02
 */
public class GrpJonRule extends CreateRule{
	
	/**
	 * 入会规则策略
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
	public RuleBodyDTO G00000000038_Rule(Map<String, Object> paramMap, Map<String, Object> tempMap) throws Exception {
		// 扩展参数
		Map<String, Object> extParams = new HashMap<String, Object>();
		// 会员活动类型
		extParams.put("CAMPAIGN_TYPE", paramMap.get("campaignType"));
		RuleBodyDTO ruleBody = new RuleBodyDTO();
		ruleBody.setExtParams(extParams);
		return ruleBody;
	}
}
