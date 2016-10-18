/*
 * @(#)BINOLSTIOS09_IF.java     1.0 2012/11/27
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

public interface BINOLSTIOS08_IF extends ICherryInterface {
	public int getProductInDepotExcelCount(Map<String, Object> map);

	public List<Map<String, Object>> getProductInDepotExcelList(
			Map<String, Object> map);

	public List<Map<String, Object>> getProductInDepotExcelDetailList(
			Map<String, Object> map);

	public Map<String, Object> getProductInDepotExcelInfo(
			Map<String, Object> map);

	public byte[] exportExcel(Map<String, Object> map) throws Exception;
}