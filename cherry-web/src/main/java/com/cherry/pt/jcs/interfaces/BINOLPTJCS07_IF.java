/*  
 * @(#)BINOLPTJCS07_IF.java     1.0 2011/05/31      
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

public interface BINOLPTJCS07_IF extends ICherryInterface{
	/**
	 * 更新产品信息
	 * @param map
	 * @return
	 * 
	 * */
	public int tran_updProduct(Map<String,Object> map) throws Exception;
	
	/**
	 * 取得促销活动所用的产品件数(已下发)
	 * 
	 * @param map
	 */
	public int getActUsePrtCount(Map<String, Object> map);
	
	/**
	 * 取得促销活动所用的产品件数
	 * 
	 * @param map
	 */
	public int getActPrtCount(Map<String, Object> map);
	
	/**
	 * 更新产品条码对应关系信息
	 * @param map
	 */
	public void updPrtBarCode(Map<String, Object> map);

	/**
	 * 取得新添加的产品条码
	 *
	 * @param barCode
	 * @param allList
	 * @return
	 */
	public List<Map<String, Object>> getAddCodeList(
			List<Map<String, Object>> barCode, List<Map<String, Object>> allList);

	/**
	 * 取得更新产品条码
	 *
	 * @param barCode
	 * @return
	 */
	public List<Map<String, Object>> getUpdCodeList(
			List<Map<String, Object>> barCode);

	/**
	 *  查询该barCode在同一编码的所有产品中是否被使用过
	 * @param barCodeMap
	 * @return
	 */
	public Map<String, Object> getbarCodeUsedRecord(Map<String,Object> barCodeMap);
}
