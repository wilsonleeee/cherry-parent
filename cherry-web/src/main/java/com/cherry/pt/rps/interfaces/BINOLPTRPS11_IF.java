/*  
 * @(#)BINOLPTRPS11_IF.java     1.0 2011/05/31      
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

import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.ICherryInterface;

public interface BINOLPTRPS11_IF extends ICherryInterface{
	
	/**
	 * 取得库存记录总数
	 * 
	 * @param map
	 * @return
	 */
	public int getProStockCount(Map<String, Object> map);
	
	/**
	 * 取得库存汇总信息
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getSumInfo(Map<String, Object> map);
	
	/**
	 * 取得库存记录LIST
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
	 * 一览概要EXCEL导出
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public byte[] exportSummaryExcel(Map<String, Object> map) throws Exception;
	
	/**
     * 导出CSV处理
     * 
     * @param map
	 * @return 导出文件地址
	 * @throws Exception
     */
	public String exportCSV(Map<String, Object> map) throws Exception;
	
	/**
	 * 取得导出文件名（国际化）
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public String getExportName(Map<String, Object> map) throws Exception;
	
	/**
	 * 查询产品库存详细总数
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int getDeptStockCount(Map<String, Object> map) throws Exception;
}
