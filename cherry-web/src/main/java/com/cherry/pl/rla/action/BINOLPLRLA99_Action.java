/*
 * @(#)BINOLPLRLA99_Action.java     1.0 2010/10/27
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

package com.cherry.pl.rla.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.pl.common.PrivilegeConstants;
import com.cherry.pl.rla.bl.BINOLPLRLA99_BL;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 角色分配共通Action
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
public class BINOLPLRLA99_Action extends BaseAction {
	
	private static final long serialVersionUID = 1L;

	/** 角色分配BL */
	@Resource
	private BINOLPLRLA99_BL binOLPLRLA99_BL;
	
	/** 共通BL */
	@Resource
	private BINOLCM00_BL binOLCM00_BL;
	
	/**
	 * 角色分配处理前的参数验证
	 * 
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public void validateSaveRole() throws Exception {

		// 选中角色信息
        String roleAssign = request.getParameter("roleAssign");
        if(roleAssign != null && !"".equals(roleAssign)) {
        	List<Map<String, Object>> list = (List<Map<String, Object>>)JSONUtil.deserialize(roleAssign);
        	for(int i = 0; i < list.size(); i++) {
        		boolean hasError = false;
        		Map<String, Object> map = list.get(i);
        		// 开始日期
        		String startDate = (String)map.get(PrivilegeConstants.START_DATE);
        		// 有效期限
        		String expireDate = (String)map.get(PrivilegeConstants.EXPIRE_DATE);
        		if(startDate != null && !"".equals(startDate)) {
        			// 日期格式验证
        			if(!CherryChecker.checkDate(startDate)) {
        				this.addFieldError("startDate"+(String)map.get(PrivilegeConstants.ROLE_ID),getText("ECM00008",new String[]{getText("PPL00001")}));
        				hasError = true;
        			}
        		}
        		if(expireDate != null && !"".equals(expireDate)) {
        			// 日期格式验证
        			if(!CherryChecker.checkDate(expireDate)) {
        				this.addFieldError("expireDate"+(String)map.get(PrivilegeConstants.ROLE_ID),getText("ECM00008",new String[]{getText("PPL00002")}));
        				hasError = true;
        			}
        		}
        		if(!hasError && (expireDate != null && !"".equals(expireDate))) {
        			if(startDate == null || "".equals(startDate)) {
        				startDate = binOLPLRLA99_BL.getSysDate().substring(0,10);
        			}
        			// 开始日期在有效期限之后
        			if(CherryChecker.compareDate(startDate, expireDate) > 0) {
        				this.addFieldError("expireDate"+(String)map.get(PrivilegeConstants.ROLE_ID),getText("EPL00003"));
        			}
        		}
        	}
        }
        if(this.hasErrors())
        	// 验证出错时，画面初期表示处理
        	init();
    }

	/**
	 * 角色一览表示
	 * 
	 * @return String 
	 * @throws JSONException 
	 */
	public String init() throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 不是总部的场合
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		} else {
			map.put(CherryConstants.BRANDINFOID, brandInfoId);
		}
		// 组织ID
		map.put(CherryConstants.ORGANIZATIONID, organizationId);
		// 岗位类别ID
		map.put(CherryConstants.POSITIONCATEGORYID, positionCategoryId);
		// 岗位ID
		map.put(CherryConstants.POSITIONID, positionId);
		// 用户ID
		map.put(CherryConstants.USERID, userId);
		if(this.hasErrors()) {
			// 选中角色信息
			map.put("roleAssign", roleAssign);
		}
		// 取得角色信息List
		roleList = binOLPLRLA99_BL.getRoleInfoList(map);
		// 查询假日
		holidays = binOLCM00_BL.getHolidays(map);
		
		return SUCCESS;
	}
	
	/**
	 * 角色分配处理
	 * 
	 * @return String 
	 * @throws Exception 
	 */
	public String saveRole() throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 作成者
		map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
		// 作成程序名
		map.put(CherryConstants.CREATEPGM, "BINOLPLRLA99");
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
		// 更新程序名
		map.put(CherryConstants.UPDATEPGM, "BINOLPLRLA99");
		// 选中角色信息
		map.put("roleAssign", roleAssign);
		// 组织ID
		map.put(CherryConstants.ORGANIZATIONID, organizationId);
		// 岗位类别ID
		map.put(CherryConstants.POSITIONCATEGORYID, positionCategoryId);
		// 岗位ID
		map.put(CherryConstants.POSITIONID, positionId);
		// 用户ID
		map.put(CherryConstants.USERID, userId);
		// 保存分配的角色
		binOLPLRLA99_BL.saveRoleAssigned(map);
		return init();
	}
	
	/** 假日信息 */
	private String holidays;

	/** 选中角色信息 */
	private String roleAssign;
	
	/** 角色信息List */
	private List<Map<String, Object>> roleList;
	
	/** 组织ID */
	private String organizationId;
	
	/** 岗位类别ID */
	private String positionCategoryId;
	
	/** 岗位ID */
	private String positionId;
	
	/** 用户ID */
	private String userId;
	
	/** 品牌ID */
	private String brandInfoId;

	public String getRoleAssign() {
		return roleAssign;
	}

	public void setRoleAssign(String roleAssign) {
		this.roleAssign = roleAssign;
	}

	public List<Map<String, Object>> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<Map<String, Object>> roleList) {
		this.roleList = roleList;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public String getPositionCategoryId() {
		return positionCategoryId;
	}

	public void setPositionCategoryId(String positionCategoryId) {
		this.positionCategoryId = positionCategoryId;
	}

	public String getPositionId() {
		return positionId;
	}

	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getHolidays() {
		return holidays;
	}

	public void setHolidays(String holidays) {
		this.holidays = holidays;
	}

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

}
