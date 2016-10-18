/*	
 * @(#)AnalyzeRuleMessage_IF.java     1.0 2011/8/18		
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
package com.cherry.mq.mes.interfaces;

import java.util.Map;

/**
 * 处理规则消息 IF
 * 
 * @author hub
 * @version 1.0 2011.8.18
 */
public interface AnalyzeRuleMessage_IF {
	
	/**
	 * 处理规则消息
	 * 
	 * @param Map
	 *            参数集合
	 * @throws Exception 
	 * 
	 */
	public void analyzeRuleData (Map<String, Object> map) throws Exception;
}
