/*
 * @(#)BINOLCMPL02_Service.java     1.0 2010/11/04
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

package com.cherry.cm.privilege.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 岗位数据过滤权限共通Service
 * 
 * @author WangCT
 * @version 1.0 2010.11.04
 */
@SuppressWarnings("unchecked")
public class BINOLCMPL02_Service extends BaseService {
	
	/**
	 * 查询某一用户管辖的所有岗位ID(权限类型为0时)
	 * 
	 * @param Map
	 * @return List 
	 */
	public List<Map<String, Object>> getPositionID0List(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCMPL02.getPositionID0List");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 查询某一用户管辖的所有岗位ID(权限类型为1时)
	 * 
	 * @param Map
	 * @return List 
	 */
	public List<Map<String, Object>> getPositionID1List(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCMPL02.getPositionID1List");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 查询某一用户管辖的所有岗位ID(权限类型为2时)
	 * 
	 * @param Map
	 * @return List 
	 */
	public List<Map<String, Object>> getPositionID2List(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCMPL02.getPositionID2List");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 添加岗位数据过滤权限
	 * 
	 * @param List
	 */
	public void addPositionPrivilege(List<Map<String, Object>> list) {
		baseServiceImpl.saveAll(list, "BINOLCMPL02.addPositionPrivilege");
	}
	
	/**
	 * 删除岗位数据权限
	 * 
	 * @param Map
	 * @return 处理件数 
	 */
	public int deletePositionPrivilege(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCMPL02.deletePositionPrivilege");
		return baseServiceImpl.remove(parameterMap);
	}
	
	/**
	 * 查询指定组织的所有岗位
	 * 
	 * @param map 查询条件
	 * @return 指定组织的所有岗位list 
	 */
	public List<Map<String, Object>> getPositionList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCMPL02.getPositionList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	

}
