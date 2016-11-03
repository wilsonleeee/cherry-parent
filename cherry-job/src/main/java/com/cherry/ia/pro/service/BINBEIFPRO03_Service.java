/*
 * @(#)BINBEIFPRO03_Service.java     1.0 2014/5/26
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
package com.cherry.ia.pro.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.ConvertUtil;

/**
 * 
 *柜台产品下发SERVICE
 * 
 * 
 * @author jijw
 * @version 1.0 2014.5.26
 */
public class BINBEIFPRO03_Service extends BaseService {

	/**
	 * 查询新后台柜台产品数据list
	 * 
	 * @param int
	 * 
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getCouProductList(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBEIFPRO03.getCouProductList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 更新方案中销售日期在业务日期前后1天内的产品的版本号
	 * 
	 * @param map
	 * @return
	 */
	public void updSoluDetailVerByPrtSellDate(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFPRO03.updSoluDetailVerByPrtSellDate");
		baseServiceImpl.update(map);
	}
	
	/**
	 * 查询新后台产品方案柜台关联数据list
	 * @param map
	 * @return
	 */
	public List getPrtSoluCouList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEIFPRO03.getPrtSoluCouList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 根据指定Version取方案明细的产品信息List
	 * @param map
	 * @return
	 */
	public List getPrtSoluDetailByVersionList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEIFPRO03.getPrtSoluDetailByVersionList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 插入柜台产品接口数据库(WITPOSA_product_with_counter)
	 * 
	 * @param Map
	 * 
	 * @return int
	 */
	public void addProductWithCounter(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBEIFPRO03.addProductWithCounter");
		ifServiceImpl.save(map);
	}
	
	/**
	 * 插入柜台产品接口数据库(WITPOSA_product_with_counter)
	 * 
	 * @param Map
	 * 
	 * @return int
	 */
	public void addIFPrtSoluWithCounter(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBEIFPRO03.addIFPrtSoluWithCounter");
		ifServiceImpl.save(map);
	}
	
	/**
	 * 插入产品方案明细接口表
	 * 
	 * @param Map
	 * 
	 * @return int
	 */
	public void addIFPrtSoluSCS(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,"BINBEIFPRO03.addIFPrtSoluSCS");
		ifServiceImpl.save(map);
	}
	
	/**
	 * 物理删除接口数据库某品牌的柜台产品信息
	 * 
	 * @param map
	 * @return
	 */
	public int delIFCouProduct(Map<String, Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBEIFPRO03.delIFCouProduct");
		return ifServiceImpl.remove(map);
	}
	
	/**
	 * 删除指定的柜台产品数据
	 * 
	 * @param map
	 * @return
	 */
	public int delIFProductWithCounter(Map<String, Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBEIFPRO03.delIFProductWithCounter");
		return ifServiceImpl.remove(map);
	}
	
	
	/**
	 * 物理删除产品方案柜台接口表
	 * 
	 * @param map
	 * @return
	 */
	public int delIFPrtSoluWithCounter(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEIFPRO03.delIFPrtSoluWithCounter");
		return ifServiceImpl.remove(map);
	}
	
	/**
	 * 物理删除产品方案明细接口表
	 * 
	 * @param map
	 * @return
	 */
	public int delIFPrtSoluSCS(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEIFPRO03.delIFPrtSoluSCS");
		return ifServiceImpl.remove(map);
	}
	
	/**
	 * 
	 * @param map
	 * @return
	 */
	public String getBrandCode(Map<String, Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBEIFPRO03.getBrandCode");
		return ConvertUtil.getString(baseServiceImpl.get(map));
	}
	
	
	
	/** ********************** 区域城市/渠道的柜台变化  ***************************************************************************************** */
	/**
	 * 取得产品方案配置信息List(UpdateTime升序)
	 * @param map
	 * @return
	 */
	public List getDPConfigDetailList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEIFPRO03.getDPConfigDetailList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 取得方案配置的区域或渠道实际的的柜台与以前配置的差异(区域城市/渠道)List
	 * @param map
	 * @return
	 */
	public List getCntForPrtSoluCityChannelDiff(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put("businessType", "1");
		parameterMap.put("operationType", "1");
		parameterMap.put("userId", map.get("userID"));
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEIFPRO03.getCntForPrtSoluCityChannelDiff");
		return baseServiceImpl.getList(parameterMap);
	}
	
	
	/**
	 * 取方案明细的产品信息List
	 * @param map
	 * @return
	 */
	public List getPrtPriceSoluDetailList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEIFPRO03.getPrtPriceSoluDetailList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 更新部门产品表的数据
	 * @param map
	 * @return 
	 */
	public String mergeProductDepartInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEIFPRO03.mergeProductDepartInfo");
		return String.valueOf(baseServiceImpl.get(map));
	}
	
	/**
	 * merge产品方案部门关系表
	 * @param map
	 * @return 
	 */
	public String mergePrtSoluDepartRelation(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEIFPRO03.mergePrtSoluDepartRelation");
		return String.valueOf(baseServiceImpl.get(map));
	}
	
	/**
	 * 更新更新产品方案主表的树形结构等信息
	 * @param map
	 * @return
	 */
	public int updProductDepart(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEIFPRO03.updProductDepart");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 更新更新产品方案主表的树形结构等信息
	 * @param map
	 * @return
	 */
	public int updPrtSoluDepartRelation(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEIFPRO03.updPrtSoluDepartRelation");
		return baseServiceImpl.update(map);
	}	
	/**
	 * 删除方案绑定柜台对应的部门产品价格
	 * @param map
	 * @return
	 * 
	 * */
	public int delProductDepartPrice(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEIFPRO03.delProductDepartPrice");
		return baseServiceImpl.remove(map);
	}
	
	/**
	 * 删除方案配置履历表中指定的方案
	 * 
	 * @param map
	 * @return
	 */
	public int delPrtSoluWithDepartHis(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEIFPRO03.delPrtSoluWithDepartHis");
		return baseServiceImpl.remove(map);
	}
	
	/**
	 * 更新产品方案配置履历表  
	 * @param map
	 * @return 
	 */
	public String mergePrtSoluWithDepartHis(Map<String, Object> map){
		map.put("businessType", "1");
		map.put("operationType", "1");
		map.put("userId", map.get("userID"));
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEIFPRO03.mergePrtSoluWithDepartHis");
		return String.valueOf(baseServiceImpl.get(map));
	}
	
	/**
	 * 插入部门价格
	 * @param map
	 */
	public void insertProductPrice(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEIFPRO03.insertProductPrice");
		baseServiceImpl.save(map);
	}
	

	
	// **************************************************  颖通产品方案明细维护  *****************************************************************************//
	// 颖通需求：颖通的需求是不同的柜台销售不同产品，但销售的价格是相同的
	
	/**
	 * 取得产品价格方案
	 * @param map
	 * @return
	 */
	public List getPrtPriceSolutionList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEIFPRO03.getPrtPriceSolutionList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 取得产品方案明细表的产品与以前配置的差异List
	 * @param map
	 * @return
	 */
	public List getPrtForPrtSoluDetailDiff(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEIFPRO03.getPrtForPrtSoluDetailDiff");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * merge产品方案明细表  
	 * @param map
	 * @return 
	 */
	public String mergeProductPriceSolutionDetail(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEIFPRO03.mergeProductPriceSolutionDetail");
		return String.valueOf(baseServiceImpl.get(map));
	}
	
	/**
	 * 将产品方案明细表的数据无效 
	 * @param map
	 * @return
	 */
	public int updPrtSoluDetail(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEIFPRO03.updPrtSoluDetail");
		return baseServiceImpl.update(map);
	}
	
	/**
	 *  更新产品方案明细表的产品价格为最新的产品方案价格    
	 * @param map
	 * @return 
	 */
	public List mergePPSDPrice(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEIFPRO03.mergePPSDPrice");
		return baseServiceImpl.getList(map);
	}
}
