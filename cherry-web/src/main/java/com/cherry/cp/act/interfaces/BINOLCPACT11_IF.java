/*	
 * @(#)BINOLCPACT11_IF.java     1.0 @2014-01-13		
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
package com.cherry.cp.act.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

/**
 * 活动履历Interface
 * 
 * @author LUOHONG
 * 
 */
public interface BINOLCPACT11_IF extends ICherryInterface {

	/**
	 * 取得活动履历数量
	 * 
	 * @param map
	 * @return
	 */
	public int getCampHistoryCount(Map<String, Object> map);

	
	/**
	 * 取得活动履历详细List
	 * 
	 * @param map
	 * @return
	 * @throws Exception 
	 */
	public List<Map<String, Object>> getCampHistoryList(Map<String, Object> map) throws Exception;
}
