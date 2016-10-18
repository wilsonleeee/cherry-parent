/*	
 * @(#)TemplateRule_IF.java     1.0 2011/4/18		
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

import com.cherry.cm.core.ICherryInterface;

/**
 * 模板规则 IF
 * 
 * @author hub
 * @version 1.0 2011.4.18
 */
public interface TemplateRule_IF extends ICherryInterface {

	/**
	 * 将页面提交的模板内容转换成规则
	 * 
	 * 
	 * @param List
	 *            模板List
	 * @param List
	 *            关系List
	 * @return 无
	 * @throws Exception 
	 */
	public String convertTemplateToRule(List<Map<String, Object>> camTempList,
			Map<String, Object> relationInfo, Map<String, Object> ruleInfo);
}
