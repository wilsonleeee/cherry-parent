/*	
 * @(#)BINOLPTJCS02_Service.java     1.0 2012-7-25 		
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

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 	产品扩展属性详细Service
 * 
 * @author jijw
 * @version 1.0 2012-7-25
 */
@SuppressWarnings("unchecked")
public class BINOLPTJCS02_Service extends BaseService {
	
	/**
	 * 取得产品扩展属性List
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getProductExtPropertyList (Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS02.getProductExtPropertyList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 根据groupId取得产品扩展属性List
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getProductExtPropertyListByGroupId (Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS02.getProductExtPropertyListByGroupId");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 查询产品扩展属性总数
	 * 
	 * @param map 查询条件
	 * @return int
	 */
	public int getProductExtPropertyCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS02.getProductExtPropertyCount");
		return baseConfServiceImpl.getSum(parameterMap);
	}
	
	/**
	 * 更新产品扩展属性
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public int updateProductExtProperty(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS02.updateProductExtProperty");
		return (Integer)baseServiceImpl.update(map);
	}
	
	/**
	 * 增加产品扩展属性
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public void insertProductExtProperty(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS02.insertProductExtProperty");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 停用或启用产品扩展属性
	 * 
	 * @param map
	 * @return
	 */
	public int disOrEnableExtProp(Map<String,Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS02.disOrEnableExtProp");
		return (Integer)baseServiceImpl.update(map); 
	}
	
	/*** ############################################### 产品预设值 start   ###############################################***/
	
	/**
	 * 取得产品扩展属性预设值List
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getProductExtDefValueList (Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS02.getProductExtDefValueList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 删除产品扩展预设值（根据产品扩展属性ID）
	 * @param map
	 */
	public void deleteExtDefVal(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS02.deleteExtDefVal");
		baseServiceImpl.remove(map);
	}
	
	/**
	 * 删除产品扩展属性预设值(checkbox)
	 * @param map
	 */
	public void deleteExtDefValForChk(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS02.deleteExtDefValForChk");
		baseServiceImpl.remove(map);
	}
	
	/**
	 * 新增产品扩展预设值
	 * @param map
	 */
	public void insertExtDefVal(List<Map<String,Object>> list) {
		baseServiceImpl.saveAll(list, "BINOLPTJCS02.insertExtDefVal");
	}
	
	/**
	 * 批量更新产品扩展属性预设值
	 * @param list
	 */
	public void updateProductExtPropertyBath(List<Map<String,Object>> list){
		baseServiceImpl.updateAllObjs(list, "BINOLPTJCS02.updateProductExtProperty");
	}
	
	/**
	 * 取得产品扩展属性预设值List（checkbox）
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getProductExtDefValueListForChk (Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTJCS02.getProductExtDefValueListForChk");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 增加产品扩展属(checkbox)
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public void insertExtDefValForChk(List<Map<String,Object>> list) {
		baseServiceImpl.saveAll(list, "BINOLPTJCS02.insertExtDefValForChk");
	}
	
	/***###############################################  产品预设值 end   ###############################################***/

}
