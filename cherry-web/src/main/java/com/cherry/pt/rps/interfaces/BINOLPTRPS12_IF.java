/*  
 * @(#)BINOLPTRPS12_IF.java     1.0 2011/05/31      
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
package com.cherry.pt.rps.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.ICherryInterface;


public interface BINOLPTRPS12_IF extends ICherryInterface{
	
	/**
	 * 取得产品信息
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getProProduct(Map<String, Object> map);
	
	/**
	 * 取得产品库存详细
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getProStockDetails(Map<String, Object> map);
	
	/**
	 * 取得产品库存出入明细单据
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getdetailed(Map<String, Object> map);
}
