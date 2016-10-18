/*
 * @(#)BINOLSTBIL09_Service.java     1.0 2012/12/04
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
 * 
 * 盘点查询Service
 * 
 * 
 * 
 * @author liuminghao
 * @version 1.0 2011.12.04
 */
@SuppressWarnings("unchecked")
public class BINOLPTRPS16_Service extends BaseService {

	/**
	 * 取得盘点单总数
	 * 
	 * @param map
	 * @return
	 */
	public int getTakingCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS16.getTakingCount");
		return baseServiceImpl.getSum(map);
	}

	/**
	 * 取得盘点单List
	 * 
	 * @param map
	 * @return
	 */
	public List getTakingList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS16.getTakingList");
		return baseServiceImpl.getList(map);
	}

	/**
	 * 汇总信息
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getSumInfo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS16.getSumInfo");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}

	/**
	 * 取得盘点信息(Excel导出)
	 * 
	 * @param map
	 * @return
	 */
	public List getTakingInfoListExcel(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLPTRPS16.getTakingInfoListExcel");
		return baseServiceImpl.getList(map);
	}

	/**
	 * 获取相关联的产品厂商ID
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getConjunctionProList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS16.getConjunctionProList");
		return baseServiceImpl.getList(map);
	}
}
