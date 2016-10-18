/*	
 * @(#)BINOLBSCNT05_Service.java     1.0 2011/05/09		
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

package com.cherry.bs.cnt.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.cache.annotation.CacheEvict;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 	停用启用柜台处理Service
 * 
 * @author WangCT
 * @version 1.0 2011.05.009
 */
public class BINOLBSCNT05_Service extends BaseService {
	
	/**
	 * 停用启用柜台
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 * 
	 */
	@CacheEvict(value={"CherryDepartCache","CherryAllDepartCache"},allEntries=true,beforeInvocation=false)
	public int updateCounterInfo(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCNT05.updateCounterInfo");
		return baseServiceImpl.update(parameterMap);
	}
	
	/**
	 * 停用启用部门表中的柜台
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 * 
	 */
	@CacheEvict(value={"CherryDepartCache","CherryAllDepartCache"},allEntries=true,beforeInvocation=false)
	public int updateDepartInfo(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCNT05.updateDepartInfo");
		return baseServiceImpl.update(parameterMap);
	}
	
	/**
	 * 柜台停用/启用后更新产品方案明细表的version字段
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 * 
	 */
	public int updatePrtSoluDepartRelation(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCNT05.updatePrtSoluDepartRelation");
		return baseServiceImpl.update(parameterMap);
	}

}
