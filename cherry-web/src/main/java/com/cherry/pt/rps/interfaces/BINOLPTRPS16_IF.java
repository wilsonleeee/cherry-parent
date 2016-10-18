/*
 * @(#)BINOLPTRPS01_IF.java     1.0 2012/12/04
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

import com.cherry.cm.core.ICherryInterface;

/**
 * 
 * 盘点查询Interface
 * 
 * @author liuminghao
 * @version 1.0 2011.12.04
 */
public interface BINOLPTRPS16_IF extends ICherryInterface {

	public int searchTakingCount(Map<String, Object> map);

	public List<Map<String, Object>> searchTakingList(Map<String, Object> map);

	/**
	 * 汇总信息
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getSumInfo(Map<String, Object> map);

	public byte[] exportExcel(Map<String, Object> map) throws Exception;

	/**
	 * 取得导出文件名（国际化）
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public String getExportName(Map<String, Object> map) throws Exception;

	/**
	 * 获取相关连的产品厂商ID
	 * @param tempMap
	 * @return
	 */
	public List getConjunctionProList(Map<String, Object> map);
}
