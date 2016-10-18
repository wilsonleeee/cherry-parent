/*
 * @(#)BINOLBSDEP11_Action.java     1.0 2011.2.10
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

import com.cherry.bs.dep.bl.BINOLBSDEP11_BL;
import com.cherry.bs.dep.form.BINOLBSDEP11_Form;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.opensymphony.xwork2.ModelDriven;


/**
 * 品牌添加画面Action
 * 
 * @author WangCT
 * @version 1.0 2011.2.10
 */
public class BINOLBSDEP11_Action extends BaseAction implements ModelDriven<BINOLBSDEP11_Form> {

	private static final long serialVersionUID = -5509974142188137256L;
	
	/** 品牌添加画面Form */
	private BINOLBSDEP11_Form form = new BINOLBSDEP11_Form();
	
	/** 品牌添加画面BL */
	@Resource
	private BINOLBSDEP11_BL binOLBSDEP11_BL;
	
	/**
	 * 
	 * 品牌添加画面初期处理
	 * 
	 * @return 品牌添加画面
	 */
	public String init() {
		
		return SUCCESS;
	}
	
	/**
	 * 
	 * 添加品牌处理
	 * 
	 * @return 添加完了画面
	 */
	public String addBrandInfo() throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 品牌代码
		if(form.getBrandCode() != null && !"".equals(form.getBrandCode())) {
			map.put("brandCode", form.getBrandCode());
		}
		// 品牌名称中文
		if(form.getBrandNameChinese() != null && !"".equals(form.getBrandNameChinese())) {
			map.put("brandNameChinese", form.getBrandNameChinese());
		}
		// 品牌名称中文简称
		if(form.getBrandNameShort() != null && !"".equals(form.getBrandNameShort())) {
			map.put("brandNameShort", form.getBrandNameShort());
		}
		// 品牌名称外文
		if(form.getBrandNameForeign() != null && !"".equals(form.getBrandNameForeign())) {
			map.put("brandNameForeign", form.getBrandNameForeign());
		}
		// 品牌名称外文简称
		if(form.getBrandNameForeignShort() != null && !"".equals(form.getBrandNameForeignShort())) {
			map.put("brandNameForeignShort", form.getBrandNameForeignShort());
		}
		// 成立日期
		if(form.getFoundationDate() != null && !"".equals(form.getFoundationDate())) {
			map.put("foundationDate", form.getFoundationDate());
		}
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 组织ID
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 组织code
		map.put("orgCode", userInfo.getOrganizationInfoCode());
		// 组织名称
		map.put("orgNameChinese", userInfo.getOrgName());
		// 作成者
		map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
		// 作成程序名
		map.put(CherryConstants.CREATEPGM, "BINOLBSDEP11");
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
		// 更新程序名
		map.put(CherryConstants.UPDATEPGM, "BINOLBSDEP11");
		// 添加品牌处理
		binOLBSDEP11_BL.tran_addBrandInfo(map);
		this.addActionMessage(getText("ICM00001"));
		return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
	}
	
	/**
	 * 
	 * 添加品牌前字段验证处理
	 * 
	 */
	public void validateAddBrandInfo() throws Exception {
		
		// 品牌代码必须入力验证
		if(CherryChecker.isNullOrEmpty(form.getBrandCode())) {
			this.addFieldError("brandCode", getText("ECM00009",new String[]{getText("PBS00033")}));
		} else {
			// 品牌代码不是英数字的场合
			if(!CherryChecker.isAlphanumeric(form.getBrandCode())) {
				this.addFieldError("brandCode", getText("ECM00031",new String[]{getText("PBS00033")}));
			}
			// 品牌代码不能超过10位验证
			if(form.getBrandCode().length() > 10) {
				this.addFieldError("brandCode", getText("ECM00020",new String[]{getText("PBS00033"),"10"}));
			}
		}
		// 品牌名称中文必须入力验证
		if(CherryChecker.isNullOrEmpty(form.getBrandNameChinese())) {
			this.addFieldError("brandNameChinese", getText("ECM00009",new String[]{getText("PBS00034")}));
		} else {
			// 品牌名称中文不能超过50位验证
			if(form.getBrandNameChinese().length() > 50) {
				this.addFieldError("brandNameChinese", getText("ECM00020",new String[]{getText("PBS00034"),"50"}));
			}
		}
		if(form.getBrandNameShort() != null && !"".equals(form.getBrandNameShort())) {
			// 品牌名称中文简称不能超过20位验证
			if(form.getBrandNameShort().length() > 20) {
				this.addFieldError("brandNameShort", getText("ECM00020",new String[]{getText("PBS00035"),"20"}));
			}
		}
		if(form.getBrandNameForeign() != null && !"".equals(form.getBrandNameForeign())) {
			// 品牌名称外文不能超过50位验证
			if(form.getBrandNameForeign().length() > 50) {
				this.addFieldError("brandNameForeign", getText("ECM00020",new String[]{getText("PBS00036"),"50"}));
			}
		}
		if(form.getBrandNameForeignShort() != null && !"".equals(form.getBrandNameForeignShort())) {
			// 品牌名称外文简称不能超过20位验证
			if(form.getBrandNameForeignShort().length() > 20) {
				this.addFieldError("brandNameForeignShort", getText("ECM00020",new String[]{getText("PBS00037"),"20"}));
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
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 组织ID
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 品牌代码
			map.put("brandCode", form.getBrandCode());
			// 判断品牌代码是否已经存在
			String brandId = binOLBSDEP11_BL.checkBrandCode(map);
			if(brandId != null && !"".equals(brandId)) {
				this.addFieldError("brandCode", getText("EBS00014"));
			}
			// 品牌名称中文
			map.put("brandNameChinese", form.getBrandNameChinese());
			// 判断品牌名称是否已经存在
			String brandId2 = binOLBSDEP11_BL.checkBrandName(map);
			if(brandId2 != null && !"".equals(brandId2)) {
				this.addFieldError("brandNameChinese", getText("EBS00015"));
			}
		}
		
	}

	@Override
	public BINOLBSDEP11_Form getModel() {
		return form;
	}

}
