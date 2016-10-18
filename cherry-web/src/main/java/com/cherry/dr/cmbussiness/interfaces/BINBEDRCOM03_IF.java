/*	
 * @(#)BINBEDRCOM03_IF.java     1.0 2012/02/27	
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
import com.cherry.dr.cmbussiness.dto.core.RuleFilterDTO;

/**
 * 规则执行前共通处理 IF
 * 
 * @author hub
 * @version 1.0 2012.02.27
 */
public interface BINBEDRCOM03_IF {
	
	/**
	 * 
	 * 执行条件验证
	 * 
	 * 
	 * @param c
	 *            验证对象
	 * @param allFilters
	 *            规则条件集合
	 * @param filterName
	 *            规则条件名称
	 * @param methodName
	 *            验证方法名称
	 * @return boolean 验证结果：true 满足条件, false 不满足条件
	 * 
	 */
	public boolean doCheck(Object c, List<RuleFilterDTO> allFilters, String filterName, String methodName) throws Exception;
	
	/**
	 * 
	 * 执行规则处理
	 * 
	 * 
	 * @param c
	 *            验证对象
	 * @param allFilters
	 *            规则条件集合
	 * @param filterName
	 *            规则条件名称
	 * @param methodName
	 *            验证方法名称
	 * @return 
	 * @throws Exception 
	 * 
	 */
	public Object doThen(Object oc, List<RuleFilterDTO> allFilters, String filterName, String methodName) throws Exception;
	
	/**
	 * 
	 * 优先级处理
	 * 
	 * 
	 * @param c
	 *            验证对象
	 * @param allFilters
	 *            规则条件集合
	 * @param priorityInfo
	 *            优先级信息
	 * @param campType
	 *          会员活动类型
	 * 
	 */
	public List<String> execPriority(CampBaseDTO c, List<RuleFilterDTO> allFilters, List<Map<String, Object>> priorityList, String campType) throws Exception;
}
