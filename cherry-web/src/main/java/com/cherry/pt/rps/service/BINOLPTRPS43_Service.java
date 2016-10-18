/*	
 * @(#)BINOLPTRPS43_Service.java     1.0 2011/03/16		
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
 * 销售记录查询一览service
 * 
 * @author zgl
 * @version 1.0 2011.03.16
 *
 */
@SuppressWarnings("unchecked")
public class BINOLPTRPS43_Service extends BaseService
{

	/**
	 * 获取产品销售记录总数
	 * @param map
	 * @return int
	 * 
	 * */
	
	public int getSaleRecordCount(Map<String, Object> map)
	{
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS43.getSaleRecordCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 获取产品销售记录LIST
	 * @param map
	 * @return list
	 * 
	 * */
	
	public List<Map<String,Object>> getSaleRecordList(Map<String,Object> map)
	{
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS43.getSaleRecordList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得销售总金额与总数量及销售记录单数量
	 * 
	 * */
	public List<Map<String,Object>> getSumInfo(Map<String,Object> map){
		return baseServiceImpl.getList(map,"BINOLPTRPS43.getSumInfo");
	}
	
	/**
	 * 各商品统计详细（各商品的总数量及总金额）
	 * 
	 * */
	public List<Map<String,Object>> getSaleProPrmList(Map<String,Object> map){
		return baseServiceImpl.getList(map,"BINOLPTRPS43.getSaleProPrmList");
	}
	
	/**
	 * 取得销售商品信息
	 * @param map
	 * @return List
	 */
	public List indSearchPrmPrt(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS43.getPrmPrtList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 获取产品销售记录明细总数
	 * @param map
	 * @return int
	 * 
	 * */
	public int getExportDetailCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS43.getExportDetailCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 取得Excel导出的销售记录明细List
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getExportDetailList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS43.getExportDetailList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 获取产品销售记录明细总数（过滤明细中的快递费信息）
	 * @param map
	 * @return int
	 * 
	 * */
	public int getExportDetailFilterKDCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS43.getExportDetailFilterKDCount");
		return baseServiceImpl.getSum(paramMap);
	}
	
	/**
	 * 取得Excel导出的销售记录明细List(过滤快递费明细)
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getExprotDetailListFilterKD(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS43.getExprotDetailListFilterKD");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 取得Excel导出的销售记录一览List（海明威）
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getExportSaleRecordList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS43.getExportSaleRecordList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 取得支付方式
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getPayTypeList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS43.getPayTypeList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 更新发票类型
	 * @param list
	 * 
	 */
	public void updateInvoiceFlag(List<Map<String, Object>> list) {
		baseServiceImpl.updateAll(list,"BINOLPTRPS43.updateInvoiceFlag");
	}
}
