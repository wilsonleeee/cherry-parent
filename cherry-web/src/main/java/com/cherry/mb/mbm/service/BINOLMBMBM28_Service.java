/*
 * @(#)BINOLMBMBM28_Service.java     1.0 2013.09.23
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
 * 会员问题处理画面Service
 * 
 * @author WangCT
 * @version 1.0 2013.09.23
 */
public class BINOLMBMBM28_Service extends BaseService {
	
	/**
	 * 取得会员问题明细
	 * 
	 * @param map 查询条件
	 * @return 会员问题明细
	 */
	public Map<String, Object> getIssueDetail(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM28.getIssueDetail");
		return (Map)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 取得会员问题处理信息List
	 * 
	 * @param map 查询条件
	 * @return 会员问题处理信息List
	 */
	public List<Map<String, Object>> getIssueActionList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM28.getIssueActionList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 更新会员问题
	 * 
	 * @param map 更新内容
	 * @return 更新件数
	 */
	public int updIssue(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM28.updIssue");
		return baseServiceImpl.update(parameterMap);
	}
	
	/**
	 * 更新会员问题状态
	 * 
	 * @param map 更新内容
	 * @return 更新件数
	 */
	public int updIssueStatus(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM28.updIssueStatus");
		return baseServiceImpl.update(parameterMap);
	}
	
	/**
	 * 删除会员问题
	 * 
	 * @param map 删除条件
	 * @return 删除件数
	 */
	public int delIssue(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM28.delIssue");
		return baseServiceImpl.remove(parameterMap);
	}
	
	/**
	 * 添加会员问题处理内容
	 * 
	 * @param map 添加内容
	 */
	public void addIssueAction(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM28.addIssueAction");
		baseServiceImpl.save(parameterMap);
	}
	
	/**
	 * 更新会员问题处理内容
	 * 
	 * @param map 更新内容
	 * @return 更新件数
	 */
	public int updIssueAction(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM28.updIssueAction");
		return baseServiceImpl.update(parameterMap);
	}
	
	/**
	 * 删除会员问题处理内容
	 * 
	 * @param map 删除条件
	 * @return 删除件数
	 */
	public int delIssueAction(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM28.delIssueAction");
		return baseServiceImpl.remove(parameterMap);
	}

}
