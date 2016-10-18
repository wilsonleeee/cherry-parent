/*
 * @(#)BINOLSSPRM66_Service.java     1.0 2013/09/17
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

import java.util.List;
import java.util.Map;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 入库导入Service
 * 
 * @author zhangle
 * @version 1.0 2013.09.17
 */
public class BINOLSSPRM66_Service extends BaseService {
	
	//重复数据判断
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getRepeatData(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM66.getRepeatData");
		return baseServiceImpl.getList(map);
	}

	/**
	 * 通过促销品厂商编码取得产品信息
	 * 
	 * @param UnitCode
	 *            促销品厂商编码
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPrmInfoByUnitCode(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM66.getPrmInfoByUnitCode");
		return baseServiceImpl.getList(map);
	}

	/**
	 * 通过产品ID取得产品的价格信息
	 * 
	 * @param BIN_PromotionProductID
	 *            促销品ID
	 * @return
	 */
	public Object getPrmPrice(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM66.getPrmPrice");
		return baseServiceImpl.get(map);
	}

	/**
	 * 插入入库单Excel导入主表
	 * 
	 * @param map
	 * @return
	 */
	public int insertPrmInDepotExcel(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM66.insertPrmInDepotExcel");
		return baseServiceImpl.saveBackId(map);
	}

	/**
	 * 插入入库单Excel导入明细表
	 * 
	 * @param map
	 */
	public void insertPrmInDepotDetailExcel(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM66.insertPrmInDepotDetailExcel");
		baseServiceImpl.save(map);
	}

	/**
	 * 通过逻辑仓库名称获取逻辑仓库信息
	 * 
	 * @param Type
	 *            类型
	 * @param InventoryName
	 *            仓库中文名称或英文名称
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getLogicInventoryByName(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM66.getLogicInventoryByName");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}

	/**
	 * 通过部门编码取得部门信息
	 * 
	 * @param DepartCode
	 *            部门编码
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getOrgByCode(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM66.getOrgByCode");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
}