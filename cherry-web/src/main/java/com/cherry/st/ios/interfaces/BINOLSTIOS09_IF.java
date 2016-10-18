/*
 * @(#)BINOLSTIOS09_IF.java     1.0 2013/08/16
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
package com.cherry.st.ios.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

public interface BINOLSTIOS09_IF extends ICherryInterface {

	public int getImportBatchCount(Map<String, Object> map);

	public List<Map<String, Object>> getImportBatchList(Map<String, Object> map);

	public Map<String, Object> ResolveExcel(Map<String, Object> map)
			throws Exception;

	public Map<String, Object> tran_excelHandle(
			Map<String, Object> importDataMap, Map<String, Object> map)
			throws Exception;
	
	public int insertImportBatch(Map<String, Object> map);
}
