/*
 * @(#)BINOLPLRLM04_Action.java     1.0 2010/10/27
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

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.pl.common.PrivilegeConstants;
import com.cherry.pl.rlm.bl.BINOLPLRLM02_BL;
import com.cherry.pl.rlm.bl.BINOLPLRLM04_BL;

/**
 * 更新角色Action
 * @author WangCT
 * @version 1.0 2010.10.27
 */
@SuppressWarnings("unchecked")
public class BINOLPLRLM04_Action extends BaseAction {
	
	private static final long serialVersionUID = -7155300797866611611L;

	/** 更新角色BL */
	@Resource
	private BINOLPLRLM04_BL binOLPLRLM04_BL;
	
	/** 添加角色BL */
	@Resource
	private BINOLPLRLM02_BL binOLPLRLM02_BL;
	
	/**
	 * 更新角色参数验证处理
	 * 
	 */
	public void validateUpdateRole() {
		
		// 角色名称为空的场合
		if(roleName == null || "".equals(roleName)) {
			this.addFieldError("roleName", getText("ECM00009",new String[]{getText("PPL00003")}));
		} else {
			// 角色名称大于50位的场合
			if(roleName.length() > 50) {
				this.addFieldError("roleName", getText("ECM00020",new String[]{getText("PPL00003"),"50"}));
			} else {
				Map<String, Object> map = new HashMap<String, Object>();
				// 登陆用户信息
				UserInfo userInfo = (UserInfo) session
						.get(CherryConstants.SESSION_USERINFO);
				// 所属组织
				map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
				// 所属品牌
				if(brandInfoId != null && !"".equals(brandInfoId)) {
					map.put(CherryConstants.BRANDINFOID, brandInfoId);
				} else {
					map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
				}
				// 角色名称
				map.put(PrivilegeConstants.ROLE_NAME, roleName);
				// 查询指定角色是否存在
				Map<String, Object> roleMap = binOLPLRLM02_BL.getRoleByRoleName(map);
				// 存在场合
				if(roleMap != null && Integer.parseInt(roleId) != (Integer)roleMap.get(PrivilegeConstants.ROLE_ID)) {
					this.addFieldError("roleName", getText("EPL00002",new String[]{}));
				}
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
	}
	
	/**
	 * 更新角色初期表示
	 * 
	 * @return String 
	 */
	public String init() {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 角色信息ID
		map.put(PrivilegeConstants.ROLE_ID, roleId);
		// 取得角色信息
		roleInfo = binOLPLRLM04_BL.getRoleInfo(map);
		return SUCCESS;
	}
	
	/**
	 * 更新角色处理
	 * 
	 * @return String 
	 */
	public String updateRole() throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
		// 更新程序名
		map.put(CherryConstants.UPDATEPGM, "BINOLPLRLM04");
		// 角色信息ID
		map.put(PrivilegeConstants.ROLE_ID, roleId);
		// 角色名称
		map.put(PrivilegeConstants.ROLE_NAME, roleName);
		// 角色描述
		map.put(PrivilegeConstants.DECRIPTION, decription);
		// 角色分类
		map.put(PrivilegeConstants.ROLE_KIND, roleKind);
		// 更新日时
		map.put(CherryConstants.MODIFY_TIME, modifyTime);
		// 更新次数
		map.put(CherryConstants.MODIFY_COUNT, modifyCount);
		try {
			// 更新角色处理
			binOLPLRLM04_BL.tran_updateRole(map);
		} catch (Exception e) {
			// 更新失败场合
			if(e instanceof CherryException){
                CherryException temp = (CherryException)e;            
                this.addActionError(temp.getErrMessage());                
            }else{
                throw e;
            }    
		}
		
		return SUCCESS;
	}
	
	/** 角色信息ID */
	private String roleId;
	
	/** 角色名称 */
	private String roleName;
	
	/** 角色描述 */
	private String decription;
	
	/** 角色分类 */
	private String roleKind;
	
	/** 更新日时 */
	private String modifyTime;
	
	/** 更新次数 */
	private String modifyCount;
	
	/** 角色信息 */
	private Map roleInfo;
	
	/** 品牌ID */
	private String brandInfoId;

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
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

	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getModifyCount() {
		return modifyCount;
	}

	public void setModifyCount(String modifyCount) {
		this.modifyCount = modifyCount;
	}

	public Map getRoleInfo() {
		return roleInfo;
	}

	public void setRoleInfo(Map roleInfo) {
		this.roleInfo = roleInfo;
	}

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

}
