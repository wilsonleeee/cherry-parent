/*
 * @(#)BINOLBSDEP08_Action.java     1.0 2011.2.10
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
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.dep.bl.BINOLBSDEP07_BL;
import com.cherry.bs.dep.bl.BINOLBSDEP08_BL;
import com.cherry.bs.dep.form.BINOLBSDEP08_Form;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 组织编辑画面Action
 * 
 * @author WangCT
 * @version 1.0 2011.2.10
 */
@SuppressWarnings("unchecked")
public class BINOLBSDEP08_Action extends BaseAction implements ModelDriven<BINOLBSDEP08_Form> {

	private static final long serialVersionUID = -2893107744406874648L;

	/** 组织编辑画面Form */
	private BINOLBSDEP08_Form form = new BINOLBSDEP08_Form();
	
	/** 组织编辑画面BL */
	@Resource
	private BINOLBSDEP08_BL binOLBSDEP08_BL;
	
	/** 组织添加画面BL */
	@Resource
	private BINOLBSDEP07_BL binOLBSDEP07_BL;
	
	/**
	 * 
	 * 组织编辑画面初期处理
	 * 
	 * @return 组织编辑画面 
	 */
	public String init() {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 组织ID
		map.put(CherryConstants.ORGANIZATIONINFOID, form.getOrganizationInfoId());
		organization = binOLBSDEP08_BL.getOrganization(map);
		if(organization == null || organization.isEmpty()) {
			this.addActionError(getText("EBS00013"));
            return SUCCESS;
		}
		return SUCCESS;
		
	}
	
	/**
	 * 更新组织处理
	 * 
	 * @return 更新完了画面
	 */
	public String updateOrganization() throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 组织ID
		map.put("organizationInfoId", form.getOrganizationInfoId());
		// 更新日时
		map.put("modifyTime", form.getModifyTime());
		// 更新次数
		map.put("modifyCount", form.getModifyCount());
		// 组织名称中文
		if(form.getOrgNameChinese() != null && !"".equals(form.getOrgNameChinese())) {
			map.put("orgNameChinese", form.getOrgNameChinese());
		}
		// 组织名称外文
		if(form.getOrgNameForeign() != null && !"".equals(form.getOrgNameForeign())) {
			map.put("orgNameForeign", form.getOrgNameForeign());
		}
		// 组织名称外文简称
		if(form.getOrgNameForeignShort() != null && !"".equals(form.getOrgNameForeignShort())) {
			map.put("orgNameForeignShort", form.getOrgNameForeignShort());
		}
		// 组织名称中文简称
		if(form.getOrgNameShort() != null && !"".equals(form.getOrgNameShort())) {
			map.put("orgNameShort", form.getOrgNameShort());
		}
		// 成立日期
		if(form.getFoundationDate() != null && !"".equals(form.getFoundationDate())) {
			map.put("foundationDate", form.getFoundationDate());
		}
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
		// 更新程序名
		map.put(CherryConstants.UPDATEPGM, "BINOLBSDEP08");
		try {
			// 更新组织处理
			binOLBSDEP08_BL.tran_updateOrganization(map);
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
	 * 更新组织处理前参数验证处理
	 * 
	 */
	public void validateUpdateOrg() throws Exception {
		
		// 组织名称中文必须入力验证
		if(CherryChecker.isNullOrEmpty(form.getOrgNameChinese())) {
			this.addFieldError("orgNameChinese", getText("ECM00009",new String[]{getText("PBS00029")}));
		} else {
			// 组织名称中文不能超过50位验证
			if(form.getOrgNameChinese().length() > 50) {
				this.addFieldError("orgNameChinese", getText("ECM00020",new String[]{getText("PBS00029"),"50"}));
			}
		}
		if(form.getOrgNameShort() != null && !"".equals(form.getOrgNameShort())) {
			// 组织名称中文简称不能超过20位验证
			if(form.getOrgNameShort().length() > 20) {
				this.addFieldError("orgNameShort", getText("ECM00020",new String[]{getText("PBS00030"),"20"}));
			}
		}
		if(form.getOrgNameForeign() != null && !"".equals(form.getOrgNameForeign())) {
			// 组织名称外文不能超过50位验证
			if(form.getOrgNameForeign().length() > 50) {
				this.addFieldError("orgNameForeign", getText("ECM00020",new String[]{getText("PBS00031"),"50"}));
			}
		}
		if(form.getOrgNameForeignShort() != null && !"".equals(form.getOrgNameForeignShort())) {
			// 组织名称外文简称不能超过20位验证
			if(form.getOrgNameForeignShort().length() > 20) {
				this.addFieldError("orgNameForeignShort", getText("ECM00020",new String[]{getText("PBS00032"),"20"}));
			}
		}
		if(form.getFoundationDate() != null && !"".equals(form.getFoundationDate())) {
			// 成立日期日期格式验证
			if(!CherryChecker.checkDate(form.getFoundationDate())) {
				this.addFieldError("foundationDate", getText("ECM00022",new String[]{getText("PBS00008")}));
			}
		}
		if(!this.hasFieldErrors()) {
			Map<String, Object> map = new HashMap<String, Object>();
			// 组织名称中文
			map.put("orgNameChinese", form.getOrgNameChinese());
			// 判断组织名称是否已经存在
			String orgId2 = binOLBSDEP07_BL.getOrganizationInfoID(map);
			if(orgId2 != null && !form.getOrganizationInfoId().equals(orgId2)) {
				this.addFieldError("orgNameChinese", getText("EBS00012"));
			}
		}
		
	}
	
	/** 组织信息 */
	private Map organization;

	public Map getOrganization() {
		return organization;
	}

	public void setOrganization(Map organization) {
		this.organization = organization;
	}

	@Override
	public BINOLBSDEP08_Form getModel() {
		return form;
	}

}
