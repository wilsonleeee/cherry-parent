/*
 * @(#)BINOLPLSCF02_Action.java     1.0 2010/10/27
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

package com.cherry.pl.scf.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.pl.scf.bl.BINOLPLSCF02_BL;
import com.cherry.pl.scf.form.BINOLPLSCF02_Form;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 审核审批配置一览Action
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
@SuppressWarnings("unchecked")
public class BINOLPLSCF02_Action extends BaseAction implements ModelDriven<BINOLPLSCF02_Form> {

	private static final long serialVersionUID = 5883815229843815917L;
	
	/** 审核审批配置一览BL */
	@Resource
	private BINOLPLSCF02_BL binOLPLSCF02_BL;
	
	/** 共通BL */
	@Resource
	private BINOLCM05_BL binOLCM05_BL;
	
	/** 审核审批配置一览Form */
	private BINOLPLSCF02_Form form = new BINOLPLSCF02_Form();
	
	/**
	 * 审核审批配置一览初期表示
	 * 
	 * @return 审核审批配置一览画面
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
		// 总部用户登录的时候
		if (CherryConstants.BRAND_INFO_ID_VALUE == userInfo.getBIN_BrandInfoID()) {
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
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
//		// 查询业务类型List
//		bussinessTypeCodeList = binOLPLSCF02_BL.getBussinessTypeCodeList(map);
		return SUCCESS;
	}
	
	/**
	 * 根据品牌查询业务类型List
	 * 
	 * @return null
	 */
	public String searchBuType() throws Exception {
		
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
		// 所属品牌
		map.put("brandInfoId", form.getBrandInfoId());
		// 查询业务类型List
		List<Map<String, Object>> list = binOLPLSCF02_BL.getBussinessTypeCodeList(map);
		
		ConvertUtil.setResponseByAjax(response, list);
		return null;
	}
	
	/**
	 * 取得审核审批配置一览
	 * 
	 * @return 审核审批配置一览画面
	 */
	public String auditList() {
		
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
		// 所属品牌
		if(form.getBrandInfoId() != null && !"".equals(form.getBrandInfoId())) {
			map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		} else {
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		}
		// 业务类型代码
		if(form.getBussinessTypeCode() != null && !"".equals(form.getBussinessTypeCode())) {
			map.put("bussinessTypeCode", form.getBussinessTypeCode());
		}
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		
		// 查询审核审批配置信息总数
		int count = binOLPLSCF02_BL.getAuditPrivilegeCount(map);
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if(count != 0) {
			// 查询审核审批配置信息List
			auditPrivilegeList = binOLPLSCF02_BL.getAuditPrivilegeList(map);
		}
		
		return SUCCESS;
	}
	
	/** 品牌List */
	private List<Map<String, Object>> brandInfoList;
	
	/** 业务类型List */
	private List<Map<String, Object>> bussinessTypeCodeList;
	
	/** 审核审批配置信息List */
	private List<Map<String, Object>> auditPrivilegeList;

	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}

	public List<Map<String, Object>> getBussinessTypeCodeList() {
		return bussinessTypeCodeList;
	}

	public void setBussinessTypeCodeList(
			List<Map<String, Object>> bussinessTypeCodeList) {
		this.bussinessTypeCodeList = bussinessTypeCodeList;
	}

	public List<Map<String, Object>> getAuditPrivilegeList() {
		return auditPrivilegeList;
	}

	public void setAuditPrivilegeList(List<Map<String, Object>> auditPrivilegeList) {
		this.auditPrivilegeList = auditPrivilegeList;
	}

	@Override
	public BINOLPLSCF02_Form getModel() {
		return form;
	}

}
