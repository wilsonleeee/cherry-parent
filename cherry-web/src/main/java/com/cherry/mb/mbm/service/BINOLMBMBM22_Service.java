/*
 * @(#)BINOLMBMBM22_Service.java     1.0 2013.08.13
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
 * 会员答卷画面Service
 * 
 * @author WangCT
 * @version 1.0 2013.08.13
 */
public class BINOLMBMBM22_Service extends BaseService {
	
	/**
	 * 取得会员答卷信息总数
	 * 
	 * @param map 检索条件
	 * @return 会员答卷信息总数
	 */
	public int getMemAnswerCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM22.getMemAnswerCount");
		return baseServiceImpl.getSum(parameterMap);
	}
	
	/**
	 * 取得会员答卷信息List
	 * 
	 * @param map 检索条件
	 * @return 会员答卷信息List
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getMemAnswerList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM22.getMemAnswerList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 根据问卷ID和答卷ID获取问题和答案
	 * 
	 * @param map 检索条件
	 * @return 问题和答案信息List
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getMemQuestionList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM22.getMemQuestionList");
		return baseServiceImpl.getList(parameterMap);
	}

}
