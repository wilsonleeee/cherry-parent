/*	
 * @(#)BINOLPTRPS13_IF.java     1.0 2011/03/16		
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
import com.cherry.cm.core.*;

/**
 * 销售记录查询一览接口
 * @author zgl
 * @version 1.0 2011/03/16 
 * 
 * */
public interface BINOLPTRPS13_IF extends ICherryInterface{
	
	/**
	 * 获取销售记录总数
	 * @param map
	 * @return
	 */
	public int getSaleRecordCount(Map<String,Object> map);

	/**
	 * 获取销售记录LIST
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> getSaleRecordList(Map<String,Object> map);

	/**
	 * 获取产品的总数量和总金额
	 * 
	 * */
	public Map<String,Object> getSumInfo(Map<String,Object> map);
	
	/**
	 * 各商品统计详细（各商品的总数量及总金额）
	 * 
	 * */
	public List<Map<String,Object>> getSaleProPrmList(Map<String,Object> map);
	
	/**
	 * 销售商品信息模糊查询(输入框AJAX)
	 * @param map
	 * @return String
	 */
	public String indSearchPrmPrt(Map<String, Object> map);
	
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
	
	/**
	 * 取得导出文件名（国际化）
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public String getExportName(Map<String, Object> map) throws Exception;
	
	/**
	 * 取得支付方式
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> getPayTypeList(Map<String,Object> map);
	
	/**
	 * 获取销售记录明细总数
	 * @param map
	 * @return
	 */
	public int getExportDetailCount(Map<String, Object> map);
	
	/**
	 * 修改发票类型
	 * @param list
	 * @throws Exception
	 */
	public void updateInvoiceFlag(List<Map<String, Object>> list) throws Exception;
	
	/**
	 * 海明威特殊模板（主单）
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public String exportMain(Map<String, Object> map) throws Exception;
	
	/**
	 * 海明威特殊模板（明细单）
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public String exportDetail(Map<String, Object> map) throws Exception;

	/**
	 * 获取产品销售记录明细总数（过滤明细中的快递费信息）
	 * @param map
	 * @return
	 */
	public int getExportDetailFilterKDCount(Map<String, Object> map);

	
	/**
	 * 指定商品统计详细（指定商品的总成本价）
	 * @param map
	 * @return
	 */
	public Map<String, Object> getSaleProPrmCostPriceList(Map<String, Object> map);
}
