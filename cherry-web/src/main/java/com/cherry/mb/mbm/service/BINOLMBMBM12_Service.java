/*
 * @(#)BINOLMBMBM12_Service.java     1.0 2013/04/11
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
 * 会员资料修改履历查询画面Service
 * 
 * @author WangCT
 * @version 1.0 2013/04/11
 */
public class BINOLMBMBM12_Service extends BaseService {
	
	/**
	 * 查询会员资料修改履历总件数
	 * 
	 * @param map 查询条件
	 * @return 会员资料修改履历件数
	 */
	public int getMemInfoRecordCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM12.getMemInfoRecordCount");
		return baseServiceImpl.getSum(paramMap);
	}
	
	/**
	 * 查询会员资料修改履历
	 * 
	 * @param map 查询条件
	 * @return 会员资料修改履历
	 */
	public List<Map<String, Object>> getMemInfoRecordList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM12.getMemInfoRecordList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 查询会员资料修改履历(导出用)
	 * 
	 * @param map 查询条件
	 * @return 会员资料修改履历
	 */
	public List<Map<String, Object>> getMemInfoRecordExportList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM12.getMemInfoRecordExportList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 取得所有柜台信息List
	 * 
	 * @param map 查询条件
	 * @return 所有柜台信息List
	 */
	public List<Map<String, Object>> getCounterInfoList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM12.getCounterInfoList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 取得所有员工信息List
	 * 
	 * @param map 查询条件
	 * @return 所有员工信息List
	 */
	public List<Map<String, Object>> getEmployeeList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM12.getEmployeeList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 取得所有区域信息List
	 * 
	 * @param map 查询条件
	 * @return 所有区域信息List
	 */
	public List<Map<String, Object>> getRegionList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBMBM12.getRegionList");
		return baseServiceImpl.getList(paramMap);
	}

}
