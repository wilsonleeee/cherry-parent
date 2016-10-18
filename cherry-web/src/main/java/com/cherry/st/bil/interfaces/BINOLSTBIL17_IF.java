/*  
 * @(#)BINOLSTBIL17_IF.java     1.0 2012/11/17   
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
 * 产品调拨申请查询Interface
 * 
 * @author niushunjie
 * @version 1.0 2012.11.28
 */
public interface BINOLSTBIL17_IF extends ICherryInterface{
    /**
     * 取得调拨申请单总数
     * @param map
     * @return
     */
    public int searchProductAllocationCount(Map<String, Object> map);
	
    /**
     * 取得调拨申请单List
     * @param map
     * @return
     */
    public List<Map<String, Object>> searchProductAllocationList(Map<String, Object> map);

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
	
	/**
	 * 明细数量
	 * @param map
	 * @return
	 */
    public int getExportDetailCount(Map<String, Object> map);
    
    /**
     * 导出CSV处理
     * 
     * @param map
     * @return 导出文件地址
     * @throws Exception
     */
    public String exportCSV(Map<String, Object> map) throws Exception;
}