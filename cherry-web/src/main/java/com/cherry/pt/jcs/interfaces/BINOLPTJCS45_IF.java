/*  
 * @(#)BINOLPTJCS18_IF.java     1.0 2014/08/31      
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

import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.ICherryInterface;
import com.googlecode.jsonplugin.JSONException;

public interface BINOLPTJCS45_IF extends ICherryInterface{


	public int searchSolutionCount(Map<String, Object> map);
	
	public List<Map<String, Object>> searchSolutionList(Map<String, Object> map);
	
    public int tran_disOrEnableSolu(Map<String, Object> map) throws JSONException, CherryException, Exception;
    
}
