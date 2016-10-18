/*  
 * @(#)BINOLMOCIO15_IF.java     1.0 2011/05/31      
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
package com.cherry.mo.cio.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.ICherryInterface;

public interface BINOLMOCIO15_IF extends ICherryInterface{

	public int searchRivalCount(Map<String, Object> map);
	
	public List<Map<String, Object>> searchRivalList(Map<String, Object> map);
	
	public void tran_addRival(Map<String, Object> map)throws CherryException;
	
	public void tran_updateRival(Map<String, Object> map) throws Exception;
	
	public void tran_deleteRival(Map<String, Object> map) throws Exception;
	
	public String getCount(Map<String, Object> map);
}
