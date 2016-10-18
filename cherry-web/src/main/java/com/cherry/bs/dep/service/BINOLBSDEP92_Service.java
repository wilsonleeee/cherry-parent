/*
 * @(#)BINOLBSDEP92_Service.java     1.0 2011.2.10
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
import java.util.List;
import java.util.Map;

import org.springframework.cache.annotation.CacheEvict;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 品牌信息管理共通Service
 * 
 * @author WangCT
 * @version 1.0 2011.2.10
 */
@SuppressWarnings("unchecked")
public class BINOLBSDEP92_Service extends BaseService {
	
	/**
	 * 查询品牌总数
	 * 
	 * @param map 检索条件
	 * @return 返回品牌总数
	 */
	public int getBrandInfoCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSDEP92.getBrandInfoCount");
		return baseServiceImpl.getSum(parameterMap);
	}

	/**
	 * 取得品牌信息List
	 * 
	 * @param map 检索条件
	 * @return 品牌信息List
	 */
	public List<Map<String, Object>> getBrandInfoList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSDEP92.getBrandInfoList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 取得品牌信息
	 * 
	 * @param map 查询条件
	 * @return 品牌信息
	 */
	public Map<String, Object> getBrandInfo(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSDEP92.getBrandInfo");
		return (Map)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 判断品牌代码是否已经存在
	 * 
	 * @param map 查询条件
	 * @return 品牌ID
	 */
	public String checkBrandCode(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSDEP92.checkBrandCode");
		return (String)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 判断品牌名称是否已经存在
	 * 
	 * @param map 查询条件
	 * @return 品牌ID
	 */
	public String checkBrandName(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSDEP92.checkBrandName");
		return (String)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 添加品牌
	 * 
	 * @param map 添加内容
	 */
	public int addBrandInfo(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSDEP92.addBrandInfo");
		return baseServiceImpl.saveBackId(parameterMap);
	}
	
	/**
	 * 
	 * 更新品牌
	 * 
	 * @param map 更新条件
	 * @return 处理件数
	 */
	public int updateBrandInfo(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSDEP92.updateBrandInfo");
		return baseServiceImpl.update(parameterMap);
	}
	
	/**
	 * 
	 * 更新部门信息
	 * 
	 * @param map 更新条件
	 * @return 处理件数
	 */
	public int updateOrganizationInfo(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSDEP92.updateOrganizationInfo");
		return baseServiceImpl.update(parameterMap);
	}
	
	/**
	 * 
	 * 伦理删除品牌
	 * 
	 * @param map 更新条件
	 * @return 处理件数
	 */
	public int deleteBrandInfo(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSDEP92.deleteBrandInfo");
		return baseServiceImpl.update(parameterMap);
	}

	/**
	 * 给某品牌添加默认的基本配置信息
	 * 
	 * @param map 添加内容
	 */
	public void addSystemConfig(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSDEP92.addSystemConfig");
		baseServiceImpl.save(parameterMap);
	}
	
	/**
	 * 给品牌添加默认的岗位信息
	 * 
	 * @param map 添加内容
	 */
	@CacheEvict(value="CherryEmpCache",allEntries=true,beforeInvocation=false)
	public void addPosCategory(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSDEP92.addPosCategory");
		baseServiceImpl.save(parameterMap);
	}
	
	/**
	 * 给品牌添加默认的密码配置信息
	 * 
	 * @param map 添加内容
	 */
	public void addPwdConfig(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSDEP92.addPwdConfig");
		baseServiceImpl.save(parameterMap);
	}
	
	/**
	 * 添加品牌数据源信息
	 * 
	 * @param map 添加内容
	 */
	public void addDataSource(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSDEP92.addDataSource");
		baseConfServiceImpl.save(parameterMap);
	}
	
	/**
	 * 添加品牌业务日期
	 * 
	 * @param map 添加内容
	 */
	public void addBussinessDate(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSDEP92.addBussinessDate");
		baseServiceImpl.save(parameterMap);
	}
	
	/**
	 * 添加默认厂商信息
	 * 
	 * @param map 添加内容
	 */
	public void addManufacturerInfo(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSDEP92.addManufacturerInfo");
		baseServiceImpl.save(parameterMap);
	}
	
	/**
	 * 取得组织区域节点
	 * 
	 * @param map 查询条件
	 * @return 组织区域节点
	 */
	public String getOrgRegionNode(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSDEP92.getOrgRegionNode");
		return (String)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 取得品牌区域节点
	 * 
	 * @param map 查询条件
	 * @return 品牌区域节点
	 */
	public String getBrandRegionNode(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSDEP92.getBrandRegionNode");
		return (String)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 判断品牌组织在BIN_BrandDataSourceConfig表中是否已经存在
	 * 
	 * @param map 查询条件
	 * @return 品牌ID
	 */
	public int checkOrgCode(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLBSDEP92.checkOrgCode");
		return baseConfServiceImpl.getSum(parameterMap);
	}
}
