/*
 * @(#)BINOLSTIOS08_Service.java     1.0 2013/07/15
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

import java.util.List;
import java.util.Map;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.ConvertUtil;

/**
 * 
 * 入库excel导入详细
 * 
 * @author zhangle
 * @version 1.0 2013.07.15
 */
public class BINOLSTIOS08_Service extends BaseService{
	
	/**
	 * 查询入库单（Excel导入）信息
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getProductInDepotExcelList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTIOS08.getProductInDepotExcelList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 查询入库单（Excel导入）总数
	 * @param map
	 * @return
	 */
	public int getProductInDepotExcelCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTIOS08.getProductInDepotExcelCount");
		return ConvertUtil.getInt(baseServiceImpl.get(map));
	}
	
	/**
	 * 通过ID查询单条入库单（Excel导入）信息
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getProductInDepotExcelInfo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTIOS08.getProductInDepotExcelInfo");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}	
	
	/**
	 * 查询入库单明细（Excel导入）
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getProductInDepotExcelDetailList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTIOS08.getProductInDepotExcelDetailList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 查询导出数据信息
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getExportDataList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTIOS08.getExportDataList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 查询导出数据总数
	 * @param map
	 * @return
	 */
	public int getExportDataCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTIOS08.getExportDataCount");
		return ConvertUtil.getInt(baseServiceImpl.get(map));
	}
}
