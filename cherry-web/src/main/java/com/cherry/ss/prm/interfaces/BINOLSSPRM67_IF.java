/*
 * @(#)BINOLSSPRM67_IF.java     1.0 2013/09/17
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

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

/**
 * 
 * 入库导入明细Interface
 * 
 * @author zhangle
 * @version 1.0 2013.09.17
 */
public interface BINOLSSPRM67_IF extends ICherryInterface {

	/**
	 * 获取入库（Excel导入）单List
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getPrmInDepotExcelList(Map<String, Object> map);
	
	/**
	 * 获取入库（Excel导入）单Count
	 * @param map
	 * @return
	 */
	public int getPrmInDepotExcelCount(Map<String, Object> map);
	
	/**
	 * 获取入库（Excel导入）单主表
	 * @param map
	 * @return
	 */
	public Map<String, Object> getPrmInDepotExcelInfobyId(Map<String, Object> map);
	
	/**
	 * 获取入库（Excel导入）单明细
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getPrmInDepotExcelDetailList(Map<String, Object> map);
	
	/**
	 * 导出Excel
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public byte[] exportExcel(Map<String, Object> map) throws Exception;
}