/*	
 * @(#)BINOLPTRPS46_IF.java     1.0 2016/10/18		
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
import com.cherry.cm.core.*;

/**
 * 产品加权平均价查询一览接口
 * 
 * @author lianmh
 * @version 1.0 2016/10/18
 * 
 */
public interface BINOLPTRPS46_IF extends BINOLCM37_IF {

	/**
	 * 获取记录总数
	 * 
	 * @param map
	 * @return
	 */
	public int getPrtCheckoutInfoCount(Map<String, Object> map);

	/**
	 * 获取LIST
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getPrtCheckoutInfoList(Map<String, Object> map);
	
	/**
	 * 导出处理
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public String exportCSV(Map<String, Object> map) throws Exception;


	/**
	 * Excel导出
	 * 
	 * @param map
	 * @return 返回导出信息
	 * @throws CherryException
	 */
	public byte[] exportExcel(Map<String, Object> map) throws Exception;

	/**
	 * 获取导出记录明细总数
	 * 
	 * @param map
	 * @return
	 */
	public int getExportDetailCount(Map<String, Object> map);

}
