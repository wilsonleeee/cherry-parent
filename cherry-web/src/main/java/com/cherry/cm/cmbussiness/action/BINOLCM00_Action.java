/*	
 * @(#)BINOLCM00_Action.java     1.0 2010/10/12		
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
package com.cherry.cm.cmbussiness.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.util.ConvertUtil;

/**
 * 共通Action
 * 
 * @author lipc
 * 
 */
public class BINOLCM00_Action extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1674944773063439781L;

	@Resource
	private BINOLCM00_BL binolcm00BL;

	/** 区域ID */
	private String regionId;

	/** 品牌ID */
	private String brandInfoId;

	/** 部门ID */
	private String organizationId;

	/** 部门类型 */
	private String departType;

	/** 业务类型 */
	private String busiType;

	/** 操作类型 */
	private String operationType;

	public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public String getDepartType() {
		return departType;
	}

	public void setDepartType(String departType) {
		this.departType = departType;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public String getBusiType() {
		return busiType;
	}

	public void setBusiType(String busiType) {
		this.busiType = busiType;
	}

	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}
	
	/**
	 * AJAX 取得区域下属LIST并返回JSON对象
	 * 
	 * @throws Exception
	 */
	public void querySubRegion() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 区域Id
		map.put("regionId", regionId);
		// 取得区域下属list
		List<Map<String, Object>> list = binolcm00BL.getChildRegionList(map);
		// 响应JSON对象
		ConvertUtil.setResponseByAjax(response, list);
	}

	/**
	 * AJAX 取得部门下岗位LIST并返回JSON对象
	 * 
	 * @throws Exception
	 */
	public void queryPosition() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 部门Id
		map.put(CherryConstants.ORGANIZATIONID, organizationId);
		// 取得岗位LIST
		List<Map<String, Object>> list = binolcm00BL.getPositionList(map);
		// 响应JSON对象
		ConvertUtil.setResponseByAjax(response, list);
	}

	/**
	 * AJAX 取得权限部门LIST并返回JSON对象
	 * 
	 * @throws Exception
	 */
	public void queryDepart() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 业务类型
		map.put(CherryConstants.BUSINESS_TYPE, busiType);
		// 操作类型
		map.put(CherryConstants.OPERATION_TYPE, operationType);
		// 部门类型
		map.put(CherryConstants.DEPART_TYPE, departType);
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 取得部门List
		List<Map<String, Object>> list = binolcm00BL.getDepartList(map);
		// 响应JSON对象
		ConvertUtil.setResponseByAjax(response, list);
	}

	/**
	 * AJAX 取得部门LIST并返回JSON对象
	 * 
	 * @throws Exception
	 */
	public void queryOrg() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 组织ID
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 部门ID
		map.put(CherryConstants.BRANDINFOID, brandInfoId);
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 取得部门List
		List<Map<String, Object>> list = binolcm00BL.getOrgList(map);
		// 响应JSON对象
		ConvertUtil.setResponseByAjax(response, list);
	}

	/**
	 * AJAX 取得实体仓库LIST并返回JSON对象
	 * 
	 * @throws Exception
	 */
	public void queryInventory() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 业务类型
		map.put(CherryConstants.BUSINESS_TYPE, busiType);
		// 操作类型
		map.put(CherryConstants.OPERATION_TYPE, operationType);
		// 部门类型
		map.put(CherryConstants.DEPART_TYPE, departType);
		// 部门ID
		map.put(CherryConstants.ORGANIZATIONID, organizationId);
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 取得部门List
		List<Map<String, Object>> list = binolcm00BL.getInventoryList(map);
		// 响应JSON对象
		ConvertUtil.setResponseByAjax(response, list);
	}
	/**
	 * 空请求维持session时效
	 */
	public void refreshSessionRequest() throws Exception {}
}
