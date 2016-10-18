/*
 * @(#)BINOLPLSCF04_Action.java     1.0 2010/10/27
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
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.Bean2Map;
import com.cherry.pl.scf.bl.BINOLPLSCF03_BL;
import com.cherry.pl.scf.bl.BINOLPLSCF04_BL;
import com.cherry.pl.scf.form.BINOLPLSCF04_Form;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 更新审核审批配置信息Action
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
@SuppressWarnings("unchecked")
public class BINOLPLSCF04_Action extends BaseAction implements ModelDriven<BINOLPLSCF04_Form> {

	private static final long serialVersionUID = -4529496468454375413L;
	
	/** 更新审核审批配置信息BL */
	@Resource
	private BINOLPLSCF04_BL binOLPLSCF04_BL;
	
	/** 添加审核审批配置信息BL */
	@Resource
	private BINOLPLSCF03_BL binOLPLSCF03_BL;
	
//	/** 审核审批配置一览BL */
//	@Resource
//	private BINOLPLSCF02_BL binOLPLSCF02_BL;
	
	/** 共通BL */
	@Resource
	private BINOLCM05_BL binOLCM05_BL;
	
	/** 更新审核审批配置信息Form */
	private BINOLPLSCF04_Form form = new BINOLPLSCF04_Form();
	
	/**
	 * 更新审核审批配置信息画面初期表示处理
	 * 
	 * @return 更新审核审批配置信息画面
	 */
	public String init() {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 审核审批配置信息ID
		map.put("auditPrivilegeId", form.getAuditPrivilegeId());
		// 查询审核审批配置信息
		auditInfo = binOLPLSCF04_BL.getAuditInfo(map);
		// 查询审核审批配置信息不存在的场合
		if(auditInfo == null) {
			this.addActionError(getText("EPL00011"));    
            return SUCCESS;
		}
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
		map.put(CherryConstants.BRANDINFOID, auditInfo.get("brandInfoId"));
//		// 查询业务类型List
//		bussinessTypeCodeList = binOLPLSCF02_BL.getBussinessTypeCodeList(map);
		// 发起者身份类型
		map.put("type", auditInfo.get("initiatorType"));
		// 发起者List
		initiatorList = binOLPLSCF03_BL.getCodeByType(map);
		// 审核者身份类型
		map.put("type", auditInfo.get("auditorType"));
		// 审核者List
		auditorList = binOLPLSCF03_BL.getCodeByType(map);
		return SUCCESS;
	}
	
	/**
	 * 更新审核审批配置信息处理
	 * 
	 * @return 更新完了画面
	 */
	public String updateAudit() throws Exception {
		
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属品牌
		if(form.getBrandInfoId() != null && !"".equals(form.getBrandInfoId())) {
			map.put("brandInfoId", form.getBrandInfoId());
		} else {
			map.put("brandInfoId", userInfo.getBIN_BrandInfoID());
		}
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
		// 更新程序名
		map.put(CherryConstants.UPDATEPGM, "BINOLPLSCF04");
		
		try {
			// 更新审核审批配置信息处理
			binOLPLSCF04_BL.tran_updateAudit(map);
		} catch (Exception e) {
			// 更新失败场合
			if(e instanceof CherryException){
                CherryException temp = (CherryException)e;            
                this.addActionError(temp.getErrMessage());    
                return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
            }else{
                throw e;
            }    
		}
		this.addActionMessage(getText("ICM00001"));
		return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
	}
	
	/**
	 * 更新审核审批配置信息处理前参数验证处理
	 * 
	 */
	public void validateUpdateAudit() throws Exception {
		
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
			if(auditId != null && !form.getAuditPrivilegeId().equals(auditId)) {
				this.addActionError(getText("EPL00010"));
			}
		}
	}
	
	/** 更新审核审批配置信息 */
	private Map auditInfo;
	
	/** 品牌List */
	private List<Map<String, Object>> brandInfoList;
	
	/** 业务类型List */
	private List<Map<String, Object>> bussinessTypeCodeList;
	
	/** 发起者List */
	private List<Map<String, Object>> initiatorList;
	
	/** 审核者List */
	private List<Map<String, Object>> auditorList;

	public Map getAuditInfo() {
		return auditInfo;
	}

	public void setAuditInfo(Map auditInfo) {
		this.auditInfo = auditInfo;
	}

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

	public List<Map<String, Object>> getInitiatorList() {
		return initiatorList;
	}

	public void setInitiatorList(List<Map<String, Object>> initiatorList) {
		this.initiatorList = initiatorList;
	}

	public List<Map<String, Object>> getAuditorList() {
		return auditorList;
	}

	public void setAuditorList(List<Map<String, Object>> auditorList) {
		this.auditorList = auditorList;
	}

	@Override
	public BINOLPLSCF04_Form getModel() {
		return form;
	}

}
