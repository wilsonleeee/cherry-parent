/*	
 * @(#)BINOLCM99_Service     1.0 2011/10/20	
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
package com.cherry.cm.cmbussiness.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.ConvertUtil;

/**
 * 报表导出共通Service
 * 
 * @author lipc
 * 
 */
public class BINOLCM99_Service extends BaseService {
	/**
	 * 取得报表数据List
	 * 
	 * @param pramMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getDataList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>(map);
		String pageId = ConvertUtil.getString(map.get(CherryConstants.PAGE_ID));
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM99.get"
				+ pageId.toUpperCase() + "List");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 取得待打印单据主数据
	 * 现支持显示支付方式，故主数据不再只是单条数据【包含了支付明细】
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getMainData(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>(map);
		String printType=ConvertUtil.getString(map.get("printType"));
		String pageId = ConvertUtil.getString(map.get(CherryConstants.PAGE_ID));
		if("2".equals(printType)){
			if (pageId.toUpperCase().equals("BINOLWPSAL07")){
				parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM99.get"
						+ pageId.toUpperCase() + "Map2");
			}else {
				parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM99.get"
						+ pageId.toUpperCase() + "Map");
			}			
		}else{
			if (pageId.toUpperCase().equals("BINOLWPSAL07")){
				parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM99.get"
						+ pageId.toUpperCase() + "Map1");
			}else {
				parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM99.get"
						+ pageId.toUpperCase() + "Map");
			}			
		}
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 插入报表打印记录表
	 * 
	 * @param map
	 */
	public void insertPrintLog(Map<String, Object> map){
		baseServiceImpl.save(map, "BINOLCM99.insertPrintLog");
	}
	
	/**
	 * 取得报表打印记录列表
	 * 
	 * @param pramMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getLogList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>(map);
		String pageId = ConvertUtil.getString(map.get(CherryConstants.PAGE_ID));
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM99.get"
				+ pageId.toUpperCase() + "LogList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 取得报表打印记录列表
	 * 
	 * @param pramMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getProductList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM99.getProductList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 根据输入的字符串模糊查询出对应的产品信息
	 * 
	 * */
	public List<Map<String,Object>> getProductInfoList(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM99.getProductInfoList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 检查柜台是否有分配可用的方案
	 * 
	 * */
	public List<Map<String,Object>> chkCntSoluData(Map<String,Object> map){
		
		return baseServiceImpl.getList(map, "BINOLCM99.chkCntSoluData");
	}
	
	/**
	 * 根据输入的字符串模糊查询出柜台对应的产品信息
	 * 
	 * */
	public List<Map<String,Object>> getCntProductList(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM99.getCntProductList");
		return baseServiceImpl.getList(map);
	}
}
