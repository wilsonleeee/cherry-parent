/*
 * @(#)BINOLPTJCS01_Service.java     1.0 2011/04/11
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
 * 产品分类维护Service
 * 
 * @author lipc
 * @version 1.0 2011.04.11
 */
@SuppressWarnings("unchecked")
public class BINOLPTJCS01_Service extends BaseService {

	/**
	 * 根据分类名取得分类ID
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public int getPropId1(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS01.getPropId1");
		return CherryUtil.obj2int(baseServiceImpl.get(map));
	}
	
	/**
	 * 根据分类终端下发取得分类ID
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public int getPropId2(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS01.getPropId2");
		return CherryUtil.obj2int(baseServiceImpl.get(map));
	}

	/**
	 * 取得分类Info
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public Map getCategoryInfo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS01.getCategoryInfo");
		return (Map) baseServiceImpl.get(map);
	}

	/**
	 * 取得分类List
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getCategoryList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS01.getCategoryList");
		return baseServiceImpl.getList(map);
	}

	/**
	 * 分类树形顺序批量更新
	 * 
	 * @param map
	 */
	public void updViewSeq(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS01.updViewSeq");
		baseServiceImpl.update(map);
	}

	/**
	 * 添加分类
	 * 
	 * @param map
	 */
	public int addCatProperty(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS01.addCatProperty");
		return baseServiceImpl.saveBackId(map);
	}

	/**
	 * 分类更新
	 * 
	 * @param map
	 */
	public void updCatProperty(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS01.updCatProperty");
		baseServiceImpl.update(map);
	}

	/**
	 * 根据属性值,品牌查询分类属性值ID
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public int getCateValId(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS01.getCateValId");
		return CherryUtil.obj2int(baseServiceImpl.get(map));
	}
	
	/**
	 * 根据属性值查询分类属性值ID
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public int getCateValId1(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS01.getCateValId1");
		return CherryUtil.obj2int(baseServiceImpl.get(map));
	}
	
	/**
	 * 根据属性值名查询分类属性值ID
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public int getCateValId2(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS01.getCateValId2");
		return CherryUtil.obj2int(baseServiceImpl.get(map));
	}
	/**
	 * 取得分类选项值Info
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public Map getCateVal(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS01.getCateVal");
		return (Map) baseServiceImpl.get(map);
	}

	/**
	 * 取得分类选项值List
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getCateValList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS01.getCateValList");
		return baseServiceImpl.getList(map);
	}

	/**
	 * 添加分类选项值
	 * 
	 * @param map
	 */
	public int addPropVal(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS01.addPropVal");
		return baseServiceImpl.saveBackId(map);
	}

	/**
	 * 更新分类选项值
	 * 
	 * @param map
	 */
	public void updPropVal(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS01.updPropVal");
		baseServiceImpl.update(map);
	}
	
	/**
	 * 更新分类绑定
	 * 
	 * @param map
	 */
	public void updBindCate(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS01.updBindCate");
		baseServiceImpl.update(map);
	}
	
	/**
	 * 取得绑定大分类属性值ID
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public int getBindPropValId(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS01.getBindPropValId");
		return CherryUtil.obj2int(baseServiceImpl.get(map));
	}
	
	/**
	 * 分类树形顺序批量更新
	 * 
	 * @param map
	 */
	public void updProduct(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS01.updProduct");
		baseServiceImpl.update(map);
	}
}
