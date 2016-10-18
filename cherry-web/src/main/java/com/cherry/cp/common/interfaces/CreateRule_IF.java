/*	
 * @(#)CreateRule_IF.java     1.0 2011/7/18		
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

import java.util.Map;

import com.cherry.cp.common.dto.RuleBodyDTO;
import com.cherry.cp.common.dto.RuleConditionDTO;
import com.cherry.dr.cmbussiness.dto.core.CampBaseDTO;

/**
 * 创建规则处理 IF
 * 
 * @author hub
 * @version 1.0 2011.7.18
 */
public interface CreateRule_IF {
	
	/**
	 * 运行指定名称的方法
	 * 
	 * @param String
	 *            指定的方法名
	 * @param Map
	 *            参数集合
	 * @param Map
	 *            模板Map
	 * @throws Exception 
	 * 
	 */
	public RuleBodyDTO invokeMd(String mdName, Map<String, Object> paramMap, Map<String, Object> tempMap) throws Exception;
	
	/**
	 * 运行指定名称的方法
	 * 
	 * @param String
	 *            指定的方法名
	 * @param Map
	 *            参数集合
	 * @param Map
	 *            模板Map
	 * @throws Exception 
	 * 
	 */
	public void invokeTestMd(String mdName, Map<String, Object> paramMap, Map<String, Object> tempMap, CampBaseDTO campBaseDTO) throws Exception;
	
	/**
	 * 运行指定名称的方法(规则条件)
	 * 
	 * @param String
	 *            指定的方法名
	 * @param Map
	 *            参数集合
	 * @param Map
	 *            模板Map
	 * @throws Exception 
	 * 
	 */
	public RuleConditionDTO invokeMdCond(String mdName, Map<String, Object> paramMap, Map<String, Object> tempMap) throws Exception;
}
