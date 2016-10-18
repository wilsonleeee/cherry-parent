/*  
 * @(#)BINOLPTUNQ01_IF    1.0 2016-05-26     
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
package com.cherry.pt.unq.interfaces;

import java.util.List;
import java.util.Map;


/**
 * 
 * 唯一码生成IF
 * 
 * @author zw
 * @version 1.0 2016.05.26
 */
public interface BINOLPTUNQ01_IF{

	/**
	 * 取得唯一码生成总数
	 * @param map
	 * @return
	 */

	public int getUnqGenerateCount(Map<String, Object> map);
	
	
	/**
	 * 取得唯一码生成List
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getUnqViewList(Map<String, Object> map);
	
	
	/**
	 * 取得唯一码明细List
	 * @param map
	 * @return
	 * @throws Exception 
	 */
	public byte[] exportExcel(Map<String, Object> map) throws Exception;
	
	/**
	 * 修改导出Excel次数和最终导出Excel时间
	 * @param map
	 * @return
	 * @throws Exception 
	 */
	public void updateExportExcelCountAndExportExcelTime(Map<String, Object> map);
	

	/**
	 * 导出CSV
	 * @param map
	 * @return
	 * @throws Exception 
	 */
	public String exportCSV(Map<String, Object> map) throws Exception;
	
	/* ************************************************************************************************************* */

	/**
	 * 生成唯一码
	 * 生成箱码、积分唯一码、关联唯一码
	 * @return
	 * @throws Exception
	 */
	public String tran_GenerateUnqCode(Map<String,Object> map ) throws Exception;
	
	/**
	 * 取得批次号
	 * @param map
	 * @return
	 */

	public String getNewPrtUniqueCodeBatchNo(Map<String, Object> map);
	
	
	/* ************************************************************************************************************* */


}
