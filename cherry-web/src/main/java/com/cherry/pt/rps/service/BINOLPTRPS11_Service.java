/*	
 * @(#)BINOLPTRPS11_Service.java     1.0 2011/03/15		
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
 * 库存记录查询service
 * 
 * @author lipc
 * @version 1.0 2011.03.15
 * 
 */
@SuppressWarnings("unchecked")
public class BINOLPTRPS11_Service extends BaseService {

	/**
	 * 取得库存记录总数
	 * 
	 * @param map
	 * @return
	 */
	public int getProStockCount(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS11.getProStockCount");
		return baseServiceImpl.getSum(map);
	}

	/**
	 * 取得库存记录LIST
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getProStockList(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS11.getProStockList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得库存记录一览概要导出数据
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getProStockSummaryInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS11.getProStockSummaryInfo");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 取得汇总信息
	 * 
	 * */
	public Map<String,Object> getSumInfo(Map<String,Object> map){
		return (Map<String,Object>)baseServiceImpl.get(map, "BINOLPTRPS11.getSumInfo");
	}
	
	/**
	 * 查询产品库存详细总数
	 * @param map
	 * @return
	 */
	public int getDeptStockCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS11_1.getDeptStockCount");
		return baseServiceImpl.getSum(parameterMap);
	}
	
	/**
	 * 取得产品库存List
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getDeptStock(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS11_1.getDeptStock");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得仓库某些产品库存List
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getDepotStockList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS11_2.getDepotStockList");
		return baseServiceImpl.getList(map);
	}
}
