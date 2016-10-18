/*
 * @(#)BINOLPTRPS30_IF.java     1.0 2014/06/24
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
 * SPA报表IF
 * @author zhangle
 * @version 1.0 2014.06.27
 */
public interface BINOLPTRPS30_IF extends BINOLCM37_IF {
	
	/**
	 * 查询SPA报表记录数
	 * @param map
	 * @return
	 */
	public int getSPARptCount(Map<String, Object> map);
	
	/**
	 * 查询SPA报表List
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getSPARptList(Map<String, Object> map);
	
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
	
	/**
	 * 获取业务日期
	 * @return
	 */
	public String getBussnissDate(Map<String, Object> map);

}
