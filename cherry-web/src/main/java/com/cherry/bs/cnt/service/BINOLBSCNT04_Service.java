/*	
 * @(#)BINOLBSCNT04_Service.java     1.0 2011/05/09		
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

package com.cherry.bs.cnt.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.cache.annotation.CacheEvict;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 	创建柜台画面Service
 * 
 * @author WangCT
 * @version 1.0 2011.05.009
 */
public class BINOLBSCNT04_Service extends BaseService {
	
	/**
	 * 添加柜台信息
	 * 
	 * @param map 添加内容
	 */
	@CacheEvict(value={"CherryDepartCache","CherryAllDepartCache"},allEntries=true,beforeInvocation=false)
	public int addCounterInfo(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCNT04.addCounterInfo");
		return baseServiceImpl.saveBackId(parameterMap);
	}
	
	/**
	 * 添加柜台事件
	 * 
	 * @param map 添加内容
	 */
	public void addCounterEvent(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCNT04.addCounterEvent");
		baseServiceImpl.save(parameterMap);
	}
	
	/**
	 * 添加柜台事件
	 * 
	 * @param map 添加内容
	 */
	public void addCounterEvent(List<Map<String, Object>> list) {
		baseServiceImpl.saveAll(list, "BINOLBSCNT04.addCounterEvent");
	}
	
	/**
	 * 柜台号唯一验证
	 * 
	 * @param map 查询条件
	 */
	public String getCounterInfoId(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCNT04.getCounterInfoId");
		return (String)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 取得品牌的顶层节点
	 * 
	 * @param map 查询条件
	 */
	public String getFirstPath(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCNT04.getFirstPath");
		return (String)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 取得品牌下的未知节点
	 * 
	 * @param map 查询条件
	 */
	public String getUnknownPath(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCNT04.getUnknownPath");
		return (String)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 取得柜台主管的所属部门
	 * 
	 * @param map 查询条件
	 */
	public String getCounterHeaderDep(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCNT04.getCounterHeaderDep");
		return (String)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 取得柜台信息(新老后台交互时使用)
	 * 
	 * @param map 查询条件
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getCounterInfo(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCNT04.getCounterInfo");
		return (Map)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 根据员工ID取得U盘序列号List
	 * 
	 * @param map 查询条件
	 * @return U盘序列号List
	 */
	@SuppressWarnings("unchecked")
	public List<String> getUdiskSNList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCNT04.getUdiskSNList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 根据品牌ID取得品牌code
	 * 
	 * @param map 查询条件
	 * @return 品牌code
	 */
	public String getBrandCode(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCNT04.getBrandCode");
		return (String)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 取得员工信息
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getEmployeeInfo (Map<String, Object> map) {
		
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCNT04.getEmployeeInfo");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	
	/**
	 * 取得城市区号 
	 * 
	 * @param map 查询条件
	 * @return 品牌code
	 */
	public String getCntTeleCode(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCNT04.getCntTeleCode");
		return (String)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 取得城市区号 
	 * 
	 * @param map 查询条件
	 * @return 品牌code
	 */
	public String getCntCodeRightTree(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSCNT04.getCntCodeRightTree");
		return (String)baseServiceImpl.get(parameterMap);
	}


}
