/*
 * @(#)BINOLCTTPL01_Action.java     1.0 2012/12/11
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
package com.cherry.ct.common.service;

import java.util.HashMap;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 沟通计划管理Service
 * 
 * @author ZhangGS
 * @version 1.0 2012.12.11
 */
public class BINOLCTCOM01_Service extends BaseService{
	/**
	 * 根据活动编号取得沟通计划编号
	 * @param map
	 * @return 沟通计划编号
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getPlanInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCTCOM01.getPlanInfo");
		return (Map<String, Object>) baseServiceImpl.get(paramMap);
	}
}
