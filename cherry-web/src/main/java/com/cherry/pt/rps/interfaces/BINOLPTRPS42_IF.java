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
 * 预付单查询一览接口
 * @author nanjunbo
 * @version 1.0 2016/07/15 
 * 
 * */
public interface BINOLPTRPS42_IF extends ICherryInterface{
	
	/**
	 * 获取预付单记录总数
	 * @param map
	 * @return
	 */
	public int getPreInfoCount(Map<String,Object> map);

	/**
	 * 获取预付单LIST
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> getPreInfoList(Map<String,Object> map);

	/**
	 * 获取产品的总数量和总金额
	 * 
	 * */
	public Map<String,Object> getSumPreInfo(Map<String,Object> map);
	
	/**
	 * 获取预付单明细
	 * @param map
	 * @return
	 */
	public Map<String,Object> getpreInfoMap(Map<String,Object> map);
	/**
	 * 预付单详情list
	 * 
	 * */
	public List<Map<String,Object>> getPreDetailInfoList(Map<String,Object> map);
	
	/**
	 * 提货单详情list
	 * 
	 * */
	public List<Map<String,Object>> getPickDetailInfoList(Map<String,Object> map);
	
	
	/**
	 * Excel导出
	 * 
	 * @param map
	 * @return 返回导出信息
	 * @throws CherryException
	 */
	public byte[] exportExcel(Map<String, Object> map) throws Exception;
	

	
	/**
	 * 获取销售记录明细总数
	 * @param map
	 * @return
	 */
	public int getExportDetailCount(Map<String, Object> map);
	/**
	 * 获取支付方式
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> getPayTypeList(Map<String,Object> map);
}
