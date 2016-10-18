/*  
 * @(#)BINOLSTIOS03_IF.java     1.0 2011/05/31      
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
package com.cherry.st.ios.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.ICherryInterface;

public interface BINOLSTIOS03_IF extends ICherryInterface{
	/**
	 * 保存移库记录
	 * 
	 * 
	 * */
	public int tran_save(Map<String,Object> map,List<String[]> list,UserInfo userinfo) throws Exception; 
	/**
	 * 根据产品条码以及厂商编码获取产品厂商ID以及对应的库存
	 * 
	 * */
	public Map<String,Object> getPrtVenIdAndStock(Map<String,Object> map);
	
	 public int getOrganIdByDepotID(Map<String,Object> map);
	 public String getDepart(Map<String, Object> map);
}
