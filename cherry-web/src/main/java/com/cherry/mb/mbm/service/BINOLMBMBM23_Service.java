/*
 * @(#)BINOLMBMBM23_Service.java     1.0 2013.08.29
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
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 会员短信沟通明细画面Service
 * 
 * @author WangCT
 * @version 1.0 2013.08.29
 */
public class BINOLMBMBM23_Service extends BaseService {
	
	/**
	 * 取得会员短信沟通总数
	 * 
	 * @param map 检索条件
	 * @return 会员短信沟通总数
	 */
	public int getSmsSendDetailCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM23.getSmsSendDetailCount");
		return baseServiceImpl.getSum(parameterMap);
	}
	
	/**
	 * 取得会员短信沟通List
	 * 
	 * @param map 检索条件
	 * @return 会员短信沟通List
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getSmsSendDetailList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM23.getSmsSendDetailList");
		return baseServiceImpl.getList(parameterMap);
	}

}
