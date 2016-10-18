/*
 * @(#)BINOLBSDEP05_Service.java     1.0 2010/10/27
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

package com.cherry.bs.dep.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.cache.annotation.CacheEvict;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 停用启用部门Service
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
public class BINOLBSDEP05_Service extends BaseService {
	
	/**
	 * 停用启用部门
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 * 
	 */
	@CacheEvict(value="CherryAllDepartCache",allEntries=true,beforeInvocation=false)
	public int updateDepartInfo(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSDEP05.updateDepartInfo");
		return baseServiceImpl.update(parameterMap);
	}

}
