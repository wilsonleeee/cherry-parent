/*  
 * @(#)BINOLPTRPS37_IF.java     1.0 2015-1-21      
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

import com.cherry.cm.cmbussiness.interfaces.BINOLCM37_IF;
import com.cherry.cm.core.CherryException;

/**
 * 
 * @ClassName: BINOLPTRPS37_IF 
 * @Description: TODO(实时库存预警interfaces) 
 * @author menghao
 * @version v1.0.0 2015-1-21 
 *
 */
public interface BINOLPTRPS37_IF extends BINOLCM37_IF {
	
	/**
	 * 取得实时库存预警记录总数
	 * 
	 * @param map
	 * @return
	 */
	public int getProStockCount(Map<String, Object> map);
	
	/**
	 * 取得实时库存预警记录LIST
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getProStockList(Map<String, Object> map);
	
	/**
	 * Excel导出
	 * 
	 * @param map
	 * @return 返回导出信息
	 * @throws CherryException
	 */
	public byte[] exportExcel(Map<String, Object> map) throws Exception;
	
	/**
     * 导出CSV处理
     * 
     * @param map
	 * @return 导出文件地址
	 * @throws Exception
     */
	public String exportCSV(Map<String, Object> map) throws Exception;
	
	
}
