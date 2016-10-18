/*	
 * @(#)BINOLSTCM07_Service.java     1.0 2010/10/29		
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
package com.cherry.st.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class BINOLSTCM09_Service extends BaseService{
	
	/**
	 * 插入退库单主表
	 * 
	 * @param map
	 */
	public int insertProductReturn(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM09.insertProductReturn");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 插入退库单明细表
	 * 
	 * @param map
	 */
	public void insertProductReturnDetail(List<Map<String,Object>> list){
		baseServiceImpl.saveAll(list,"BINOLSTCM09.insertProductReturnDetail");
	}
	
	/**
	 * 修改退库单据主表数据
	 * 
	 * @param praMap
	 */
	 public int updateProductReturnMain(Map<String, Object> praMap){
		 praMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM09.updateProductReturnMain");
		 return baseServiceImpl.update(praMap);
	 }
	 
	/**
	 * 
	 *取得退库信息
	 * 
	 *@param praMap
     */
	 @SuppressWarnings("unchecked")
	public Map<String, Object> getProductReturnMainData(Map<String,Object> map) {
		 map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM09.getProductReturnMainData");
		 return (Map<String, Object>) baseServiceImpl.get(map);
	}
	 
	 /**
	  * 
	  *取得退库明细信息
	  * 
	  *@param praMap
	  */
	 @SuppressWarnings("unchecked")
	 public List<Map<String, Object>> getProductReturnDetailData(Map<String,Object> map) {
		 map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM09.getProductReturnDetailData");
		 return  baseServiceImpl.getList(map);
	 }
}
