/*
 * @(#)BINOLBSPOS08_Service.java     1.0 2010/10/27
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
 * 
 * 更新岗位类别画面Service
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
public class BINOLBSPOS08_Service extends BaseService {
	
	/**
	 * 
	 * 更新岗位类别
	 * 
	 * @param map 更新条件
	 * @return 处理件数
	 */
	@CacheEvict(value="CherryEmpCache",allEntries=true,beforeInvocation=false)
	public int updatePosCategory(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSPOS08.updatePosCategory");
		return baseServiceImpl.update(parameterMap);
	}
	
	/**
	 * 验证同一组织中是否存在同样的岗位名称
	 * 
	 * @param map 检索条件
	 * @return 返回岗位类别ID
	 */
	public String getPosCategoryNameCheck(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSPOS08.getPosCategoryNameCheck");
		return (String)baseServiceImpl.get(parameterMap);
	}

}
