/*
 * @(#)BINOLPTRPS28_IF.java     1.0 2013/08/12
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
 * 销售完成进度报表IF
 * 
 * @author WangCT
 * @version 1.0 2013/08/12
 */
public interface BINOLPTRPS28_IF extends ICherryInterface {
	
	/**
	 * 查询销售完成进度报表信息总件数
	 * 
	 * @param map 查询条件
	 * @return 销售完成进度报表信息总件数
	 */
	public int getSaleTargetRptCount(Map<String, Object> map);
	
	/**
	 * 查询销售完成进度报表信息List
	 * 
	 * @param map 查询条件
	 * @return 销售完成进度报表信息List
	 */
	public List<Map<String, Object>> getSaleTargetRptList(Map<String, Object> map);

}
