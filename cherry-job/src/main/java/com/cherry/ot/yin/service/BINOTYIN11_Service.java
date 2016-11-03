/*	
 * @(#)BINOTYIN11_Service.java     1.0 @2013-5-02
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
package com.cherry.ot.yin.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 *
 * 颖通接口：颖通柜台导入Service
 *
 * @author jijw
 *
 * @version  2013-5-02
 */
@SuppressWarnings("unchecked")
public class BINOTYIN11_Service extends BaseService {
	
	/**
	 * 取得颖柜台接口表数据(颖通数据源)
	 * 
	 * @param map
	 * @return
	 */
	public List getCounterListForOT(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOTYIN11.getCounterListForOT");
		return tpifServiceImpl.getList(map);
	}
	
	/**
	 * 
	 * 查询组织结构中的柜台信息
	 * 
	 * @param counterMap 查询条件
	 * @return 部门ID
	 * 
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getOrganizationId(Map<String, Object> counterMap) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(counterMap);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINOTYIN11.getOrganizationId");
		return (Map)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 
	 * 查询柜台在组织结构表中的插入位置
	 * 
	 * @param counterMap 查询条件
	 * @return Object
	 * 
	 */				
	public Object getCounterNodeId(Map<String, Object> counterMap) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(counterMap);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINOTYIN11.getCounterNodeId");
		return baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 
	 * 在组织结构表中插入柜台节点
	 * 
	 * @param counterMap 插入内容
	 * @return Object
	 * 
	 */					
	public Object insertCouOrg(Map<String, Object> counterMap) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(counterMap);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINOTYIN11.insertCouOrg");
		return baseServiceImpl.saveBackId(paramMap);
	}
	
	/**
	 * 取得柜台主管所在部门节点
	 * 
	 * @param map 查询条件
	 * @return 部门节点
	 */
	public String getCouHeadDepPath(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINOTYIN11.getCouHeadDepPath");
		return (String)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 
	 * 更新在组织结构中的柜台
	 * 
	 * @param counterMap 更新条件
	 * @return 更新件数
	 * 
	 */
	public int updateCouOrg(Map<String, Object> counterMap) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(counterMap);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINOTYIN11.updateCouOrg");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 
	 * 查询柜台信息
	 * 
	 * @param counterMap 查询条件
	 * @return 柜台ID
	 * 
	 */
	public Object getCounterId(Map<String, Object> counterMap) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(counterMap);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINOTYIN11.getCounterId");
		return baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 
	 * 插入柜台信息
	 * 
	 * @param counterMap 插入内容
	 * @return 柜台ID
	 * 
	 */
	public int insertCounterInfo(Map<String, Object> counterMap) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(counterMap);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINOTYIN11.insertCounterInfo");
		return baseServiceImpl.saveBackId(paramMap);
	}
	
	/**
	 * 
	 * 更新柜台信息表
	 * 
	 * @param counterMap 更新条件
	 * @return 更新件数
	 * 
	 */
	public int updateCounterInfo(Map<String, Object> counterMap) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(counterMap);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINOTYIN11.updateCounterInfo");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 取得柜台信息(新老后台交互时使用)
	 * 
	 * @param map 查询条件
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getCounterInfo(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOTYIN11.getCounterInfo");
		return (Map)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 取得香港（城市）对应的城市、省份、大区ID
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getCounterRegionID(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
//		parameterMap.put("cityName", "香港");
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOTYIN11.getCounterRegionID");
		return baseServiceImpl.getList(parameterMap);
	}

}
