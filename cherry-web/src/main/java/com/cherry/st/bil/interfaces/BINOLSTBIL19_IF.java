/*  
 * @(#)BINOLSTBIL13_IF.java     1.0 2012/07/24   
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
package com.cherry.st.bil.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

/**
 * 退库申请查询Interface
 * 
 * @author niushunjie
 */
public interface BINOLSTBIL19_IF extends ICherryInterface{
    /**
     * 取得退库申请单总数
     * @param map
     * @return
     */
    public int searchSaleReturnRequestCount(Map<String, Object> map);
	
    /**
     * 取得退库申请单List
     * @param map
     * @return
     */
    public List<Map<String, Object>> searchSaleReturnRequestList(Map<String, Object> map);

    /**
     * 汇总信息
     * @param map
     * @return
     */
    public Map<String, Object> getSumInfo(Map<String, Object> map);
    
    /**
	 * 获取导出Excel
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public byte[] exportExcel(Map<String, Object> map) throws Exception;
}