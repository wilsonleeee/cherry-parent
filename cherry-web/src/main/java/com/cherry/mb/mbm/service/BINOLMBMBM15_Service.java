/*
 * @(#)BINOLMBMBM15_Service.java     1.0 2013/04/26
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
 * 会员发卡柜台变更处理Service
 * 
 * @author WangCT
 * @version 1.0 2013/04/26
 */
public class BINOLMBMBM15_Service extends BaseService {
	
	/**
	 * 会员发卡柜台转柜处理
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int moveMemCounter(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM15.moveMemCounter");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 查询原发卡柜台的会员信息List
	 * 
	 * @param map 查询条件
	 * @return 原发卡柜台的会员信息List
	 */
	public List<Map<String, Object>> getOldCounterMemInfoList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM15.getOldCounterMemInfoList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 批量更新会员发卡柜台
	 * 
	 * @param list 更新条件
	 */
	public void updMemCounter(List<Map<String, Object>> list) {
		baseServiceImpl.updateAll(list, "BINOLMBMBM15.updMemCounter");
	}
	
	/**
	 * 批量添加会员修改履历主信息
	 * 
	 * @param list 添加内容
	 */
	public void addMemInfoRecord(List<Map<String, Object>> list) {
		baseServiceImpl.saveAll(list, "BINOLMBMBM15.addMemInfoRecord");
	}
	
	/**
	 * 批量添加会员修改履历明细信息
	 * 
	 * @param map 添加内容
	 */
	public void addMemInfoRecordDetail(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM15.addMemInfoRecordDetail");
		baseServiceImpl.save(paramMap);
	}
	
	/**
	 * 查询需要撤销转柜的会员信息List
	 * 
	 * @param map 查询条件
	 * @return 需要撤销转柜的会员信息List
	 */
	public List<Map<String, Object>> getReMoveCouMemInfoList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM15.getReMoveCouMemInfoList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 根据转柜台批次号查询任意一个会员修改履历信息
	 * 
	 * @param map 查询条件
	 * @return 会员修改履历信息
	 */
	public Map<String, Object> getMemInfoRecordInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM15.getMemInfoRecordInfo");
		return (Map)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 根据转柜台批次号查询转柜会员总数
	 * 
	 * @param map 查询条件
	 * @return 转柜会员总数
	 */
	public int getMemInfoRecordCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM15.getMemInfoRecordCount");
		return baseServiceImpl.getSum(paramMap);
	}

}
