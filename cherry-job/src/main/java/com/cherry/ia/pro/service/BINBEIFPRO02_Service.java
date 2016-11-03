/*
 * @(#)BINBEIFPRO02_Service.java     1.0 2011/6/15
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

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.ConvertUtil;

/**
 * 
 *产品下发SERVICE
 * 
 * 
 * @author lipc
 * @version 1.0 2011.6.15
 */
public class BINBEIFPRO02_Service extends BaseService {

	/**
	 * 查询新后台产品数据list
	 * 
	 * @param int
	 * 
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getProductList(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBEIFPRO02.getProductList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 插入产品接口数据库
	 * 
	 * @param Map
	 * 
	 * @return int
	 */
	public void addProduct(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBEIFPRO02.addProduct");
		ifServiceImpl.save(map);
	}
	
	/**
	 * 插入ProductSetting
	 * 
	 * @param Map
	 * 
	 * @return int
	 */
	public void addProductSetting(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBEIFPRO02.addProductSetting");
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
				"BINBEIFPRO02.insertPrtBarCode");
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
				"BINBEIFPRO02.updateClosingTime");
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
				"BINBEIFPRO02.getBarCodeCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 物理删除接口数据库某品牌的产品信息
	 * 
	 * @param map
	 * @return
	 */
	public int delIFProduct(Map<String, Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID,
		"BINBEIFPRO02.delIFProduct");
		return ifServiceImpl.remove(map);
	}
	
	public String getBrandCode(Map<String, Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID,
		"BINBEIFPRO02.getBrandCode");
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
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEIFPRO02.getBarCodeModify");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
}
