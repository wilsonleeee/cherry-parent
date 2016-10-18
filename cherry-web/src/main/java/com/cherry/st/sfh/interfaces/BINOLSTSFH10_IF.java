/*  
 * @(#)BINOLPTODR01_IF.java    1.0 2011-8-9     
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
package com.cherry.st.sfh.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

public interface BINOLSTSFH10_IF extends ICherryInterface {

	/**
	 * 往柜台产品订货参数表中插入数据
	 * 
	 * 
	 * */
	public void tran_setCouPrtParameter(Map<String,Object> map) throws Exception;
	
	/**
	 * 设置全局订货参数
	 * @param map
	 * @throws Exception
	 */
	public void tran_setGlobalParameter(Map<String, Object> map) throws Exception;
	
	/**
	 * 往产品订货参数表中插入数据
	 * 
	 * 
	 * */
	public void tran_setProductParameter(Map<String,Object> map,String date,List<Map<String,Object>> list) throws Exception;
	
	/**
	 * 往柜台订货参数表中插入数据
	 * 
	 * 
	 * */
	public void tran_setCounterParameter(Map<String,Object> map,List<Map<String,Object>> list) throws Exception;	
	
	/**
	 * 编辑柜台产品订货参数
	 * 
	 * 
	 * */
	public void tran_editCouPrtParameter(Map<String,Object> map) throws Exception;
	
	/**
	 * 编辑全局订货参数
	 * @param map
	 * @throws Exception
	 */
	public void tran_editGlobalParameter(Map<String,Object> map) throws Exception;
	
	/**
	 * 编辑产品订货参数
	 * 
	 * 
	 * */
	public void tran_editProductParameter(Map<String,Object> map) throws Exception;
	
	/**
	 * 编辑柜台订货参数
	 * 
	 * 
	 * */
	public void tran_editCounterParameter(Map<String,Object> map) throws Exception;
	
	/**
	 * 获取柜台订货参数list
	 * 
	 * 
	 * */
	public List<Map<String,Object>> getCounterParameterList(Map<String,Object> map);
	
	/**
	 * 获取柜台订货参数总数
	 * 
	 * 
	 * */
	public int getCounterParameterCount(Map<String,Object> map);
	
	/**
	 * 获取产品订货参数List
	 * 
	 * 
	 * 
	 * */
	public List<Map<String,Object>> getProductParameterList(Map<String,Object> map,String date);
	
	/**
	 * 获取产品订货参数总数
	 * 
	 * 
	 * */
	public int getProductParameterCount(Map<String,Object> map,String date);
	
	/**
	 * 获取柜台产品订货参数List
	 * 
	 * 
	 * */
	public List<Map<String,Object>> getCouPrtParameterList(Map<String,Object> map);
	
	/**
	 * 获取柜台产品订货参数总数
	 * 
	 * 
	 * */
	public int getCouPrtParameterCount(Map<String,Object> map);
	
	/**
	 * 获取全局订货参数List
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getGlobalParameterList(Map<String, Object> map);
	
	/**
	 * 获取全局订货参数总数
	 * @param map
	 * @return
	 */
	public int getGlobalParameterCount(Map<String, Object> map);
	
	/**
	 * 获取下发数据
	 * 
	 * 
	 * */
	public void issumOrderParam(Map<String ,Object> map)throws Exception;
	
	/**
	 * 获取产品的树节点
	 * 
	 * */
	public List<Map<String,Object>> getProductTreeNodes(Map<String,Object> map) throws Exception;
	
	/**
	 * 获取柜台树节点
	 * 
	 * */
	public List<Map<String,Object>> getCounterTreeNodes(Map<String,Object> map) throws Exception;
}
