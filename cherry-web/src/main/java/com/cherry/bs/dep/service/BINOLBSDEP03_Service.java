/*
 * @(#)BINOLBSDEP03_Service.java     1.0 2010/10/27
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

package com.cherry.bs.dep.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.cache.annotation.CacheEvict;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 更新部门画面Service
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
public class BINOLBSDEP03_Service extends BaseService {
	
	/**
	 * 
	 * 更新部门信息
	 * 
	 * @param map 更新条件
	 * @return 处理件数
	 */
	@CacheEvict(value="CherryAllDepartCache",allEntries=true,beforeInvocation=false)
	public int updateOrganizationInfo(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSDEP03.updateOrganizationInfo");
		return baseServiceImpl.update(parameterMap);
	}
	
	/**
	 * 
	 * 更新部门地址
	 * 
	 * @param map 更新条件
	 * @return 处理件数
	 */
	public int updateAddress(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSDEP03.updateAddress");
		return baseServiceImpl.update(parameterMap);
	}
	
	/**
	 * 
	 * 更新下属机构地址
	 * 
	 * @param map 更新条件
	 * @return 处理件数
	 */
	public int updateSubordinateAddress(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSDEP03.updateSubordinateAddress");
		return baseServiceImpl.update(parameterMap);
	}
	
	/**
	 * 
	 * 伦理删除部门地址
	 * 
	 * @param map 更新条件
	 * @return 处理件数
	 */
	public int deleteAddress(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSDEP03.deleteAddress");
		return baseServiceImpl.update(parameterMap);
	}
	
	/**
	 * 
	 * 伦理删除下属机构地址
	 * 
	 * @param map 更新条件
	 * @return 处理件数
	 */
	public int deleteSubordinateAddress(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSDEP03.deleteSubordinateAddress");
		return baseServiceImpl.update(parameterMap);
	}
	
	/**
	 * 
	 * 更新部门联系人
	 * 
	 * @param map 更新条件
	 * @return 处理件数
	 */
	public int updateContactInfo(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSDEP03.updateContactInfo");
		return baseServiceImpl.update(parameterMap);
	}
	
	/**
	 * 
	 * 伦理删除部门联系人
	 * 
	 * @param map 更新条件
	 * @return 处理件数
	 */
	public int deleteContactInfo(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSDEP03.deleteContactInfo");
		return baseServiceImpl.update(parameterMap);
	}
	
	/**
	 * 
	 * 组织结构节点移动
	 * 
	 * @param map 更新条件
	 * @return 处理件数
	 */
	@CacheEvict(value="CherryAllDepartCache",allEntries=true,beforeInvocation=false)
	public int updateOrganizationNode(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSDEP03.updateOrganizationNode");
		return baseServiceImpl.update(parameterMap);
	}

}
