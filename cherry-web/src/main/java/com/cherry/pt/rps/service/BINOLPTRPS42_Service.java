/*	
 * @(#)BINOLPTRPS42_Service.java     1.0 2011/03/16		
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
package com.cherry.pt.rps.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 预付单查询service
 * 
 * @author nanjunbo
 * @version 1.0 2016.07.15
 *
 */
@SuppressWarnings("unchecked")
public class BINOLPTRPS42_Service extends BaseService
{

	/**
	 * 获取产品销售记录总数
	 * @param map
	 * @return int
	 * 
	 * */
	
	public int getPreInfoCount(Map<String, Object> map)
	{
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS42.getPreInfoCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 获取产品销售记录LIST
	 * @param map
	 * @return list
	 * 
	 * */
	
	public List<Map<String,Object>> getPreInfoList(Map<String,Object> map)
	{
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS42.getPreInfoList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得销售总金额与总数量及销售记录单数量
	 * 
	 * */
	public Map<String,Object> getSumPreInfo(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS42.getSumPreInfo");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	
	/**
	 * 获取预付单明细
	 * @param map
	 * @return
	 */
	public Map<String,Object> getpreInfoMap(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS42.getpreInfoMap");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	
	/**
	 * 预付单详情list
	 * 
	 * */
	public List<Map<String,Object>> getPreDetailInfoList(Map<String,Object> map){
		return baseServiceImpl.getList(map,"BINOLPTRPS42.getPreDetailInfoList");
	}
	
	/**
	 * 提货单详情list
	 * 
	 * */
	public List<Map<String,Object>> getPickDetailInfoList(Map<String,Object> map){
		return baseServiceImpl.getList(map,"BINOLPTRPS42.getPickDetailInfoList");
	}
	
	
	/**
	 * 获取产品销售记录明细总数
	 * @param map
	 * @return int
	 * 
	 * */
	public int getExportDetailCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS42.getExportDetailCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 取得Excel导出的销售记录明细List
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getExportDetailList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS42.getExportDetailList");
		return baseServiceImpl.getList(map);
	}
	

	/**
	 * 取得支付方式
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getPayTypeList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS42.getPayTypeList");
		return baseServiceImpl.getList(map);
	}
	
}
