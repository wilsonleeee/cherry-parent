/*	
 * @(#)RuleHandler_IF.java     1.0 2011/8/18		
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

import java.util.Map;

/**
 * 执行规则文件 IF
 * 
 * @author hub
 * @version 1.0 2011.8.18
 */
public interface RuleHandler_IF {
	
	/**
	 * 执行规则文件
	 * 
	 * @param Map
	 *            参数集合
	 * @throws Exception 
	 * 
	 */
	public void executeRule (Map<String, Object> map) throws Exception;
}
