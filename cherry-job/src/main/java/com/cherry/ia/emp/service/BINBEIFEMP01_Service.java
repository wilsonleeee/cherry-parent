/*
 * @(#)BINBEIFEMP01_Service.java     1.0 2010/11/12
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
 * 营业员列表导入Service
 * 
 * 
 * @author WangCT
 * @version 1.0 2011.05.26
 */
public class BINBEIFEMP01_Service extends BaseService {
	
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
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFEMP01.getBaInfoList");
		return ifServiceImpl.getList(paramMap);
	}
	
	/**
	 * 
	 * 查询营业员信息
	 * 
	 * @param map 查询条件
	 * @return 营业员ID
	 * 
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getBaInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFEMP01.getBaInfo");
		return (Map)baseServiceImpl.get(paramMap);
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
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFEMP01.updateBaInfo");
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
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFEMP01.insertBaInfo");
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
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFEMP01.updateEmployee");
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
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFEMP01.insertEmployee");
		return baseServiceImpl.saveBackId(paramMap);
	}
	
	/**
	 * 
	 * 备份营业员信息表
	 * 
	 * @param map 查询条件
	 * @return 无
	 * 
	 */
	public void backupBaInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFEMP01.backupBaInfo");
		baseServiceImpl.save(paramMap);
	}

	/**
	 * 
	 * 删除世代番号超过上限的数据
	 * 
	 * @param map 删除条件
	 * @return 无
	 * 
	 */
	public void clearBackupData(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFEMP01.clearBackupData");
		baseServiceImpl.remove(paramMap);
	}

	/**
	 * 
	 * 更新世代番号
	 * 
	 * @param map 更新条件
	 * @return 无
	 * 
	 */
	public void updateBackupCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFEMP01.updateBackupCount");
		baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 
	 * 查询营业员所在部门ID
	 * 
	 * @param map 查询条件
	 * @return 部门ID
	 * 
	 */
	public Object getOrganizationId(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFEMP01.getOrganizationId");
		return baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 
	 * 查询营业员所在柜台的柜台主管
	 * 
	 * @param map 查询条件
	 * @return 柜台主管
	 * 
	 */
	public Object getHigherPath(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFEMP01.getHigherPath");
		return baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 
	 * 查询营业员岗位类别ID
	 * 
	 * @param map 查询条件
	 * @return 岗位类别ID
	 * 
	 */
	public Object getPositionCategoryId(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFEMP01.getPositionCategoryId");
		return baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 
	 * 查询需要伦理删除的营业员数据
	 * 
	 * @param map 查询条件
	 * @return 需要伦理删除的营业员List
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getDelList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFEMP01.getDelList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 
	 * 删除无效的营业员数据
	 * 
	 * @param map 需要伦理删除的营业员
	 * @return 无
	 * 
	 */
	public void delInvalidBaInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEIFEMP01.delInvalidBaInfo");
		baseServiceImpl.update(paramMap);
	}

}
