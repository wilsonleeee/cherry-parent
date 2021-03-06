/*
 * @(#)BINOLPTJCS03_Service.java     1.0 2011/03/19
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

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.CherryUtil;

/**
 * 产品添加Service
 * 
 * @author lipc
 * @version 1.0 2011.03.19
 */
public class BINOLPTJCS03_Service extends BaseService {

	/**
	 * 取得产品ID
	 * 
	 * @param map
	 * @return int
	 */
	public int getProductId(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS03.getProductId");
		return CherryUtil.obj2int(baseServiceImpl.get(map));
	}
	
	/**
	 * 取得促销产品ID
	 * 
	 * @param map
	 * @return int
	 */
	public int getPromotionId(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS03.getPromotionId");
		return CherryUtil.obj2int(baseServiceImpl.get(map));
	}
	
	/**
	 * 取得产品分类List
	 * 
	 * @param map
	 * @return String
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getCategoryList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS03.getCategoryList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得产品分类
	 * 
	 * @param map
	 * @return map
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getCateValList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS03.getCateValList2");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 插入产品表并返回产品ID
	 * 
	 * @param map
	 * @return int
	 */
	public int insertProduct(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS03.insertProduct");
		return baseServiceImpl.saveBackId(map);
	}

	/**
	 * 插入产品厂商表
	 * 
	 * @param map
	 * @return
	 */
	public int insertProductVendor(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLPTJCS03.insertProductVendor");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 插入产品分类对应表
	 * 
	 * @param map
	 * @return
	 */
	public void insertPrtCategory(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLPTJCS03.insertPrtCategory");
		baseServiceImpl.save(map);
	}

	/**
	 * 插入产品价格表
	 * 
	 * @param map
	 * @return
	 */
	public void insertProductPrice(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLPTJCS03.insertProductPrice");
		baseServiceImpl.save(map);
	}

	/**
	 * 插入产品图片信息表
	 * 
	 * @param map
	 * @return
	 */
	public void insertProductImage(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLPTJCS03.insertProductImage");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 大分类属性值ID取得小分类属性List
	 * 
	 * @param map
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getSubCateList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS03.getSubCateList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 小分类属性值ID取得大分类属性List 
	 * 
	 * @param map
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPatCateList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS03.getPatCateList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 插入产品BOM表
	 * 
	 * @param map
	 */
	public void insertProductBOM(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS03.insertProductBOM");
		baseServiceImpl.save(map);
	}
}
