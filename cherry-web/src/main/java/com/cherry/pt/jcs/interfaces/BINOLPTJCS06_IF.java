/*  
 * @(#)BINOLPTJCS06_IF.java     1.0 2011/05/31      
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

public interface BINOLPTJCS06_IF extends ICherryInterface{
	/**
	 * 取得产品详细信息
	 * @param map
	 * @return
	 * 
	 * */
	public Map<String,Object> getDetail(Map<String,Object> map);
	
	/**
	 * 取得产品条码List
	 * @param map
	 * @return list
	 * 
	 * */
	public List<Map<String,Object>> getBarCodeList(Map<String,Object> map);
	
	/**
	 * 获取产品分类List
	 * @param map
	 * @return list
	 * 
	 * */
	public List<Map<String,Object>> getCateList(Map<String,Object> map);
	
	/**
	 * 获取产品销售价格List
	 * @param map
	 * @return list
	 * 
	 * */
	public List<Map<String,Object>> getSellPriceList(Map<String,Object> map);
	
	/**
	 * 获取产品图片List
	 * @param map
	 * @return list
	 * 
	 * */
	public List<Map<String,Object>> getImgList(Map<String,Object> map);
	
	/**
	 * 获取产品BOM组件List
	 * @param map
	 * @return list
	 * 
	 * */
	public List<Map<String,Object>> getBOMList(Map<String,Object> map);
	
	/**
     * 取得产品编码条码修改履历
     * 
     * @param map
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPrtBCHistoryList(Map<String, Object> map);
}
