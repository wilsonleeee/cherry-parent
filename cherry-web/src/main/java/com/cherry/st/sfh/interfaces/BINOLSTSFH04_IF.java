/*  
 * @(#)BINOLSTSFH04_IF.java     1.0 2011/09/14     
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

import com.cherry.cm.core.ICherryInterface;

/**
 * 
 * 产品发货单一览Interface
 * 
 * @author niushunjie
 * @version 1.0 2011.09.14
 */
public interface BINOLSTSFH04_IF extends ICherryInterface{
    /**
     * 取得产品发货单总数
     * @param map
     * @return 产品发货单总数
     */
	public int searchProductDeliverCount(Map<String, Object> map);
	
    /**
     * 取得产品发货单List
     * @param map
     * @return 产品发货单List
     */
	public List<Map<String, Object>> searchProductDeliverList(Map<String, Object> map);

	/**
     * 汇总信息
     * 
     * @param map
     * @return
     */
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
