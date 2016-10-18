/*
 * @(#)BINOLPLRLM01_Form.java     1.0 2010/10/27
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

package com.cherry.pl.rlm.form;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cherry.cm.form.DataTable_BaseForm;

/**
 * 角色一览Form
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
public class BINOLPLRLM01_Form extends DataTable_BaseForm {
	
	/** 角色信息List */
	private List<Map<String, Object>> roleInfoList = new ArrayList<Map<String, Object>>();
	
	/** 角色分类 */
	private String roleKind;
	
	/** 角色名称或描述 */
	private String roleKw;
	
	/** 品牌ID */
	private String brandInfoId;

	public List<Map<String, Object>> getRoleInfoList() {
		return roleInfoList;
	}

	public void setRoleInfoList(List<Map<String, Object>> roleInfoList) {
		this.roleInfoList = roleInfoList;
	}

	public String getRoleKind() {
		return roleKind;
	}

	public void setRoleKind(String roleKind) {
		this.roleKind = roleKind;
	}

	public String getRoleKw() {
		return roleKw;
	}

	public void setRoleKw(String roleKw) {
		this.roleKw = roleKw;
	}

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}

}
