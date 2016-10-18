/*  
 * @(#)BINOLSTSFH02_IF.java     1.0 2011/05/31      
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

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.ICherryInterface;

public interface BINOLSTSFH02_IF extends ICherryInterface{
	
	public int searchProductOrderCount(Map<String, Object> map);
	
	public List<Map<String, Object>> searchProductOrderList(Map<String, Object> map);
	
	public void tran_test(Map<String, Object> map,List<Map<String, Object>> list,UserInfo userinfo)throws Exception;
	
	public void tran_doaction(String entryID,String actionID,UserInfo userinfo)throws Exception;
	
	public Map<String, Object> getSumInfo(Map<String, Object> map);
	
	/**
	 * 获取导出Excel
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public byte[] exportExcel(Map<String, Object> map) throws Exception; 
}
