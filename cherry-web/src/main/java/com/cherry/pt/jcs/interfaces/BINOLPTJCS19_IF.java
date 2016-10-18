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
import com.cherry.pt.jcs.form.BINOLPTJCS18_Form;
import com.googlecode.jsonplugin.JSONException;

public interface BINOLPTJCS19_IF extends ICherryInterface{
	
	public Map<String, Object> getSolutionInfo(Map<String, Object> map);
	
	/**
	 * 取得方案明细Count
	 * 
	 * @param map
	 * @return
	 */
	public int getSolutionInfoDetailCount(Map<String, Object> map);
	
	/**
	 * 取得方案明细List
	 * 
	 * @param map
	 * @return
	 */
	public List getSolutionInfoDetailList(Map<String, Object> map);
	
	/**
	 * 保存添加的方案产品明细
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> tran_addPrtPriceSoluDetail(Map<String, Object> map,BINOLPTJCS18_Form form)  throws JSONException,Exception;
	
	/**
	 * 保存编辑的方案产品明细
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> tran_editPrtPriceSoluDetail(Map<String, Object> map,BINOLPTJCS18_Form form)  throws JSONException,Exception;
	
	/**
	 * 批量无效方案产品明细
	 * @param map
	 * @throws Exception
	 */
	public void tran_delPrtPriceSoluDetail(Map<String, Object> map) throws Exception;
	
	// **************************************************  颖通产品方案明细维护  *****************************************************************************//
	// 颖通需求：颖通的需求是不同的柜台销售不同产品，但销售的价格是相同的
	/**
	 * 产品方案明细添加产品分类
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> tran_addPrtPriceSoluCateDetail(Map<String, Object> map,BINOLPTJCS18_Form form)  throws JSONException,Exception;	
	
	/**
	 * 获取产品分类List
	 * 
	 * @param map
	 * @return list
	 * 
	 * */
	public List<Map<String, Object>> getCateList(Map<String, Object> map);
	
}
