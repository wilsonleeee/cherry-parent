/*
 * @(#)BINBESSPRM03_Service.java     1.0 2010/12/20
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 *促销品下发Service
 * 
 * 
 * @author hub
 * @version 1.0 2010.12.20
 */
@SuppressWarnings("unchecked")
public class BINBESSPRM03_Service extends BaseService {
	
	/**
	 * 取得促销产品总数
	 * 
	 * @param map
	 * @return
	 */
	public int getPromPrtCount(Map<String, Object> map) {
		map.put("unitCodeTZZK", CherryBatchConstants.PROMOTION_TZZK_UNIT_CODE);
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBESSPRM03.getPromPrtCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 查询促销品信息
	 * 
	 * @param Map
	 *            查询条件
	 * 
	 * @return List 促销品信息List
	 * 
	 */
	public List<Map<String, Object>> getPromPrtList(Map<String, Object> map) {
		map.put("unitCodeTZZK", CherryBatchConstants.PROMOTION_TZZK_UNIT_CODE);
		map.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBESSPRM03.getPromPrtList");
		return baseServiceImpl.getList(map);
	}

	/**
	 * 清空促销品中间表数据
	 * 
	 * @param map
	 *            查询条件
	 * 
	 */
	public void clearPromotionTable(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBESSPRM03.clearPromotionTable");
		ifServiceImpl.remove(map);
	}

	/**
	 * 插入促销品中间表
	 * 
	 * @param map
	 * @return
	 */
	public void insertPromotionProduct(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBESSPRM03.insertPromotionTable");
		ifServiceImpl.save(map);
	}
	
	/**
	 * 插入促销产品条码对应关系表
	 * 
	 * @param map
	 * @return
	 */
	public void insertPromotionPrtBarCode(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBESSPRM03.insertPromotionPrtBarCode");
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
				"BINBESSPRM03.updateClosingTime");
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
				"BINBESSPRM03.getBarCodeCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 查询编码条码变更数据
	 * @param map
	 * @return
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public Map<String,Object> getBarCodeModify(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBESSPRM03.getBarCodeModify");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	
	/**
	 * 插入ProductSetting
	 * 
	 * @param Map
	 * 
	 * @return int
	 */
	public void addProductSetting(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBESSPRM03.addProductSetting");
		ifServiceImpl.save(map);
	}
	
	/**
	 * 取得虚拟促销品
	 * @param map
	 * @return
	 */
	public List<String> getVirtualPrmList(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBESSPRM03.getVirtualPrmList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 插入促销产品表并返回促销产品ID
	 * 
	 * @param map
	 * @return int
	 */
	public int insertPromotionProductBackId(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID,
				"BINBESSPRM03.insertPromotionProduct");
		return baseServiceImpl.saveBackId(paramMap);
	}
	
	/**
	 * 插入促销产品厂商表
	 * 
	 * @param map
	 * @return
	 */
	public void insertPromProductVendor(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID,
				"BINBESSPRM03.insertPromProductVendor");
		baseServiceImpl.save(paramMap);
	}
	
}
