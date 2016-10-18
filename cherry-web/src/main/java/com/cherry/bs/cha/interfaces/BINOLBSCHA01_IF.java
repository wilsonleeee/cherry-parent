/*  
 * @(#)BINOLBSCHA01_IF.java     1.0 2011/05/31      
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
package com.cherry.bs.cha.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.ICherryInterface;

public interface BINOLBSCHA01_IF extends ICherryInterface{


	public int searchChannelCount(Map<String, Object> map);
	
	public List<Map<String, Object>> searchChannelList(Map<String, Object> map);
	
    public int tran_enableChannel(Map<String, Object> map) throws CherryException;
    
    public int tran_disableChannel(Map<String, Object> map) throws CherryException;
}
