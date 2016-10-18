/*
 * @(#)BINOLPTJCS14_Service.java v1.0 2014-6-12
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
package com.cherry.pt.jcs.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 柜台产品价格维护Service
 * 
 * @author JiJW
 * @version 1.0 2014-6-12
 */
public class BINOLPTJCS16_Service extends BaseService {
	
	/**
	 * 取得产品信息
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String,Object> getProductInfo(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS16.getProductInfo");
		return (Map<String, Object>) baseServiceImpl.get(parameterMap);
	}
	/**
	 * 取得产品信息List(根据)
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getProductInfoList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS16.getProductInfoList");
		return  baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 删除指定产品方案明细ID的数据
	 * @param map
	 * @return
	 * 
	 * */
	public int delPrtPriceSoluDetail(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS16.delPrtPriceSoluDetail");
		return baseServiceImpl.remove(map);
	}
	
	/**
	 * 删除方案绑定柜台对应的部门产品价格
	 * @param map
	 * @return
	 * 
	 * */
	public int delProductDepartPrice(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS16.delProductDepartPrice");
		return baseServiceImpl.remove(map);
	}
	
	/**
	 * 取得新方案明细与关联柜台List
	 * @param map
	 * @return
	 */
	public List getNewSoluDetailProductDepartPriceList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS16.getNewSoluDetailProductDepartPriceList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 更新部门产品表的数据
	 * @param map
	 * @return 
	 */
	@Deprecated
	public String mergeProductDepartInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS16.mergeProductDepartInfo");
		return String.valueOf(baseServiceImpl.get(map));
	}
	
	/**
	 * 更新产品价格方案明细表
	 * @param map
	 * @return 
	 */
	public Map<String,Object> mergePrtPriceSoluDetail(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS16.mergePrtPriceSoluDetail");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	
	/**
	 * 插入产品价格方案明细表 
	 * 
	 * @param map
	 * @return int
	 */
	public int insertPrtPriceSoluDetail(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS14.insertPrtPriceSoluDetail");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 更新更新产品方案主表的树形结构等信息
	 * @param map
	 * @return
	 */
	public int updProductDepart(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS16.updProductDepart");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 更新产品价格方案明细表
	 * @param map
	 * @return
	 */
	public int updPrtPriceSoluDetail(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS16.updPrtPriceSoluDetail");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 取得产品价格方案
	 * @param map
	 * @return
	 */
	public List getPrtPriceSolutionList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
    	//业务类型   0：基础数据；1：库存数据；2：会员数据； A:全部 等
		parameterMap.put("businessType", "0");
    	//操作类型   0：更新（包括新增，删除）；1：查询
		parameterMap.put("operationType", "1");
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS16.getPrtPriceSolutionList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 插入部门价格
	 * @param map
	 */
	public void insertProductPrice(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS16.insertProductPrice");
		baseServiceImpl.save(map);
	}
	
}
