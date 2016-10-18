/*
 * @(#)BasAttendanceService.java     1.0 2011/11/11
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
package com.cherry.shindig.gadgets.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 考勤信息取得Service
 * 
 * @author WangCT
 * @version 1.0 2011/11/11
 */
public class BasAttendanceService extends BaseService {
	
	/**
	 * 查询考勤信息List
	 * 
	 * @param map 查询条件
	 * @return 考勤信息List 
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getAttendanceList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BasAttendance.getAttendanceList");
		return baseServiceImpl.getList(parameterMap);
	}

}
