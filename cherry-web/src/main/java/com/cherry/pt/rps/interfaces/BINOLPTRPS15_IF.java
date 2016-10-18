/*	
 * @(#)BINOLPTRPS15_IF.java     1.0 2012/10/31		
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
 * 统计销售信息IF
 * 
 * @author WangCT
 * @version 1.0 2012/10/31
 */
public interface BINOLPTRPS15_IF extends ICherryInterface {
	
	/**
	 * 查询销售统计总数
	 * 
	 * @param map 查询条件
	 * @return 销售统计总数
	 */
	public int getSaleCountInfoCount(Map<String, Object> map);
	
	/**
	 * 查询销售统计信息
	 * 
	 * @param map 查询条件
	 * @return 销售统计信息
	 */
	public Map<String, Object> getSaleCountInfo(Map<String, Object> map);
	
	/**
	 * 查询销售统计信息List
	 * 
	 * @param map 检索条件
	 * @return 销售统计信息List
	 */
	public List<Map<String, Object>> getSaleCountInfoList(Map<String, Object> map);

}
