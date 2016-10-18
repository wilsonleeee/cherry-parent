/*  
 * @(#)BINOLBSRES04_IF.java     1.0 2011/05/31      
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
package com.cherry.bs.res.interfaces;

import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

public interface BINOLBSRES04_IF extends ICherryInterface{
	
	public int tran_insertRes(Map<String, Object> map);
	

	public String getCount(Map<String, Object> map);

}
