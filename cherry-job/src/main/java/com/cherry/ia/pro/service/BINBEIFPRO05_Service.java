/*
 * @(#)BINBEIFPRO05_Service.java     1.0 2016/7/4
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
 * @author chenkuan
 * @version 1.0 2016.7.4
 */
public class BINBEIFPRO05_Service extends BaseService {

	
	/**
	 * 更新特价产品方案中销售日期在业务日期前后1天内的产品的版本号
	 * 
	 * @param map
	 * @return
	 */
	public void updSoluDetailVerByPrtSellDate(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFPRO05.updSoluDetailVerByPrtSellDate");
		baseServiceImpl.update(map);
	}
	
	/**
	 * 查询新后台特价产品方案柜台关联数据list
	 * @param map
	 * @return
	 */
	public List getPrtSoluCouList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEIFPRO05.getPrtSoluCouList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 根据指定Version取特价方案明细的产品信息List
	 * @param map
	 * @return
	 */
	public List getPrtSoluDetailByVersionList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEIFPRO05.getPrtSoluDetailByVersionList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	
	/**
	 * 插入特价产品接口数据库(WITPOSA_Product_SpecialOfferSolu)
	 * 
	 * @param Map
	 * 
	 * @return int
	 */
	public void addIFPrtSoluWithCounter(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBEIFPRO05.addIFPrtSoluWithCounter");
		ifServiceImpl.save(map);
	}
	
	/**
	 * 插入特价产品方案明细接口表
	 * 
	 * @param Map
	 * 
	 * @return int
	 */
	public void addIFPrtSoluSCS(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,"BINBEIFPRO05.addIFPrtSoluSCS");
		ifServiceImpl.save(map);
	}
	
	
	
	
	/**
	 * 物理删除特价产品方案柜台接口表
	 * 
	 * @param map
	 * @return
	 */
	public int delIFPrtSoluWithCounter(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEIFPRO05.delIFPrtSoluWithCounter");
		return ifServiceImpl.remove(map);
	}
	
	/**
	 * 物理删除特价产品方案明细接口表
	 * 
	 * @param map
	 * @return
	 */
	public int delIFPrtSoluSCS(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEIFPRO05.delIFPrtSoluSCS");
		return ifServiceImpl.remove(map);
	}
	
	/**
	 * 
	 * @param map
	 * @return
	 */
	public String getBrandCode(Map<String, Object> map){
		map.put(CherryBatchConstants.IBATIS_SQL_ID,"BINBEIFPRO05.getBrandCode");
		return ConvertUtil.getString(baseServiceImpl.get(map));
	}

}
