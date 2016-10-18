/*
 * @(#)BINOLCTTPL01_Action.java     1.0 2013.1.16
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

package com.cherry.ct.common.interfaces;

import java.util.List;
import java.util.Map;
/**
 * 
 * 
 * @author ZhangGS
 * @version 1.0 2013.1.16
 */
public interface BINOLCTCOM03_IF {

	/**
	 * 沟通信息保存处理
	 * 
	 * @param Map,Map
	 * 			保存处理的参数集合
	 * @return 无
	 * 			
	 * @throws Exception 
	 */
	public void tran_saveCommInfo(Map<String, Object> planMap, List<Map<String, Object>> commMapList, String type) throws Exception;

	/**
	 * 获取沟通计划信息List
	 * 
	 * @param map
	 * @return 沟通计划List
	 * @throws Exception 
	 */
	public Map<String, Object> getPlanInfoByCode (Map<String, Object> map) throws Exception;
	
}