/*  
 * @(#)BINOLPTRPS06_IF.java     1.0 2011/05/31      
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

public interface BINOLPTRPS06_IF extends ICherryInterface{
	
	public int getAllocationCount(Map<String, Object> map);
	
	public List<Map<String, Object>> getAllocationList(Map<String, Object> map);
	
	public Map<String, Object> getSumInfo(Map<String, Object> map);
}
