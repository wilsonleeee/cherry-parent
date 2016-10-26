/*
 * @(#)BINOLPTJCS14_IF.java v1.0 2014-6-12
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
package com.cherry.pt.jcs.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

/**
 * 柜台产品价格维护IF
 * 
 * @author JiJW
 * @version 1.0 2014-6-12
 */
public interface BINOLPTJCS48_IF extends ICherryInterface {
	
	
	public Map<String, Object> ResolveExcel(Map<String, Object> map)
			throws Exception;
	
	public Map<String, Object> ResolveExcelCnt(Map<String, Object> map)
			throws Exception;
	
	public Map<String, Object> tran_excelHandle(
			Map<String, Object> importDataMap, Map<String, Object> map)
			throws Exception;	
	
	public Map<String, Object> tran_excelHandleCnt(Map<String, Object> importDataMap,
			Map<String, Object> sessionMap) throws Exception;
	
	/**
	 * 取得产品价格方案
	 * @param map
	 * @return
	 */
	public List getPrtPriceSolutionList(Map<String, Object> map);
	
	/**
	 * 取得业务日期
	 * 
	 * @param map
	 * @return
	 */
	public String getBussinessDate(Map<String, Object> map);


	
}
