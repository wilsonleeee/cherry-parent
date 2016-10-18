/*
 * @(#)BINOLSTBIL09_Service.java     1.0 2012/12/26
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
package com.cherry.ss.prm.service;

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
 * @version 1.0 2011.12.26
 */
@SuppressWarnings("unchecked")
public class BINOLSSPRM45_Service extends BaseService {

	/**
	 * 取得盘点单总数
	 * 
	 * @param map
	 * @return
	 */
	public int getTakingCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM45.getTakingCount");
		return baseServiceImpl.getSum(map);
	}

	/**
	 * 取得盘点单List
	 * 
	 * @param map
	 * @return
	 */
	public List getTakingList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM45.getTakingList");
		return baseServiceImpl.getList(map);
	}

	/**
	 * 汇总信息
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getSumInfo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM45.getSumInfo");
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
				"BINOLSSPRM45.getTakingInfoListExcel");
		return baseServiceImpl.getList(map);
	}

	/**
	 * 获取相关联的促销品
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getConjunctionPrmList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM45.getConjunctionPrmList");
		return baseServiceImpl.getList(map);
	}
}
