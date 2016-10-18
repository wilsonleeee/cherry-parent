/*
 * @(#)PrivilegeConstants.java     1.0 2010/10/27
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

package com.cherry.pl.common;

/**
 * 
 * 权限共通常量
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
public class PrivilegeConstants {
	
	/** 角色ID  */
	public static final String ROLE_ID = "roleId";
	/** 开始日期  */
	public static final String START_DATE = "startDate";
	/** 有效期限  */
	public static final String EXPIRE_DATE = "expireDate";
	/** 权限区分  */
	public static final String PRIVILEGE_FLAG = "privilegeFlag";
	/** 角色分类  */
	public static final String ROLE_KIND = "roleKind";
	/** 角色名称或描述  */
	public static final String ROLE_KW = "roleKw";
	/** 角色名称  */
	public static final String ROLE_NAME = "roleName";
	/** 角色描述  */
	public static final String DECRIPTION = "decription";
	/** 角色分类名称  */
	public static final String ROLE_KIND_TEXT = "roleKindText";
	/** 复选框  */
	public static final String CHECKBOX = "checkbox";
	/** 资源类型  */
	public static final String RESOURCE_TYPE = "resourceType";
	/** 子系统ID */
	public static final String SUB_SYS_ID = "subSysId";
	/** 模块ID */
	public static final String MODULE_ID = "moduleId";
	/** 功能ID */
	public static final String FUNCTION_ID = "functionId";
	/** 画面ID */
	public static final String PAGE_ID = "pageId";
	/** 控件ID */
	public static final String CONTROL_ID = "controlId";
	/** 子系统名称  */
	public static final String SUB_SYS_NAME = "subSysName";
	/** 模块名称  */
	public static final String MODULE_NAME = "moduleName";
	/** 功能名称  */
	public static final String FUNCTION_NAME = "functionName";
	/** 画面名称  */
	public static final String PAGE_NAME = "pageName";
	/** 控件名称  */
	public static final String CONTROL_NAME = "controlName";
	/** 资源ID */
	public static final String RESOURCE_ID = "resourceId";
	
	/** 复选框选中状态  */
	public static boolean CHECKBOX_CHECKED = true;
	/** 角色分类（组织角色）  */
	public static final String ROLE_KIND_0 = "0";
	/** 角色分类（岗位类别角色）  */
	public static final String ROLE_KIND_1 = "1";
	/** 角色分类（岗位角色）  */
	public static final String ROLE_KIND_2 = "2";
	/** 角色分类（用户角色）  */
	public static final String ROLE_KIND_3 = "3";
	/** 资源类型（子系统）  */
	public static final String RESOURCE_TYPE_0 = "0";
	/** 资源类型（模块）  */
	public static final String RESOURCE_TYPE_1 = "1";
	/** 资源类型（功能）  */
	public static final String RESOURCE_TYPE_2 = "2";
	/** 资源类型（画面）  */
	public static final String RESOURCE_TYPE_3 = "3";
	/** 资源类型（控件）  */
	public static final String RESOURCE_TYPE_4 = "4";
	/** 有效期限最大值  */
	public static final String EXPIRE_DATE_VALUE = "2100-01-01";
	  
//	// 角色分类
//	public static enum ROLE_KIND_ENUM {
//		
//		OrganizationRole("0","部门角色"),
//		PositionCategoryRole("1","岗位类别角色"),
//		PositionRole("2","岗位角色"),
//		UserRole("3","用户角色");
//		
//		public static String getText(Object key) {
//			if(key == null)
//				return null;
//			ROLE_KIND_ENUM[] roleKinds = ROLE_KIND_ENUM.values();
//			for(ROLE_KIND_ENUM roleKind : roleKinds) {
//				if(roleKind.getKey().equals(key.toString())) {
//					return roleKind.getText();
//				}
//			}
//			return null;
//		}
//		
//		ROLE_KIND_ENUM(String key, String text) {
//			this.key = key;
//			this.text = text;
//		}
//		
//		public String getKey() {
//			return key;
//		}
//		public String getText() {
//			return text;
//		}
//
//		private String key;
//		private String text;
//	}
//	
//	// 资源类型
//	public static enum RESOURCE_TYPE_ENUM {
//		
//		subSysId("0","subSysId"),
//		moduleId("1","moduleId"),
//		functionId("2","functionId"),
//		pageId("3","pageId");
//		
//		public static String getKey(Object code) {
//			if(code == null)
//				return null;
//			RESOURCE_TYPE_ENUM[] resourceTypes = RESOURCE_TYPE_ENUM.values();
//			for(RESOURCE_TYPE_ENUM resourceType : resourceTypes) {
//				if(resourceType.getCode().equals(code.toString())) {
//					return resourceType.getKey();
//				}
//			}
//			return null;
//		}
//		
//		RESOURCE_TYPE_ENUM(String code, String key) {
//			this.code = code;
//			this.key = key;
//		}
//		
//		public String getCode() {
//			return code;
//		}
//
//		public String getKey() {
//			return key;
//		}
//
//		private String code;
//		private String key;
//		
//	}

}
