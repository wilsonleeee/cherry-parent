/*		
 * @(#)BINOLCM00_Service.java     1.0 2010/10/12		
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
package com.cherry.cm.cmbussiness.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.ConvertUtil;

/**
 * 
 * 共通基础处理
 * 
 * @author lipc
 * @version 1.0 2010.10.12
 */
@SuppressWarnings("unchecked")
public class BINOLCM00_Service extends BaseService {

	/**
	 * 取得省及其所属区域List
	 * 
	 * @param map
	 *            (所属品牌,语言)
	 * @return
	 */
	public List<Map<String, Object>> getProvinceList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM00.getProvinceList");
		return baseServiceImpl.getList(map);
	}

	/**
	 * 取得regionId下属区域List
	 * 
	 * @param map
	 *            (区域ID，语言)
	 * @return
	 */
	public List<Map<String, Object>> getChildRegionList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLCM00.getChildRegionList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得登陆用户信息
	 * 
	 * @param map
	 *            
	 * @return
	 */
	public Map<String,Object> UserDetail(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM00.UserDetail");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}

	/**
	 * 取得岗位List
	 * 
	 * @param map
	 *            (部门ID，语言)
	 * @return
	 */
	public List<Map<String, Object>> getPositionList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM00.getPositionList");
		return baseServiceImpl.getList(map);
	}

	/**
	 * 取得部门类型List
	 * 
	 * @param Map
	 *            查询条件
	 * 
	 * @return List 部门类型List
	 */
	public List<Map<String, Object>> getDepartTypeList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 用户ID
		paramMap.put(CherryConstants.USERID, map.get(CherryConstants.USERID));
		// 业务类型
		paramMap.put(CherryConstants.BUSINESS_TYPE, map
				.get(CherryConstants.BUSINESS_TYPE));
		// 操作类型
		paramMap.put(CherryConstants.OPERATION_TYPE, map
				.get(CherryConstants.OPERATION_TYPE));
		paramMap.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLCM00.getDepartTypeList");
		return baseServiceImpl.getList(paramMap);
	}

	/**
	 * 取得权限部门List
	 * 
	 * @param Map
	 *            查询条件
	 * 
	 * @return List 部门List
	 */
	public List<Map<String, Object>> getDepartList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 用户ID
		paramMap.put(CherryConstants.USERID, map.get(CherryConstants.USERID));
		// 业务类型
		paramMap.put(CherryConstants.BUSINESS_TYPE, map
				.get(CherryConstants.BUSINESS_TYPE));
		// 操作类型
		paramMap.put(CherryConstants.OPERATION_TYPE, map
				.get(CherryConstants.OPERATION_TYPE));
		// 部门类型
		paramMap.put(CherryConstants.DEPART_TYPE, map
				.get(CherryConstants.DEPART_TYPE));
		// 语言类型
		paramMap.put(CherryConstants.SESSION_LANGUAGE, map
				.get(CherryConstants.SESSION_LANGUAGE));
		paramMap.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLCM00.getOrganizationList");
		return baseServiceImpl.getList(paramMap);
	}

	/**
	 * 取得部门List
	 * 
	 * @param Map
	 *            查询条件
	 * 
	 * @return List 部门List
	 */
	public List<Map<String, Object>> getOrgList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 部门类型
		paramMap.put(CherryConstants.DEPART_TYPE, map
				.get(CherryConstants.DEPART_TYPE));
		// 所属组织ID
		paramMap.put(CherryConstants.ORGANIZATIONINFOID, map
				.get(CherryConstants.ORGANIZATIONINFOID));
		// 所属品牌ID
		paramMap.put(CherryConstants.BRANDINFOID, map
				.get(CherryConstants.BRANDINFOID));
		// 语言类型
		paramMap.put(CherryConstants.SESSION_LANGUAGE, map
				.get(CherryConstants.SESSION_LANGUAGE));

		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM00.getOrgList");
		return baseServiceImpl.getList(paramMap);
	}

	/**
	 * 取得实体仓库List
	 * 
	 * @param Map
	 *            查询条件
	 * 
	 * @return List 实体仓库List
	 */
	public List<Map<String, Object>> getInventoryList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 用户ID
		paramMap.put(CherryConstants.USERID, map.get(CherryConstants.USERID));
		// 业务类型
		paramMap.put(CherryConstants.BUSINESS_TYPE, map
				.get(CherryConstants.BUSINESS_TYPE));
		// 操作类型
		paramMap.put(CherryConstants.OPERATION_TYPE, map
				.get(CherryConstants.OPERATION_TYPE));
		// 部门类型
		paramMap.put(CherryConstants.DEPART_TYPE, map
				.get(CherryConstants.DEPART_TYPE));
		// 部门ID
		paramMap.put(CherryConstants.ORGANIZATIONID, map
				.get(CherryConstants.ORGANIZATIONID));
		// 语言类型
		paramMap.put(CherryConstants.SESSION_LANGUAGE, map
				.get(CherryConstants.SESSION_LANGUAGE));
		paramMap.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLCM00.getInventoryList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 取得渠道List
	 * 
	 * @param map 查询条件
	 * @return 渠道List
	 */
	public List<Map<String, Object>> getChannelList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM00.getChannelList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 取得商场List
	 * 
	 * @return 商场List
	 */
	public List<Map<String, Object>> getMallInfoList() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM00.getMallInfoList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 取得商场List
	 * 
	 * @return 商场List
	 */
	public List<Map<String, Object>> getMallInfoList(Map<String, Object> paramMap) {
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM00.getMallInfoList");
		return baseServiceImpl.getList(paramMap);
	}
	/**
	 * 取得经销商List
	 * 
	 * @param map 查询条件
	 * @return 经销商List
	 */
	public List<Map<String, Object>> getResellerInfoList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM00.getResellerInfoList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 取得柜台主管List
	 * 
	 * @param map 查询条件
	 * @return 柜台主管List
	 */
	public List<Map<String, Object>> getCounterPositionList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM00.getCounterPositionList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 根据员工code/员工姓名查询员工信息
	 * 
	 * */
	public List<Map<String,Object>>getEmplyessInfo(Map<String,Object> map){
		return baseServiceImpl.getList(map,"BINOLCM00.getEmplyessInfo");
	}
	
	/**
	 * 取得权限柜台(包含测试柜台)List
	 * 
	 * @param Map
	 *            查询条件
	 * 
	 * @return List 柜台List
	 */
	public List<Map<String, Object>> getCounterList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 用户ID
		paramMap.put(CherryConstants.USERID, map.get(CherryConstants.USERID));
		// 业务类型
		paramMap.put(CherryConstants.BUSINESS_TYPE, map
				.get(CherryConstants.BUSINESS_TYPE));
		// 操作类型
		paramMap.put(CherryConstants.OPERATION_TYPE, map
				.get(CherryConstants.OPERATION_TYPE));
		// 部门类型
		paramMap.put(CherryConstants.DEPART_TYPE, map
				.get(CherryConstants.DEPART_TYPE));
		// 语言类型
		paramMap.put(CherryConstants.SESSION_LANGUAGE, map
				.get(CherryConstants.SESSION_LANGUAGE));
		paramMap.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLCM00.getCounterList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 取得权限柜台(不包含测试柜台)List
	 * 
	 * @param Map
	 *            查询条件
	 * 
	 * @return List 柜台List
	 */
	public List<Map<String, Object>> getNoTestCounterList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 用户ID
		paramMap.put(CherryConstants.USERID, map.get(CherryConstants.USERID));
		// 业务类型
		paramMap.put(CherryConstants.BUSINESS_TYPE, map
				.get(CherryConstants.BUSINESS_TYPE));
		// 操作类型
		paramMap.put(CherryConstants.OPERATION_TYPE, map
				.get(CherryConstants.OPERATION_TYPE));
		// 部门类型
		paramMap.put(CherryConstants.DEPART_TYPE, map
				.get(CherryConstants.DEPART_TYPE));
		// 语言类型
		paramMap.put(CherryConstants.SESSION_LANGUAGE, map
				.get(CherryConstants.SESSION_LANGUAGE));
		paramMap.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLCM00.getNoTestCounterList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 取得岗位类别信息List
	 * 
	 * @param map 查询条件
	 * @return 岗位类别信息List
	 */
	public List<Map<String, Object>> getPositionCategoryList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM00.getPositionCategoryList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 取得最大岗位级别
	 * 
	 * @param map 查询条件
	 * @return 最大岗位级别
	 */
	public String getMaxPosCategoryGrade(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM00.getMaxPosCategoryGrade");
		return (String)baseServiceImpl.get(paramMap);
	}
	/**
	 * 取得区域名
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public String getRegionName(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID,"BINOLCM00.getRegionName");
		return ConvertUtil.getString(baseServiceImpl.get(map));
	}
	
	/**
	 * 取得渠道名
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public String getChannelName(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID,"BINOLCM00.getChannelName");
		return ConvertUtil.getString(baseServiceImpl.get(map));
	}
	
	/**
	 * 取得仓库名
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public String getDepotName(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID,"BINOLCM00.getDepotName");
		return ConvertUtil.getString(baseServiceImpl.get(map));
	}
	
	/**
	 * 取得逻辑仓库名
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public String getLogicInventoryName(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID,"BINOLCM00.getLogicInventoryName");
		return ConvertUtil.getString(baseServiceImpl.get(map));
	}
	
	/**
	 * 取得部门名
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public String getDepartName(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID,"BINOLCM00.getDepartName");
		return ConvertUtil.getString(baseServiceImpl.get(map));
	}
	
	/**
	 * 根据部门ID取得部门的测试区分
	 * 
	 * 
	 * */
	public String getDepartTestType(String departId){
		return ConvertUtil.getString(baseServiceImpl.get(departId, "BINOLCM00.getDepartTestType"));
	}
	
    /**
     * 取得登陆用户名
     * 
     * @param map
     * 
     * @return
     */
    public String getLoginName(String employeeID){
        return ConvertUtil.getString(baseServiceImpl.get(employeeID, "BINOLCM00.getLoginName"));
    }
}
