/*	
 * @(#)BINOLPTRPS09_Service.java     1.0 2011/03/09		
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 出入库记录查询service
 * 
 * @author lipc
 * @version 1.0 2011.03.09
 *
 */
@SuppressWarnings("unchecked")
public class BINOLPTRPS09_Service extends BaseService{
	
	/**
	 * 出入库记录总数
	 * 
	 * @param map
	 * @return
	 */
	public int getProInOutCount(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS09.getProInOutCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 出入库记录LIST
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getProInOutList(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS09.getProInOutList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 出入库记录汇总信息
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getSumInfo(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS09.getSumInfo");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	
	/**
	 * 出入库详细导出信息
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getExportDetailList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS09.getExportDetailList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 获取产品入出库记录明细总数
	 * @param map
	 * @return int
	 * 
	 * */
	public int getExportDetailCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS09.getExportDetailCount");
		return baseServiceImpl.getSum(paramMap);
	}
}
