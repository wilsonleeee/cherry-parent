/*	
 * @(#)BINOLCPCOM04_IF.java     1.0 2011/7/18		
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
package com.cherry.cp.common.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cp.common.dto.RuleTestDTO;

/**
 * 规则测试共通 IF
 * 
 * @author hub
 * @version 1.0 2011.7.18
 */
public interface BINOLCPCOM04_IF {
	
	/**
	 * 取得对应的模板List
	 * 
	 * @param map
	 * @return 模板List
	 * @throws Exception 
	 */
	public List<Map<String, Object>> getTemplateList(Map<String, Object> map) throws Exception;
	
	/**
	 * 测试规则
	 * 
	 * @param map
	 * @return
	 * @throws Exception 
	 */
	public RuleTestDTO executeRule(Map<String, Object> map) throws Exception;

}
