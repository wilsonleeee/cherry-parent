/*
 * @(#)BINOLSSPRM66_IF.java     1.0 2013/09/17
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
package com.cherry.ss.prm.interfaces;

import java.util.Map;
import com.cherry.cm.core.ICherryInterface;

/**
 * 
 * 入库导入Interface
 * 
 * @author zhangle
 * @version 1.0 2013.09.17
 */
public interface BINOLSSPRM66_IF extends ICherryInterface {
	
	/**
	 * 获取导入数据
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> resolveExcel(Map<String, Object> map)  throws Exception;
	
	/**
	 * 导入数据
	 * @param importDataMap
	 * @param sessionMap
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> tran_excelHandle(Map<String, Object> importDataMap, Map<String, Object> sessionMap)  throws Exception;
}