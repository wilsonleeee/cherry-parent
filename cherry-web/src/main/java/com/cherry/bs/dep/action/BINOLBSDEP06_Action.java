/*
 * @(#)BINOLBSDEP06_Action.java     1.0 2011.2.10
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

package com.cherry.bs.dep.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.dep.bl.BINOLBSDEP06_BL;
import com.cherry.bs.dep.form.BINOLBSDEP06_Form;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 组织一览画面Action
 * 
 * @author WangCT
 * @version 1.0 2011.2.10
 */
public class BINOLBSDEP06_Action extends BaseAction implements ModelDriven<BINOLBSDEP06_Form> {

	private static final long serialVersionUID = 7368596508731609952L;
	
	/** 组织一览画面Form */
	private BINOLBSDEP06_Form form = new BINOLBSDEP06_Form();
	
	/** 组织一览画面BL */
	@Resource
	private BINOLBSDEP06_BL binOLBSDEP06_BL;
	
	/**
	 * 组织一览画面初期处理
	 * 
	 * @return 组织一览画面 
	 */
	public String init() {
		
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 组织代号是默认组织代号的场合
		if(CherryConstants.ORGANIZATION_CODE_DEFAULT.equals(userInfo.getOrganizationInfoCode())) {
			isAddOrgButton = "1";
		}
		return SUCCESS;
	}
	
	/**
	 * 查询组织一览处理
	 * 
	 * @return 组织一览画面 
	 */
	public String organizationList() {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		
		// 组织代号不是默认组织代号的场合
		if(!CherryConstants.ORGANIZATION_CODE_DEFAULT.equals(userInfo.getOrganizationInfoCode())) {
			// 组织ID
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		}
		if(form.getOrgNameKw() != null && !"".equals(form.getOrgNameKw())) {
			// 组织名称
			map.put("orgNameKw", form.getOrgNameKw());
		}
		if(form.getValidFlag() != null && !"".equals(form.getValidFlag())) {
			// 有效区分
			map.put("validFlag", form.getValidFlag());
		}
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		// 查询组织总数
		int count = binOLBSDEP06_BL.getOrganizationCount(map);
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if(count != 0) {
			// 取得组织信息List
			organizationList = binOLBSDEP06_BL.getOrganizationList(map);
		}
		return SUCCESS;
	}
	
	/** 组织信息List */
	private List<Map<String, Object>> organizationList;
	
	/** 添加组织按钮是否显示判断用 */
	private String isAddOrgButton;

	public List<Map<String, Object>> getOrganizationList() {
		return organizationList;
	}

	public void setOrganizationList(List<Map<String, Object>> organizationList) {
		this.organizationList = organizationList;
	}

	public String getIsAddOrgButton() {
		return isAddOrgButton;
	}

	public void setIsAddOrgButton(String isAddOrgButton) {
		this.isAddOrgButton = isAddOrgButton;
	}

	@Override
	public BINOLBSDEP06_Form getModel() {
		return form;
	}

}
