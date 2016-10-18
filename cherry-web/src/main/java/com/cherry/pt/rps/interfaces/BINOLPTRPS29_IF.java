/*
 * @(#)BINOLPTRPS29_IF.java     1.0 2014/06/24
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

/**
 * BB柜台销售统计IF
 * @author zhangle
 * @version 1.0 2014.06.24
 */
public interface BINOLPTRPS29_IF extends BINOLCM37_IF {
	
	/**
	 * 查询BB柜台销售统计记录数
	 * @param map
	 * @return
	 */
	public int getBBCounterSaleRptCount(Map<String, Object> map);
	
	/**
	 * 查询BB柜台销售统计List
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getBBCounterSaleRptList(Map<String, Object> map);
	
	/**
	 * 查询BB柜台销售明细Count
	 * @param map
	 * @return
	 */
	public int getBBCounterSaleDetailRptCount(Map<String, Object> map);
	
	/**
	 * 获取导出的map
	 * @param map
	 * @return
	 */
	public Map<String, Object> getExportMap(Map<String, Object> map);
	
	/**
	 * 导出
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public String export(Map<String, Object> map) throws Exception;

}
