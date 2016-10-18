/*  
 * @(#)BINOLPTJCS19_IF.java     1.0 2014/08/31      
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
package com.cherry.pt.jcs.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.ICherryInterface;
import com.cherry.pt.jcs.form.BINOLPTJCS38_Form;
import com.googlecode.jsonplugin.JSONException;

public interface BINOLPTJCS39_IF extends ICherryInterface{
	
	public Map<String, Object> getPrtFunInfo(Map<String, Object> map);
	
	/**
	 * 取得方案明细Count
	 * 
	 * @param map
	 * @return
	 */
	public int getPrtFunInfoDetailCount(Map<String, Object> map);
	
	/**
	 * 取得方案明细List
	 * 
	 * @param map
	 * @return
	 */
	public List getPrtFunInfoDetailList(Map<String, Object> map);
	
	/**
	 * 保存添加的方案产品明细
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> tran_addPrtFunDetail(Map<String, Object> map,BINOLPTJCS38_Form form)  throws JSONException,Exception;
	
	/**
	 * 保存编辑的方案产品明细
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> tran_editPrtPriceSoluDetail(Map<String, Object> map,BINOLPTJCS38_Form form)  throws JSONException,Exception;
	
	/**
	 * 批量无效方案产品明细
	 * @param map
	 * @throws Exception
	 */
	public void tran_delPrtPriceSoluDetail(Map<String, Object> map) throws Exception;
	
	public Map<String, Object> ResolveExcel(Map<String, Object> map)
			throws Exception;
	
	public Map<String, Object> tran_excelHandle(
			Map<String, Object> importDataMap, Map<String, Object> map)
			throws Exception;	
	
	// **************************************************  颖通产品方案明细维护  *****************************************************************************//
	// 颖通需求：颖通的需求是不同的柜台销售不同产品，但销售的价格是相同的
	/**
	 * 产品方案明细添加产品分类
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> tran_addPrtPriceSoluCateDetail(Map<String, Object> map,BINOLPTJCS38_Form form)  throws JSONException,Exception;	
	
	/**
	 * 获取产品分类List
	 * 
	 * @param map
	 * @return list
	 * 
	 * */
	public List<Map<String, Object>> getCateList(Map<String, Object> map);
	
}
