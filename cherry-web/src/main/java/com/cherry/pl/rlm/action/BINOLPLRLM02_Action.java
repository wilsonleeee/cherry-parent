/*
 * @(#)BINOLPLRLM02_Action.java     1.0 2010/10/27
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

package com.cherry.pl.rlm.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.pl.common.PrivilegeConstants;
import com.cherry.pl.rlm.bl.BINOLPLRLM02_BL;

/**
 * 添加角色Action
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
@SuppressWarnings("unchecked")
public class BINOLPLRLM02_Action extends BaseAction {
	
	private static final long serialVersionUID = -9194808942591160329L;
	
	/** 添加角色BL */
	@Resource
	private BINOLPLRLM02_BL binOLPLRLM02_BL;
	
	/** 共通BL */
	@Resource
	private BINOLCM05_BL binOLCM05_BL;
	
	/**
	 * 添加角色参数验证处理
	 * 
	 */
	public void validateAddRole() {
		
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 总部的场合
		if(userInfo.getBIN_BrandInfoID() == CherryConstants.BRAND_INFO_ID_VALUE) {
			// 品牌ID
			if(brandId == null || "".equals(brandId)) {
				this.addFieldError("brandId", getText("ECM00009",new String[]{getText("PPL00024")}));
			}
		}
		// 角色名称为空的场合
		if(roleName == null || "".equals(roleName)) {
			this.addFieldError("roleName", getText("ECM00009",new String[]{getText("PPL00003")}));
		} else {
			// 角色名称大于50位的场合
			if(roleName.length() > 50) {
				this.addFieldError("roleName", getText("ECM00020",new String[]{getText("PPL00003"),"50"}));
			}
		}
		// 角色分类为空的场合
		if(roleKind == null || "".equals(roleKind)) {
			this.addFieldError("roleKind", getText("ECM00009",new String[]{getText("PPL00004")}));
		}
		// 角色描述大于100位的场合
		if(decription != null && decription.length() > 100) {
			this.addFieldError("decription", getText("ECM00020",new String[]{getText("PPL00005"),"100"}));
		}
		if(!this.hasFieldErrors()) {
			Map<String, Object> map = new HashMap<String, Object>();
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 所属品牌
			if(brandId != null && !"".equals(brandId)) {
				map.put(CherryConstants.BRANDINFOID, brandId);
			} else {
				map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			}
			// 角色名称
			map.put(PrivilegeConstants.ROLE_NAME, roleName);
			// 查询指定角色是否存在
			Map<String, Object> roleMap = binOLPLRLM02_BL.getRoleByRoleName(map);
			// 存在场合
			if(roleMap != null) {
				this.addFieldError("roleName", getText("EPL00002",new String[]{}));
			}
		}
	}

	/**
	 * 添加角色初期表示
	 * 
	 * @return String 
	 */
	public String init() {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言
		String language = (String)session.get(CherryConstants.SESSION_LANGUAGE);
		if(language != null) {
			map.put(CherryConstants.SESSION_LANGUAGE, language);
		}
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 总部的场合
		if(userInfo.getBIN_BrandInfoID() == CherryConstants.BRAND_INFO_ID_VALUE) {
			// 取得品牌List
			brandInfoList = binOLCM05_BL.getBrandInfoList(map);
			Map<String, Object> brandMap = new HashMap<String, Object>();
			// 品牌ID
			brandMap.put("brandInfoId", CherryConstants.BRAND_INFO_ID_VALUE);
			// 品牌名称
			brandMap.put("brandName", getText("PPL00006"));
			if (null != brandInfoList && !brandInfoList.isEmpty()) {
				brandInfoList.add(0, brandMap);
			} else {
				brandInfoList = new ArrayList<Map<String, Object>>();
				brandInfoList.add(brandMap);
			}
		}
		
		return SUCCESS;
	}
	
	/**
	 * 添加角色处理
	 * 
	 * @return String 
	 */
	public String addRole() {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 作成者
		map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
		// 作成程序名
		map.put(CherryConstants.CREATEPGM, "BINOLPLRLM02");
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
		// 更新程序名
		map.put(CherryConstants.UPDATEPGM, "BINOLPLRLM02");
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		if(brandId != null && !"".equals(brandId)) {
			map.put(CherryConstants.BRANDINFOID, brandId);
		} else {
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		}
		// 角色名称
		map.put(PrivilegeConstants.ROLE_NAME, roleName);
		// 角色描述
		map.put(PrivilegeConstants.DECRIPTION, decription);
		// 角色分类
		map.put(PrivilegeConstants.ROLE_KIND, roleKind);
		// 添加角色处理
		roleId = binOLPLRLM02_BL.tran_addRole(map);
		return SUCCESS;
	}
	
	/** 角色ID */
	private int roleId;
	
	/** 角色名称 */
	private String roleName;
	
	/** 角色描述 */
	private String decription;
	
	/** 角色分类 */
	private String roleKind;
	
	/** 品牌ID */
	private String brandId;
	
	/** 品牌List */
	private List<Map<String, Object>> brandInfoList;

	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}
	
	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getDecription() {
		return decription;
	}

	public void setDecription(String decription) {
		this.decription = decription;
	}

	public String getRoleKind() {
		return roleKind;
	}

	public void setRoleKind(String roleKind) {
		this.roleKind = roleKind;
	}

	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}

}
