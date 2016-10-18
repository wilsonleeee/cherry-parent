/*  
 * @(#)BINOLMOCIO07_Service.java     1.0 2011/05/31      
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
package com.cherry.mo.cio.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class BINOLMOCIO07_Service extends BaseService{
	
	/**
	 * 取得主查询销售目标LIST的总数
	 * 
	 * @param map
	 * @return
	 */
	public int getSaleTargetCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO07.getSaleTargetCount");
		return baseServiceImpl.getSum(paramMap);
	}

	/**
	 * 取得主查询销售目标List
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> searchSaleTargetList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO07.searchSaleTargetList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 插入或者更新销售目标
	 * @param map
	 */
	public void mergeSaleTarget(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO07.mergeSaleTarget");
		baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 取得品牌CODE
	 * 
	 * @param brandInfoId
	 * @return
	 */
	public String getBrandCode(String brandInfoId) {
		return (String) baseServiceImpl.get(brandInfoId, "BINOLMOCIO07.getBrandCode");
	}
	
	/**
	 * 取得需要下发的销售目标【此处为有设置目标的单位】List
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> searchDownList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO07.searchDownList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 取得单位名称与CODE
	 * @param map
	 * @return
	 */
	public Map<String, Object> searchParameter(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO07.searchParameter");
		return (Map<String, Object>)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 取得excel中code或者名称对应的区域、柜台、营业员的id
	 * @param map
	 * @return
	 */
	public Map<String, Object> searchParameterID(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO07.searchParameterID");
		List<Map<String, Object>> list = baseServiceImpl.getList(parameterMap);
		return (list != null && list.size()>0) ? list.get(0) : null;
	}
	
	/**
	 * 供生成树节点使用
	 * 
	 * */
	public List<Map<String,Object>> getTreeNodes(Map<String,Object> map){
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO07.getTreeNodes");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 更新下发数据
	 * 
	 * @param map
	 * @return
	 */
	public void downUpdate(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO07.downUpdate");
        baseServiceImpl.save(paramMap);
	}
	
	/**
	 * 根据活动名称取得活动CODE
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getActivityInfoByName(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO07.getActivityInfoByName");
		return baseServiceImpl.getList(paramMap);
	}
	
}
