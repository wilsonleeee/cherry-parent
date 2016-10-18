/*  
 * @(#)BINOLSTCM05_IF.java    1.0 2011.09.02
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
package com.cherry.st.common.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

/**
 * 
 * 退库共通
 * 
 * @author zhanghuyi
 * @version 1.0 2011.10.21
 */
public interface BINOLSTCM09_IF extends ICherryInterface{

	
	/**
	 * 
	 *将退库单信息写入退库单据主从表
	 * 
	 *@param mainData
	 *@param detailList
	 */
	public int insertProductReturnAll(Map<String,Object> mainData,List<Map<String,Object>> detailList);
	
	/**
	 * 
	 *修改退库单据主表数据
	 * 
	 *@param praMap
	 */
	public int updateProductReturnMain(Map<String,Object> praMap);
	
	/**
	 * 
	 *根据退库单据表来向【入出库记录主表】和
	 *【入出库记录明细表】中插入数据，并修改库存。
	 * 
	 *@param praMap
	 */
	public void changeStock(Map<String,Object> praMap);
	
	/**
	 * 
     *给定退库单主ID，取得概要信息。
	 * 
	 *@param productReturnID
	 */
	public Map<String,Object> getProductReturnMainData(int productReturnID,String language);
	
	/**
	 * 
	 *给定入出库单主ID，取得明细信息。
	 * 
	 *@param productReturnID
	 */
	public List<Map<String,Object>> getProductReturnDetailData(int productReturnID,String language);
}
