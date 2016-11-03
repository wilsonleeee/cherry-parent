/*
 * @(#)BINBETLBAT03_Service.java     1.0 2011/07/13
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
package com.cherry.tl.bat.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 更新业务日期service
 * 
 * 
 * @author WangCT
 * @version 1.0 2011/07/13
 */
public class BINBETLBAT03_Service extends BaseService {
	
	/**
	 * 
	 * 更新业务日期
	 * 
	 * @param map 更新条件
	 * @return 无
	 * 
	 */	
	public void updateBussinessDate(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBETLBAT03.updateBussinessDate");
		baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 
	 * 取得业务日期
	 * 
	 * @param map 查询条件
	 * @return 业务日期
	 * 
	 */	
	public String getBussinessDate(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBETLBAT03.getBussinessDate");
		return (String)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 
	 * 取得当前更新次数 
	 * 
	 * @param map 查询条件
	 * @return 更新次数
	 * 
	 */	
	public String getModifyCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBETLBAT03.getModifyCount");
		return (String)baseServiceImpl.get(paramMap);
	}

	/**
	 * 
	 * 插入新数据
	 * 
	 * @param map 更新条件
	 * @return 无
	 * 
	 */	
	public void insertBussinessDateInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBETLBAT03.insertBussinessDateInfo");
		baseServiceImpl.update(paramMap);
	}
}
