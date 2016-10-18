/*
 * @(#)BINOLBSDEP07_Action.java     1.0 2011.2.10
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
import com.cherry.bs.dep.form.BINOLBSDEP07_Form;
import com.cherry.bs.emp.bl.BINOLBSEMP04_BL;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.DESPlus;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 组织添加画面Action
 * 
 * @author WangCT
 * @version 1.0 2011.2.10
 */
public class BINOLBSDEP07_Action extends BaseAction implements ModelDriven<BINOLBSDEP07_Form> {
	
	private static final long serialVersionUID = 6592214265290754185L;
	
	/** 组织添加画面Form */
	private BINOLBSDEP07_Form form = new BINOLBSDEP07_Form();
	
	/** 组织添加画面BL */
	@Resource
	private BINOLBSDEP07_BL binOLBSDEP07_BL;
	
	/** 添加用户画面BL */
	@Resource
	private BINOLBSEMP04_BL binolbsemp04BL;
	
	/**
	 * 
	 * 组织添加画面初期处理
	 * 
	 * @return 组织添加画面
	 */
	public String init() {
		
		return SUCCESS;
	}
	
	/**
	 * 
	 * 添加组织处理
	 * 
	 * @return 添加成功画面
	 */
	public String addOrganization() throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 组织代码
		if(form.getOrgCode() != null && !"".equals(form.getOrgCode())) {
			map.put("orgCode", form.getOrgCode());
		}
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
		// 登录帐号
		if(form.getLonginName() != null && !"".equals(form.getLonginName())) {
			map.put("longinName", form.getLonginName());
		}
		// 密码
		if(form.getPassWord() != null && !"".equals(form.getPassWord())) {
			// 加密处理
			DESPlus des = new DESPlus(CherryConstants.CUSTOMKEY);
			map.put("passWord", des.encrypt(form.getPassWord()));
		}
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 作成者
		map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
		// 作成程序名
		map.put(CherryConstants.CREATEPGM, "BINOLBSDEP07");
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
		// 更新程序名
		map.put(CherryConstants.UPDATEPGM, "BINOLBSDEP07");
		// 添加组织处理
		binOLBSDEP07_BL.tran_addOrganization(map);
		this.addActionMessage(getText("ICM00001"));
		return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
	}
	
	/**
	 * 
	 * 添加组织前字段验证处理
	 * 
	 */
	public void validateAddOrganization() throws Exception {
		
		// 组织代码必须入力验证
		if(CherryChecker.isNullOrEmpty(form.getOrgCode())) {
			this.addFieldError("orgCode", getText("ECM00009",new String[]{getText("PBS00057")}));
		} else {
			// 组织代码不能超过50位验证
			if(form.getOrgCode().length() > 50) {
				this.addFieldError("orgCode", getText("ECM00020",new String[]{getText("PBS00057"),"50"}));
			}
		}
		// 组织名称中文必须入力验证
		if(CherryChecker.isNullOrEmpty(form.getOrgNameChinese())) {
			this.addFieldError("orgNameChinese", getText("ECM00009",new String[]{getText("PBS00029")}));
		} else {
			// 组织名称中文不能超过50位验证
			if(form.getOrgNameChinese().length() > 50) {
				this.addFieldError("orgNameChinese", getText("ECM00020",new String[]{getText("PBS00029"),"50"}));
			}
		}
		// 登录帐号必须入力验证
		if(CherryChecker.isNullOrEmpty(form.getLonginName())) {
			this.addFieldError("longinName", getText("ECM00009",new String[]{getText("PBS00046")}));
		} else {
			// 登录帐号不能超过30位验证
			if(form.getLonginName().length() > 30) {
				this.addFieldError("longinName", getText("ECM00020",new String[]{getText("PBS00046"),"30"}));
			} else {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("longinName", form.getLonginName());
				// 验证登录帐号是否唯一
				String userId = binolbsemp04BL.getUserIdByLgName(map);
				if (!CherryChecker.isNullOrEmpty(userId)) {
					this.addFieldError("longinName", getText("ECM00032",
							new String[] { getText("PBS00046") }));
				}
			}
		}
		// 密码必须入力验证
		if(CherryChecker.isNullOrEmpty(form.getPassWord())) {
			this.addFieldError("passWord", getText("ECM00009",new String[]{getText("PBS00047")}));
		} else {
			// 密码不能超过30位验证
			if(form.getPassWord().length() > 30) {
				this.addFieldError("passWord", getText("ECM00020",new String[]{getText("PBS00047"),"30"}));
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
			// 组织代码
			map.put("orgCode", form.getOrgCode());
			// 判断组织编码是否已经存在
			String orgId1 = binOLBSDEP07_BL.getOrgIdByOrgCode(map);
			if(orgId1 != null && !"".equals(orgId1)) {
				this.addFieldError("orgCode", getText("EBS00021"));
			}
			// 判断组织名称是否已经存在
			String orgId2 = binOLBSDEP07_BL.getOrganizationInfoID(map);
			if(orgId2 != null && !"".equals(orgId2)) {
				this.addFieldError("orgNameChinese", getText("EBS00012"));
			}
		}
		
	}

	@Override
	public BINOLBSDEP07_Form getModel() {
		return form;
	}

}
