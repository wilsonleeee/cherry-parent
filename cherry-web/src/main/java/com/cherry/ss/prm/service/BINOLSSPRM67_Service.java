/*
 * @(#)BINOLSSPRM67_Service.java     1.0 2013/09/17
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
import com.cherry.cm.util.ConvertUtil;

/**
 * 
 * 入库导入明细Service
 * 
 * @author zhangle
 * @version 1.0 2013.09.17
 */
public class BINOLSSPRM67_Service extends BaseService {
	/**
	 * 查询入库单（Excel导入）信息
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPrmInDepotExcelList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM67.getPrmInDepotExcelList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 查询入库单（Excel导入）总数
	 * @param map
	 * @return
	 */
	public int getPrmInDepotExcelCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM67.getPrmInDepotExcelCount");
		return ConvertUtil.getInt(baseServiceImpl.get(map));
	}
	
	/**
	 * 通过ID查询单条入库单（Excel导入）信息
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getPrmInDepotExcelInfobyId(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM67.getPrmDepotExcelInfoById");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}	
	
	/**
	 * 查询入库单明细（Excel导入）
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPrmInDepotExcelDetailList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM67.getPrmInDepotExcelDetailList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 查询导出数据信息
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getExportDataList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM67.getExportDataList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 查询导出数据总数
	 * @param map
	 * @return
	 */
	public int getExportDataCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM67.getExportDataCount");
		return ConvertUtil.getInt(baseServiceImpl.get(map));
	}
}