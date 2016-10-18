/*  
 * @(#)RoleInfo.java     1.0 2011/05/31      
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
package com.cherry.cm.cmbeans;

/**
 * 角色信息 
 * @author dingyc
 *
 */
public class RoleInfo implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6519985594803602973L;
	/**
	 * 角色ID
	 */
	private int BIN_RoleID;
	/**
	 * 角色名
	 */
	private String RoleName;
	/**
	 * 角色类型
	 */
	private int RoleKind;
	
	/**
	 * 授权类型
	 */
	private String PrivilegeFlag;
	/**
	 * @return the bIN_RoleID
	 */
	public int getBIN_RoleID() {
		return BIN_RoleID;
	}
	/**
	 * @param bIN_RoleID the bIN_RoleID to set
	 */
	public void setBIN_RoleID(int bIN_RoleID) {
		BIN_RoleID = bIN_RoleID;
	}
	/**
	 * @return the roleName
	 */
	public String getRoleName() {
		return RoleName;
	}
	/**
	 * @param roleName the roleName to set
	 */
	public void setRoleName(String roleName) {
		RoleName = roleName;
	}
	/**
	 * @return the roleKind
	 */
	public int getRoleKind() {
		return RoleKind;
	}
	/**
	 * @param roleKind the roleKind to set
	 */
	public void setRoleKind(int roleKind) {
		RoleKind = roleKind;
	}
	/**
	 * @return the privilegeFlag
	 */
	public String getPrivilegeFlag() {
		return PrivilegeFlag;
	}
	/**
	 * @param privilegeFlag the privilegeFlag to set
	 */
	public void setPrivilegeFlag(String privilegeFlag) {
		PrivilegeFlag = privilegeFlag;
	}
}
