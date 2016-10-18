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

public class BINOLSTCM08_Service extends BaseService{
	
	/**
	 * 插入入库单主表
	 * 
	 * @param map
	 */
	public int insertProductInDepot(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM08.insertProductInDepot");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 插入入库单明细表
	 * 
	 * @param map
	 */
	public void insertProductInDepotDetail(List<Map<String,Object>> list){
		baseServiceImpl.saveAll(list,"BINOLSTCM08.insertProductInDepotDetail");
	}
	
	/**
	 * 修改入库单据主表数据
	 * 
	 * @param praMap
	 */
	 public int updateProductInDepotMain(Map<String, Object> praMap){
		 praMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM08.updateProductInDepotMain");
		 return baseServiceImpl.update(praMap);
	 }
	 
	/**
	 * 
	 *取得入库信息
	 * 
	 *@param praMap
     */
	 @SuppressWarnings("unchecked")
	public Map<String, Object> getProductInDepotMainData(Map<String,Object> map) {
		 map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM08.getProductInDepotMainData");
		 return (Map<String, Object>) baseServiceImpl.get(map);
	}
	 
	 /**
	  * 
	  *取得入库明细信息
	  * 
	  *@param praMap
	  */
	 @SuppressWarnings("unchecked")
	 public List<Map<String, Object>> getProductInDepotDetailData(Map<String,Object> map) {
		 map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM08.getProductInDepotDetailData");
		 return  baseServiceImpl.getList(map);
	 }
	 
	 /**
	  * 判断入库单号存在
	  * @param map
	  * @return
	  */
	 public List<Map<String,Object>> selProductInDepot(Map<String, Object> map){
	     Map<String, Object> parameterMap = new HashMap<String, Object>();
	     parameterMap.putAll(map);
	     parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM08.selProductInDepot");
	     return baseServiceImpl.getList(parameterMap);
	 }
	 
	 /**
	  * 给定入库单主ID，删除入库单明细
	  * @param map
	  * @return
	  */
	 public int delProductInDepotDetailData(Map<String, Object> map){
	     Map<String, Object> parameterMap = new HashMap<String, Object>();
	     parameterMap.putAll(map);
	     parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTCM08.delProductInDepotDetailData");
	     return baseServiceImpl.remove(parameterMap);
	 }
}