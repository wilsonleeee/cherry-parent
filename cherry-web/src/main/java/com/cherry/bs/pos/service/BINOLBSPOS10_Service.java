/*
 * @(#)BINOLBSPOS10_Service.java     1.0 2011/11/04
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
package com.cherry.bs.pos.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.cache.annotation.CacheEvict;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 启用停用岗位画面Service
 * 
 * @author WangCT
 * @version 1.0 2011/11/04
 */
public class BINOLBSPOS10_Service extends BaseService {
	
	/**
	 * 停用启用岗位
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	@CacheEvict(value="CherryEmpCache",allEntries=true,beforeInvocation=false)
	public int updatePosition(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSPOS10.updatePosition");
		return baseServiceImpl.update(parameterMap);
	}

}
