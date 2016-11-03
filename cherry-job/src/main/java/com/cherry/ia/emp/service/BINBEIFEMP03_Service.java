/*
 * @(#)BINBEIFEMP03_Service.java     1.0 2010/11/12
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

package com.cherry.ia.emp.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 营业员列表导入Service（雅漾用）
 * 
 * 
 * @author WangCT
 * @version 1.0 2011.05.26
 */
public class BINBEIFEMP03_Service extends BaseService {
	
	/**
	 * 
	 * 从接口数据库中查询营业员数据
	 * 
	 * @param map 查询条件
	 * @return 营业员List
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getBaInfoList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFEMP03.getBaInfoList");
		return witBaseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 
	 * 更新营业员信息表
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 * 
	 */
	public int updateBaInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFEMP03.updateBaInfo");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 
	 * 插入营业员信息
	 * 
	 * @param map 插入内容
	 * @return 营业员ID
	 * 
	 */
	public void insertBaInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFEMP03.insertBaInfo");
		baseServiceImpl.save(paramMap);
	}
	
	/**
	 * 
	 * 更新员工信息表
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 * 
	 */
	public int updateEmployee(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFEMP03.updateEmployee");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 
	 * 插入员工信息
	 * 
	 * @param map 插入内容
	 * @return 员工ID
	 * 
	 */
	public int insertEmployee(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFEMP03.insertEmployee");
		return baseServiceImpl.saveBackId(paramMap);
	}
	
}
