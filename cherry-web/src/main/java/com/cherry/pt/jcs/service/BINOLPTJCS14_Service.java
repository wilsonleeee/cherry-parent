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
public class BINOLPTJCS14_Service extends BaseService {
	
	/**
	 * 取得柜台产品配置主表信息
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String,Object> getDepartProductConfig(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS14.getDepartProductConfig");
		return (Map<String, Object>) baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 插入柜台产品配置表
	 * 
	 * @param map
	 * @return int
	 */
	public int insertDepartProductConfig(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS14.insertDepartProductConfig");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 取得区域信息
	 * @param map
	 * @return
	 */
	public List getRegionInfoList (Map<String, Object> map){
		map.put("userId", map.get("userID"));
		map.put("businessType", "1");
		map.put("operationType", "1");
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS14.getRegionInfoList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得渠道信息
	 * @param map
	 * @return
	 */
	public List getChannelInfoList (Map<String,Object> map){
		map.put("userId", map.get("userID"));
		map.put("businessType", "1");
		map.put("operationType", "1");
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS14.getChannelInfoList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得产品价格方案
	 * @param map
	 * @return
	 */
	public List getPrtPriceSolutionList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS14.getPrtPriceSolutionList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 根据分类取得产品
	 * @param map
	 * @return
	 */
	public List getPrtByCateVal(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS14.getPrtByCateVal");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 插柜台产品配置信息
	 * 
	 * @param map
	 * @return int
	 */
	public int insertConfigDetail(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS14.insertConfigDetail");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 插入产品价格方案主表
	 * 
	 * @param map
	 * @return int
	 */
	public int insertPrtPriceSolu(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS14.insertPrtPriceSolu");
		return baseServiceImpl.saveBackId(map);
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
	 * 删除指定柜台的柜台产品配置信息
	 * @param map
	 * @return
	 * 
	 * */
	public int delConfigDetail(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS14.delConfigDetail");
		return baseServiceImpl.remove(map);
	}
	
	/**
	 * 删除指定产品方案明细ID的数据
	 * @param map
	 * @return
	 * 
	 * */
	public int delPrtPriceSoluDetail(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS14.delPrtPriceSoluDetail");
		return baseServiceImpl.remove(map);
	}
	
	/**
	 * 取柜台对应的产品价格配置的方案
	 * @param map
	 * @return
	 */
	public Map<String,Object> getPrtPriceSoluInfoByCnt(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS14.getPrtPriceSoluInfoByCnt");
		return (Map<String, Object>) baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 取方案的产品信息
	 * @param map
	 * @return
	 */
	public Map<String,Object> getPrtPriceSoluDetailInfo(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS14.getPrtPriceSoluDetailInfo");
		return (Map<String, Object>) baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 更新更新产品方案主表的树形结构等信息
	 * @param map
	 * @return
	 */
	public int updPrtPriceSolution(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS14.updPrtPriceSolution");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 更新柜台产品配置主表信息
	 * @param map
	 * @return
	 */
	public int updDepartProductConfig(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS14.updDepartProductConfig");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 取得产品价格方案主表信息
	 * @param map
	 * @return
	 */
	public Map<String,Object> getPrtPriceSoluInfo(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS14.getPrtPriceSoluInfo");
		return (Map<String, Object>) baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 根据配置ID、柜台号取得方案中的产品信息
	 * @param map
	 * @return
	 */
	public List getPrtSoluByConfig(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS14.getPrtSoluByConfig");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 插入部门价格
	 * @param map
	 */
	public void insertProductPrice(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS14.insertProductPrice");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 删除指定品牌及对应部门Code的部门价格信息
	 * @param map
	 * @return
	 * 
	 * */
	public int delProductPrice(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS14.delProductPrice");
		return baseServiceImpl.remove(map);
	}
	
	/**
	 * 删除指定品牌及对应部门Code的产品部门信息 
	 * @param map
	 * @return
	 */
	public int delPrtDepartByDepartCode(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS14.delPrtDepartByDepartCode");
		return baseServiceImpl.remove(map);
	}
	
	/**
	 * 插入产品部门表
	 * @param map
	 * @return
	 */
	public int insertProductDepartInfo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS14.insertProductDepartInfo");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 获取产品当前有效的销售价格List
	 * @param map
	 * @return list
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getSellPriceList(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS14.getSellPriceList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 获取产品对应的按显示顺序排序的分类属性ID
	 * @param map
	 * @return
	 */
	public String getPrtCateVal(Map<String,Object> map){
		return (String) baseServiceImpl.get(map, "BINOLPTJCS14.getPrtCateVal"); 
	}
}
