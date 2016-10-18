/*  
 * @(#)BINOLPTRPS25_IF.java     1.0.0 2011/10/17      
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

public interface BINOLPTRPS25_IF extends ICherryInterface{
	
	public int getPrtDeliverCount(Map<String, Object> map);
	
	public List<Map<String, Object>> getPrtDeliverList(Map<String, Object> map);
	
	public Map<String, Object> getSumInfo(Map<String, Object> map);
	
    /**
     * 获得导出Excel
     * 
     * @param dataList
     * @param map
     * @return
     * @throws Exception
     */
    public byte[] exportExcel(Map<String, Object> map) throws Exception;
}
