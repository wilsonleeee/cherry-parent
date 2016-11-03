/*	
 * @(#)BINBEDRHAN12_Service.java     1.0 2012/08/21
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

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 会员规则履历处理Service
 * 
 * @author WangCT
 * @version 1.0 2012/08/21
 */
public class BINBEDRHAN12_Service extends BaseService {
	
	/**
	 * 把所有规则履历的数据状态更新成未处理
	 * 
	 * @param map 更新条件		
	 * @return 更新件数
	 */
	public int updAllDataStatus(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRHAN12.updAllDataStatus");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 把规则履历的数据状态更新成处理中
	 * 
	 * @param map 更新条件		
	 * @return 更新件数
	 */
	public int updDataStatus(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRHAN12.updDataStatus");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 更新需要保留的规则履历
	 * 
	 * @param map 更新条件		
	 * @return 更新件数
	 */
	public int updKeepRuleRecord(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRHAN12.updKeepRuleRecord");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 查询会员规则履历List
	 * 
	 * @param map 查询条件		
	 * @return 会员规则履历List
	 */
	public List<Map<String, Object>> getMemRuleRecordList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRHAN12.getMemRuleRecordList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 删除会员规则履历
	 * 
	 * @param map 删除条件		
	 * @return 删除件数
	 */
	public int delMemRuleRecord(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRHAN12.delMemRuleRecord");
		return baseServiceImpl.remove(paramMap);
	}
	
	/**
	 * 把旧的规则履历迁移到规则履历历史表
	 * 
	 * @param map 迁移条件
	 */
	public void memRuleRecordMove(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRHAN12.memRuleRecordMove");
		baseServiceImpl.save(paramMap);
	}
	
	/**
	 * 批量插入规则执行履历表
	 * 
	 * @param list 规则履历List
	 */
	public void addRuleExecRecord(List<Map<String, Object>> list){
		baseServiceImpl.saveAll(list, "BINBEDRHAN12.addRuleExecRecord");
	}

}
