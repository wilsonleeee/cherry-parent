/*
 * @(#)BINBEDRHAN10_Service.java     1.0 2012/05/28
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
package com.cherry.dr.handler.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 处理会员的规则履历记录service
 * 
 * 
 * @author WangCT
 * @version 1.0 2012/05/28
 */
public class BINBEDRHAN10_Service extends BaseService {
	
	/**
	 * 查询会员信息List
	 * 
	 * @param map 查询条件
	 * @return 会员信息List
	 */	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getMemberInfoList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEDRHAN10.getMemberInfoList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 查询会员规则履历List
	 * 
	 * @param map 查询条件
	 * @return 会员规则履历List
	 */	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getMemRuleRecordList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEDRHAN10.getMemRuleRecordList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 更新已经处理过的规则履历记录
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */	
	public int updateCompleteRuleRecord(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEDRHAN10.updateCompleteRuleRecord");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 查询会员信息
	 * 
	 * @param map 查询条件
	 * @return 会员信息
	 */	
	@SuppressWarnings("unchecked")
	public Map<String, Object> getMemberInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEDRHAN10.getMemberInfo");
		return (Map)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 查询会员等级信息List
	 * 
	 * @param map 查询条件
	 * @return 会员等级信息List
	 */	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getMemberLevelInfoList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEDRHAN10.getMemberLevelInfoList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 查询会员规则履历中关联单号对应的销售记录List
	 * 
	 * @param map 查询条件
	 * @return 销售记录List
	 */	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getSaleRecordList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEDRHAN10.getSaleRecordList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 查询会员规则履历中关联单号对应的化妆次数积分使用记录List
	 * 
	 * @param map 查询条件
	 * @return 化妆次数积分使用记录List
	 */	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getMemUsedCountList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEDRHAN10.getMemUsedCountList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 更新已经处理过的规则履历记录
	 * 
	 * @param map 更新条件
	 */	
	public void updateCompleteRuleRecord(List<Map<String, Object>> list) {
		baseServiceImpl.updateAll(list, "BINBEDRHAN10.updateCompleteRuleRecord");
	}
	
	/**
	 * 插入规则执行履历表
	 * 
	 * @param list 插入内容
	 */	
	public void addRuleExecRecord(List<Map<String, Object>> list) {
		baseServiceImpl.saveAll(list, "BINBEDRHAN10.addRuleExecRecord");
	}
	
	/**
	 * 查询会员信息List
	 * 
	 * @param map 查询条件
	 * @return 会员信息List
	 */	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getAllMemberInfoList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEDRHAN10.getAllMemberInfoList");
		return baseServiceImpl.getList(paramMap);
	}

}
