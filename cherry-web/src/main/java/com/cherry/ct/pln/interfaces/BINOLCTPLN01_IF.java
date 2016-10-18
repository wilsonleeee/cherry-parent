/*
 * @(#)BINOLCTTPL01_Action.java     1.0 2012/11/06
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

package com.cherry.ct.pln.interfaces;

import java.util.List;
import java.util.Map;

public interface BINOLCTPLN01_IF {
	/**
	 * 获取沟通计划数量
	 * 
	 * @param map
	 * @return 沟通计划数量
	 */
	public int getCommunicationPlanCount(Map<String, Object> map);
	
	/**
	 * 获取沟通计划信息List
	 * 
	 * @param map
	 * @return 沟通计划List
	 * @throws Exception 
	 */
	@SuppressWarnings("rawtypes")
	public List getCommunicationPlanList (Map<String, Object> map) throws Exception;
	
}
