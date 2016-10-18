/*		
 * @(#)BINOLCM07_BL.java     1.0 2010/12/08		
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

package com.cherry.cm.cmbussiness.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.service.BINOLCM07_Service;
import com.cherry.cm.core.CherryConstants;

/**
 * 弹出datatable 共通
 * @author dingyc
 *
 */
@SuppressWarnings("unchecked")
public class BINOLCM07_BL {
	
	@Resource
	private BINOLCM07_Service binOLCM07_Service;
	
	/**
	 * 取得促销产品信息
	 * @param map
	 * @return
	 */
	public HashMap getPromotionDialogInfoList (Map<String, Object> map){
		HashMap resultMap = new HashMap();
		List filterList = new ArrayList();
		// 需要过滤的字段名
		filterList.add("unitCode");
		filterList.add("barCode");
		filterList.add("nameTotal");
		filterList.add("nameForeign");
		filterList.add("salePrice");
		map.put(CherryConstants.FILTER_LIST_NAME, filterList);
		// 取得件数
		int count = binOLCM07_Service.getPromotionInfoCount(map);
		// 取得促销信息List
		List popPrmProductInfoList = binOLCM07_Service.getPromotionShortInfoList(map);
		resultMap.put(CherryConstants.POP_PRMPRODUCT_LIST, popPrmProductInfoList);
		resultMap.put("count", count);
		return resultMap;
	}
	
	/**
	 * 设定组织部门类型
	 * @param map
	 */
	public void setType (Map<String, Object> map){
		// 所属部门类型
		String departType =(String)map.get("departType");
		if ("0".equals(departType) || "1".equals(departType)){
			map.put("type","0");
		}else{
			map.put("type","1");
		}
	}
	
	/**
	 * 取得柜台总数
	 * 
	 * @param map 查询条件
	 * @return 返回柜台总数
	 */
	public int getCounterInfoCount(Map<String, Object> map) {
		
		// 取得柜台总数
		return binOLCM07_Service.getCounterInfoCount(map);
	}
	
	/**
	 * 取得柜台List
	 * 
	 * @param map 查询条件
	 * @return 柜台信息List
	 */
	public List<Map<String, Object>> getCounterInfoList(Map<String, Object> map) {
		
		// 取得柜台List
		return binOLCM07_Service.getCounterInfoList(map);
	}
	
	/**
	 * 根据柜台ID取得柜台信息
	 * 
	 * @param map 查询条件
	 * @return 柜台信息
	 */
	public Map<String, Object> getCounterInfo(Map<String, Object> map) {
		
		// 根据柜台ID取得柜台信息
		return binOLCM07_Service.getCounterInfo(map);
	}
	
	
	/**
	 * 取得厂商List
	 * 
	 * @param map 查询条件
	 * @return 厂商信息List
	 */
	public List getFactoryList(Map<String, Object> map) {
		
		// 取得厂商List
		return binOLCM07_Service.getFactoryList(map);
	}
	
	/**
	 * 查询某一区域的所有上级区域
	 * 
	 * @param map 查询条件
	 * @return 区域List
	 */
	public List getHigherRegionList(Map<String, Object> map) {
		
		// 查询某一区域的所有上级区域
		return binOLCM07_Service.getHigherRegionList(map);
	}
	
	/**
	 * 取得部门(办事处或柜台)总数
	 * 
	 * @param map 查询条件
	 * @return 返回柜台总数
	 */
	public int getDepartInfoCount(Map<String, Object> map) {
		
		// 取得部门(办事处或柜台)总数
		return binOLCM07_Service.getDepartInfoCount(map);
	}
	
	/**
	 * 取得部门(办事处或柜台)List
	 * 
	 * @param map 查询条件
	 * @return 部门(办事处或柜台)List
	 */
	public List getDepartInfoList(Map<String, Object> map) {
		
		// 取得部门(办事处或柜台)List
		return binOLCM07_Service.getDepartInfoList(map);
	}
}
