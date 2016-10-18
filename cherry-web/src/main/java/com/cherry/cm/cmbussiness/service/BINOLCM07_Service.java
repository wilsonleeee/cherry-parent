/*		
 * @(#)BINOLCM07_Service.java     1.0 2010/12/08		
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
package com.cherry.cm.cmbussiness.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 弹出datatable共通Service
 * @author dingyc
 *
 */
@SuppressWarnings("unchecked")
public class BINOLCM07_Service extends BaseService{
	/**
	 * 取得促销产品总数
	 * @param map
	 * @return
	 */
	public int getPromotionInfoCount (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM07.getPromotionCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 取得促销产品信息
	 * @param map
	 * @return
	 */
	public List getPromotionShortInfoList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM07.getPromotionShortInfoList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得柜台总数
	 * 
	 * @param map 查询条件
	 * @return 返回柜台总数
	 */
	public int getCounterInfoCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM07.getCounterInfoCount");
		return baseServiceImpl.getSum(parameterMap);
	}
	
	/**
	 * 取得柜台List
	 * 
	 * @param map 查询条件
	 * @return 柜台List
	 */
	public List<Map<String, Object>> getCounterInfoList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM07.getCounterInfoList");
		return (List)baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 根据柜台ID取得柜台信息
	 * 
	 * @param map 查询条件
	 * @return 柜台信息
	 */
	public Map<String, Object> getCounterInfo(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM07.getCounterInfo");
		return (Map)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 取得厂商List
	 * 
	 * @param map 查询条件
	 * @return 厂商List
	 */
	public List getFactoryList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM07.getFactoryList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 查询某一区域的所有上级区域
	 * 
	 * @param map 查询条件
	 * @return 区域List
	 */
	public List getHigherRegionList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM07.getHigherRegionList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 取得部门(办事处或柜台)总数
	 * 
	 * @param map 查询条件
	 * @return 返回柜台总数
	 */
	public int getDepartInfoCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM07.getDepartInfoCount");
		return baseServiceImpl.getSum(parameterMap);
	}
	
	/**
	 * 取得部门(办事处或柜台)List
	 * 
	 * @param map 查询条件
	 * @return 部门(办事处或柜台)List
	 */
	public List getDepartInfoList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM07.getDepartInfoList");
		return baseServiceImpl.getList(parameterMap);
	}
}
