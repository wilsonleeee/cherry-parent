/*	
 * @(#)BINOLSSPRM31_Service.java     1.0 2010/11/04		
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
 * 库存记录查询service
 * 
 * @author lipc
 * @version 1.0 2010.11.04
 * 
 */
@SuppressWarnings("unchecked")
public class BINOLSSPRM31_Service extends BaseService {

	/**
	 * 取得库存记录总数
	 * 
	 * @param map
	 * @return
	 */
	public int getProStockCount(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM31.getProStockCount");
		return baseServiceImpl.getSum(map);
	}

	/**
	 * 取得库存记录LIST
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getProStockList(Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM31.getProStockList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得汇总信息
	 * 
	 * */
	public Map<String,Object> getSumInfo(Map<String,Object> map){
		return (Map<String,Object>)baseServiceImpl.get(map, "BINOLSSPRM31.getSumInfo");
	}
	
	/**
	 * 取得促销品库存List
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getDeptStock(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM31_1.getDeptStock");
		return baseServiceImpl.getList(map);
	}
}
