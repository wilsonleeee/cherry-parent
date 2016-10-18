/*
 * @(#)BINOLSTIOS09_Service.java     1.0 2013/07/05
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
package com.cherry.st.ios.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.ConvertUtil;

/**
 * 
 * 入库excel导入
 * 
 * @author zhangle
 * @version 1.0 2013.07.05
 */
public class BINOLSTIOS09_Service extends BaseService{
	
	/**
	 * 查询（Excel导入）批次信息
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getImportBatchList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTIOS09.getImportBatchList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 查询（Excel导入）批次总数
	 * @param map
	 * @return
	 */
	public int getImportBatchCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTIOS09.getImportBatchCount");
		return ConvertUtil.getInt(baseServiceImpl.get(map));
	}

	/**
	 * 判断数据是否重复
	 * 对厂商编码与备用厂商编码，以厂商编码是否重复为主（厂商编码不存在时才看备用厂商编码）
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getRepeatData(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTIOS09.getRepeatData");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 判断在入库单表或者入库单（Excel）表中是否已经存在指定单据号的数据
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getRepeatBillNo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTIOS09.getRepeatBillNo");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 通过逻辑仓库名称获取逻辑仓库信息
	 * @param Type 类型
	 * @param InventoryName 仓库中文名称或英文名称
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getLogicInventoryByName(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTIOS09.getLogicInventoryByName");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	
	/**
	 * 通过部门编码取得部门信息
	 * @param DepartCode 部门编码
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getOrgInfo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTIOS09.getOrgOfCode");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	
	/**
	 * 通过产品厂商编码或者备用厂商编码取得产品信息（以厂商编码为主）
	 * @param UnitCode 产品厂商编码
	 * @param UnitCodeReference 厂商编码(备用)
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPrtInfo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTIOS09.getPrtInfoByUnitCode");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 通过产品ID取得产品的价格信息
	 * @param BIN_ProductID 产品ID
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPrtPrice(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTIOS09.getPrtPrice");
		return baseServiceImpl.getList(map);
	}	
	
	/**
	 * 插入入库单Excel导入主表
	 * @param map
	 * @return 
	 */
	public int insertProductInDepotExcel(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTIOS09.insertProductInDepotExcel");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 插入入库单Excel导入明细表
	 * @param map
	 */
	public void insertProductInDepotDetailExcel(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTIOS09.insertProductInDepotDetailExcel");
		baseServiceImpl.save(map);
	}
	
	public int insertImportBatch(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTIOS09.insertImportBatch");
		return baseServiceImpl.saveBackId(map);
	}
}
