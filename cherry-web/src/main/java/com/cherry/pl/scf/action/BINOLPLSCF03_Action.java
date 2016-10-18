/*
 * @(#)BINOLPLSCF03_Action.java     1.0 2010/10/27
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
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.pl.scf.bl.BINOLPLSCF03_BL;
import com.cherry.pl.scf.form.BINOLPLSCF03_Form;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 添加审核审批配置信息Action
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
@SuppressWarnings("unchecked")
public class BINOLPLSCF03_Action extends BaseAction implements ModelDriven<BINOLPLSCF03_Form> {

	private static final long serialVersionUID = 4749916594099374563L;
	
	/** 添加审核审批配置信息Form */
	private BINOLPLSCF03_Form form = new BINOLPLSCF03_Form();
	
	/** 添加审核审批配置信息BL */
	@Resource
	private BINOLPLSCF03_BL binOLPLSCF03_BL;
	
//	/** 审核审批配置一览BL */
//	@Resource
//	private BINOLPLSCF02_BL binOLPLSCF02_BL;
	
	/** 共通BL */
	@Resource
	private BINOLCM05_BL binOLCM05_BL;
	
	/**
	 * 添加审核审批配置信息画面初期表示处理
	 * 
	 * @return 添加审核审批配置信息画面
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
	 * 根据身份类型取得身份信息
	 * 
	 * @return null
	 */
	public String searchCodeByType() throws Exception {
		
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
		// 品牌ID
		if(form.getBrandInfoId() != null && !"".equals(form.getBrandInfoId())) {
			map.put("brandInfoId", form.getBrandInfoId());
		} else {
			map.put("brandInfoId", userInfo.getBIN_BrandInfoID());
		}
		// 身份类型
		if(form.getAuditorType() != null && !"".equals(form.getAuditorType())) {
			map.put("type", form.getAuditorType());
		} else if(form.getInitiatorType() != null && !"".equals(form.getInitiatorType())) {
			map.put("type", form.getInitiatorType());
		}
		// 根据身份类型取得身份信息
		List<Map<String, Object>> list = binOLPLSCF03_BL.getCodeByType(map);
		
		ConvertUtil.setResponseByAjax(response, list);
		
		return null;
	}
	
	/**
	 * 添加审核审批配置信息处理
	 * 
	 * @return 添加完了画面
	 */
	public String addAudit() throws Exception {
		
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		if(form.getBrandInfoId() != null && !"".equals(form.getBrandInfoId())) {
			map.put("brandInfoId", form.getBrandInfoId());
		} else {
			map.put("brandInfoId", userInfo.getBIN_BrandInfoID());
		}
		// 作成者
		map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
		// 作成程序名
		map.put(CherryConstants.CREATEPGM, "BINOLPLSCF03");
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
		// 更新程序名
		map.put(CherryConstants.UPDATEPGM, "BINOLPLSCF03");
		// 添加审核审批配置信息处理
		binOLPLSCF03_BL.tran_addAudit(map);
		this.addActionMessage(getText("ICM00001"));
		return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
	}
	
	/**
	 * 添加审核审批配置信息处理前参数验证处理
	 * 
	 */
	public void validateAddAudit() throws Exception {
		
		// 业务类型必须入力验证
		if(CherryChecker.isNullOrEmpty(form.getBussinessTypeCode())) {
			this.addFieldError("bussinessTypeCode", getText("ECM00009",new String[]{getText("PPL00014")}));
		}
		// 发起者身份类型必须入力验证
		if(CherryChecker.isNullOrEmpty(form.getInitiatorType())) {
			this.addFieldError("initiatorType", getText("ECM00009",new String[]{getText("PPL00015")}));
		}
		// 发起者必须入力验证
		if(CherryChecker.isNullOrEmpty(form.getInitiatorID())) {
			this.addFieldError("initiatorID", getText("ECM00009",new String[]{getText("PPL00016")}));
		}
		// 审核者身份类型必须入力验证
		if(CherryChecker.isNullOrEmpty(form.getAuditorType())) {
			this.addFieldError("auditorType", getText("ECM00009",new String[]{getText("PPL00017")}));
		}
		// 审核者必须入力验证
		if(CherryChecker.isNullOrEmpty(form.getAuditorID())) {
			this.addFieldError("auditorID", getText("ECM00009",new String[]{getText("PPL00018")}));
		}
		if(!this.hasFieldErrors()) {
			Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 所属品牌
			if(form.getBrandInfoId() != null && !"".equals(form.getBrandInfoId())) {
				map.put("brandInfoId", form.getBrandInfoId());
			} else {
				map.put("brandInfoId", userInfo.getBIN_BrandInfoID());
			}
			// 查询审核审批配置信息是否存在
			String auditId = binOLPLSCF03_BL.getAuditVal(map);
			if(auditId != null && !"".equals(auditId)) {
				this.addActionError(getText("EPL00010"));
			}
		}
	}
	
	/** 品牌List */
	private List<Map<String, Object>> brandInfoList;
	
	/** 业务类型List */
	private List<Map<String, Object>> bussinessTypeCodeList;

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

	@Override
	public BINOLPLSCF03_Form getModel() {
		return form;
	}

}
