/*  
 * @(#)BINOLPTJCS19_Service.java     1.0 2014/08/15      
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BaseServiceImpl;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.ConvertUtil;

public class BINOLPTJCS19_Service extends BaseService{
	
	@SuppressWarnings("unchecked")
	public Map<String,Object> getSolutionInfo(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS19.getSolutionInfo");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	
	/**
	 * 取得方案明细Count
	 * @param map
	 * @return
	 */
	public int getSolutionInfoDetailCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS19.getSolutionInfoDetailCount");
		return ConvertUtil.getInt(baseServiceImpl.get(map));
	}
	
	/**
	 * 取得方案明细List
	 * 
	 * @param map
	 * @return
	 */
	public List getSolutionInfoDetailList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS19.getSolutionInfoDetailList");
		return baseServiceImpl.getList(map);
	}
	
	// **************************************************  颖通产品方案明细维护  *****************************************************************************//
	// 颖通需求：颖通的需求是不同的柜台销售不同产品，但销售的价格是相同的
	
	/**
	 * 取得产品方案明细表的产品与以前配置的差异List
	 * @param map
	 * @return
	 */
	public List getPrtForPrtSoluDetailDiff(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS19.getPrtForPrtSoluDetailDiff");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * merge产品方案明细表  
	 * @param map
	 * @return 
	 */
	public String mergeProductPriceSolutionDetail(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS19.mergeProductPriceSolutionDetail");
		return String.valueOf(baseServiceImpl.get(map));
	}
	
	/**
	 * 将产品方案明细表的数据无效 
	 * @param map
	 * @return
	 */
	public int updPrtSoluDetail(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS19.updPrtSoluDetail");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 获取产品分类List
	 * @param map
	 * @return list
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getCateList(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS19.getCateList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 插入方案明细添加产品分类 
	 * 
	 * @param map
	 * @return int
	 */
	public int insertCate(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS19.insertCate");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 删除产品分类
	 * @param map
	 * @return
	 * 
	 * */
	public int delPrtSoluDetailCate(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS19.delPrtSoluDetailCate");
		return baseServiceImpl.remove(map);
	}
	
	/**
	 *  更新产品方案明细表的产品价格为最新的产品方案价格    
	 * @param map
	 * @return 
	 */
	public List<Map<String,Object>> mergePPSDPrice(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS19.mergePPSDPrice");
		return baseServiceImpl.getList(map);
	}
	
}