/*
 * @(#)BINBEIFPRO04_Service.java     1.0 2014/8/15
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
 *产品下发(实时)SERVICE
 * 
 * 
 * @author jijw
 * @version 1.0 2014.8.15
 */
public class BINBEIFPRO04_Service extends BaseService {
	
	/**
	 * 更新销售日期或价格生效日期在业务日期前后1天内的产品的版本号
	 * 
	 * @param map
	 * @return
	 */
	public void updPrtVerByPrtSellDatePriceSaleDate(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFPRO04.updPrtVerByPrtSellDatePriceSaleDate");
		baseServiceImpl.update(map);
	}

	/**
	 * 查询新后台产品数据list
	 * 
	 * @param map
	 * 
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getProductList(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBEIFPRO04.getProductList");
		return baseServiceImpl.getList(map);
	}

	
	/**
	 * 将编码条码变更后，老的编码条码在product_SCS中停用
	 * @param map
	 * @return
	 */
	public Integer disProductSCS(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBEIFPRO04.disProductSCS");
		return ifServiceImpl.update(map);
	}
	
	/**
	 * 更新productSCS表
	 * @param map
	 * @return 
	 */
	public Map<String,Object> mergeProductSCS(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEIFPRO04.mergeProductSCS");
		return (Map<String, Object>)ifServiceImpl.get(map);
	}

	/**
	 * 插入ProductSetting
	 * 
	 * @param map
	 * 
	 * @return int
	 */
	public void addProductSetting(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBEIFPRO04.addProductSetting");
		ifServiceImpl.save(map);
	}
	
	/**
	 * 插入产品条码对应关系表
	 * 
	 * @param map
	 * @return
	 */
	public void insertPrtBarCode(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBEIFPRO04.insertPrtBarCode");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 更新停用日时
	 * 
	 * @param map
	 * @return
	 */
	public void updateClosingTime(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBEIFPRO04.updateClosingTime");
		baseServiceImpl.update(map);
	}
	
	/**
	 * 查询对应关系件数
	 * 
	 * @param map
	 * @return
	 */
	public int getBarCodeCount(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBEIFPRO04.getBarCodeCount");
		return baseServiceImpl.getSum(map);
	}

	public String getBrandCode(Map<String, Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID,
		"BINBEIFPRO04.getBrandCode");
		return ConvertUtil.getString(baseServiceImpl.get(map));
	}
	
	/**
	 * 查询编码条码变更数据
	 * @param map
	 * @return
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public Map<String,Object> getBarCodeModify(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEIFPRO04.getBarCodeModify");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	
	/**
	 * 根据CodeKey查询Val
	 * @param map
	 * @return
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getCodeByKey(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEIFPRO04.getCodeByKey");
		return baseConfServiceImpl.getList(map);
	}
	
	/**
	 * 根据指定Version取产品功能开启时间的产品信息List
	 * 
	 * @param map
	 * 
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPrtFunDetailByVersionList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,"BINBEIFPRO04.getPrtFunDetailByVersionList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 删除产品功能开启接口表(根据brandArr、PrtFunDateCode、产品厂商ID)
	 * 
	 * @param list
	 * @return
	 */
	public void delIFPrtFunEnableAll(List<Map<String, Object>> list){
		ifServiceImpl.deleteAll(list,"BINBEIFPRO04.delIFPrtFunEnable");
	}
	
	/**
	 * 插入产品功能开启时间明细接口表
	 * 
	 * @param list
	 * 
	 * @return int
	 */
	public void addIFPrtFunEnableAll(List<Map<String, Object>> list) {
		ifServiceImpl.deleteAll(list,"BINBEIFPRO04.addIFPrtFunEnable");
	}
	
	/**
	 * 备份产品下发数据备份履历表
	 * @param map
	 */
	public void backProductIssue(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,"BINBEIFPRO04.backProductIssue");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 删除指定产品的BOMlist(根据产品厂商ID)
	 * @param map
	 */
	public int delIFPrtBomSCS(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEIFPRO04.delIFPrtBomSCS");
		return ifServiceImpl.remove(paramMap);
	}
	
	/**
	 * 插入指定产品的BOMlist
	 * @param list
	 */
	public void insertIFPrtBomSCS(List<Map<String, Object>> list) {
		ifServiceImpl.saveAll(list, "BINBEIFPRO04.insertIFPrtBomSCS");
	}
}
