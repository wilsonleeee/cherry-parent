/*  
 * @(#)BINOLSTSFH10_Service.java    1.0 2011-8-9     
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

package com.cherry.st.sfh.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

@SuppressWarnings("unchecked")
public class BINOLSTSFH10_Service extends BaseService {

	/**
	 * 往柜台产品订货参数表中插入数据
	 * 
	 * 
	 * */
	public void setCouPrtParameter(List<Map<String,Object>> list){
		baseServiceImpl.saveAll(list, "BINOLSTSFH10.setCouPrtParameter");
	}
	
	/**
	 * 往全局订货参数表中插入数据
	 * @param map
	 */
	public void setGlobalParameter(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH10.setGlobalParameter");
		baseServiceImpl.save(paramMap);
	}
	
	/**
	 * 往产品订货参数表中插入数据
	 * 
	 * 
	 * */
	public void setProductParameter(List<Map<String,Object>> list){
		baseServiceImpl.saveAll(list, "BINOLSTSFH10.setProductParameter");
	}
	
	/**
	 * 往柜台订货参数表中插入数据
	 * 
	 * 
	 * */
	public void setCounterParameter(List<Map<String,Object>> list){
		baseServiceImpl.saveAll(list, "BINOLSTSFH10.setCounterParameter");
	}
	
	/**
	 * 根据品牌（柜台）删除柜台产品订货参数表中的数据
	 * 
	 * 
	 * */
	public void deleteCouPrtParameter(List<Map<String,Object>> list){
		baseServiceImpl.deleteAll(list, "BINOLSTSFH10.deleteCouPrtParameter");
	}
	
	/**
	 * 根据品牌删除全局订货参数表中的数据
	 * @param map
	 */
	public void deleteGlobalParameter(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH10.deleteGlobalParameter");
		baseServiceImpl.remove(paramMap);
	}
	
	/**
	 * 根据品牌删除柜台订货参数表中的数据
	 * 
	 * 
	 * */
	public void deleteCounterParameter(List<Map<String,Object>> list){
		baseServiceImpl.deleteAll(list, "BINOLSTSFH10.deleteCounterParameter");
	}
	
	/**
	 * 根据品牌删除产品订货参数表中的数据
	 * 
	 * */
	public void deleteProductParameter(List<Map<String,Object>> list){
		baseServiceImpl.deleteAll(list, "BINOLSTSFH10.deleteProductParameter");
	}
	
	/**
	 * 编辑柜台产品订货参数
	 * 
	 * */
	public void editCouPrtParameter(Map<String,Object> map){
		baseServiceImpl.update(map,"BINOLSTSFH10.editCouPrtParameter");
	}
	
	/**
	 * 编辑产品订货参数
	 * 
	 * */
	public void editProductParameter(Map<String,Object> map){
		baseServiceImpl.update(map,"BINOLSTSFH10.editProductParameter");
	}
	
	/**
	 * 编辑产品订货参数
	 * 
	 * */
	public void editGlobalParameter(Map<String,Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH10.editGlobalParameter");
		baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 编辑柜台订货参数
	 * 
	 * */
	public void editCounterParameter(Map<String,Object> map){
		baseServiceImpl.update(map,"BINOLSTSFH10.editCounterParameter");
	}
	
	/**
	 * 取得产品信息
	 * 
	 * */
	public List<Map<String,Object>> getProductInformation(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH10.getProductInformation");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得柜台信息
	 * 
	 * */
	public List<Map<String,Object>> getCounterInformation(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH10.getCounterInformation");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 获取柜台订货参数list
	 * 
	 * */
	public List<Map<String,Object>> getCounterParameterList(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH10.getCounterParameterList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 获取柜台订货参数总数
	 * 
	 * */
	public int getCounterParameterCount(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH10.getCounterParameterCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 获取产品订货参数List
	 * 
	 * */
	public List<Map<String,Object>> getProductParameterList(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH10.getProductParameterList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 获取产品订货参数总数
	 * 
	 * */
	public int getProductParameterCount(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH10.getProductParameterCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 获取柜台产品订货参数List
	 * 
	 * */
	public List<Map<String,Object>> getCouPrtParameterList(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH10.getCouPrtParameterList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 获取柜台产品订货参数总数
	 * 
	 * */
	public int getCouPrtParameterCount(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH10.getCouPrtParameterCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 获取全局订货参数List
	 * 
	 * */
	public List<Map<String,Object>> getGlobalParameterList(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH10.getGlobalParameterList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 获取全局订货参数总数
	 * 
	 * */
	public int getGlobalParameterCount(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH10.getGlobalParameterCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 获取柜台订货参数，供下发时使用
	 * 
	 * */
	public List<Map<String,Object>> getIssCouParamList(Map<String,Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH10.getIssCouParamList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 获取产品订货参数，供下发时使用
	 * 
	 * */
	public List<Map<String,Object>> getIssPrtParamList(Map<String,Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH10.getIssPrtParamList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 获取柜台产品订货参数，供下发时使用
	 * 
	 * */
	public List<Map<String,Object>> getIssCouPrtParamList(Map<String,Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTSFH10.getIssCouPrtParamList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 获取品牌Code
	 * 
	 * 
	 * */
	public String getBrandCode(Map<String,Object> map){
		return (String) baseServiceImpl.get(map, "BINOLSTSFH10.getBrandCode");
	}
	
	/**
	 * 获取所有的产品及其类别，供生成树节点使用
	 * 
	 * */
	public List<Map<String,Object>> getPrtAndCatList(Map<String,Object> paramMap){
		return baseServiceImpl.getList(paramMap,"BINOLSTSFH10.getPrtAndCatList");
	}
	
	/**
	 * 取得产品类别树形显示顺序
	 * 
	 * */
	public List<Map<String,Object>> getCatPropListSeq(Map<String,Object> paramMap){
		return baseServiceImpl.getList(paramMap,"BINOLSTSFH10.getCatPropListSeq");
	}
	
	/**
	 * 取得柜台及其所属部门list，供树形显示使用
	 * 
	 * */
	public List<Map<String,Object>> getCuntAndDeptList(Map<String,Object> paramMap){
		return baseServiceImpl.getList(paramMap,"BINOLSTSFH10.getCuntAndDeptList");
	}
}
