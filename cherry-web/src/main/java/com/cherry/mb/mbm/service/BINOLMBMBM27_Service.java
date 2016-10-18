/*
 * @(#)BINOLMBMBM27_Service.java     1.0 2013.09.23
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
package com.cherry.mb.mbm.service;

import java.util.HashMap;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 添加会员问题画面Service
 * 
 * @author WangCT
 * @version 1.0 2013.09.23
 */
public class BINOLMBMBM27_Service extends BaseService {
	
	/**
	 * 添加会员问题
	 * 
	 * @param map 添加内容
	 */
	public int addIssue(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM27.addIssue");
		return baseServiceImpl.saveBackId(parameterMap);
	}

}
