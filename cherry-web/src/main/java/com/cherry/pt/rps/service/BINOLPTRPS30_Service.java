/*
 * @(#)BINOLPTRPS30_Service.java     1.0 2014/06/27
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
package com.cherry.pt.rps.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * SPA报表Service
 * @author zhangle
 * @version 1.0 2014.06.27
 */
public class BINOLPTRPS30_Service extends BaseService {
	
	/**
	 * 查询BB柜台销售统计总记录数
	 * @param map
	 * @return
	 */
	public int getSPARptCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS30.getSPARptCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 查询BB柜台销售统计List
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getSPARptList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS30.getSPARptList");
		return baseServiceImpl.getList(map);
	}

}
