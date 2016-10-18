/*
 * @(#)BINOLPLPLT02_Action.java     1.0 2010/10/27
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

package com.cherry.pl.plt.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.Bean2Map;
import com.cherry.pl.plt.bl.BINOLPLPLT01_BL;
import com.cherry.pl.plt.bl.BINOLPLPLT02_BL;
import com.cherry.pl.plt.form.BINOLPLPLT02_Form;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 添加权限类型Action
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
@SuppressWarnings("unchecked")
public class BINOLPLPLT02_Action extends BaseAction implements ModelDriven<BINOLPLPLT02_Form> {
	
	private static final long serialVersionUID = -3821077146991586982L;
	
	/** 权限类型一览BL */
	@Resource
	private BINOLPLPLT01_BL binOLPLPLT01_BL;
	
	/** 添加权限类型BL */
	@Resource
	private BINOLPLPLT02_BL binOLPLPLT02_BL;
	
	/** 添加权限类型Form */
	private BINOLPLPLT02_Form form = new BINOLPLPLT02_Form();
	
	/**
	 * 添加权限类型画面初期表示处理
	 * 
	 * @return 添加权限类型画面
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
		// 总部的场合
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		}
		// 取得岗位类别信息List
		posCategoryList = binOLPLPLT01_BL.getPositionCategoryList(map);
		return SUCCESS;
	}
	
	/**
	 * 添加权限类型处理
	 * 
	 * @return 添加完了画面
	 */
	public String addPlt() throws Exception {
		
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		if(form.getExclusive() == null || "".equals(form.getExclusive())) {
			map.put("exclusive", "0");
		}
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 作成者
		map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
		// 作成程序名
		map.put(CherryConstants.CREATEPGM, "BINOLPLPLT02");
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
		// 更新程序名
		map.put(CherryConstants.UPDATEPGM, "BINOLPLPLT02");
		// 添加权限类型处理
		binOLPLPLT02_BL.tran_addPlt(map);
		this.addActionMessage(getText("ICM00001"));
		return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
	}
	
	/**
	 * 添加权限类型处理前参数验证处理
	 * 
	 */
	public void validateAddPlt() throws Exception {
		// 数据过滤类型为按部门类型的场合
		if("0".equals(form.getCategory())) {
			// 部门类型必须入力验证
			if(form.getDepartType() == null || form.getDepartType().length == 0) {
				this.addFieldError("departType", getText("ECM00009",new String[]{getText("PPL00019")}));
			}
			// 权限区分必须入力验证
			if(CherryChecker.isNullOrEmpty(form.getPrivilegeType())) {
				this.addFieldError("privilegeType", getText("ECM00009",new String[]{getText("PPL00022")}));
			}
		} 
		// 数据过滤类型为按岗位类别的场合
		else if("1".equals(form.getCategory())) {
			// 岗位类别必须入力验证
			if(form.getPositionCategoryId() == null || form.getPositionCategoryId().length == 0) {
				this.addFieldError("positionCategoryId", getText("ECM00009",new String[]{getText("PPL00023")}));
			}
			// 权限区分必须入力验证
			if(CherryChecker.isNullOrEmpty(form.getPrivilegeType())) {
				this.addFieldError("privilegeType_1", getText("ECM00009",new String[]{getText("PPL00022")}));
			}
		}
		// 业务类型必须入力验证
		if(form.getBusinessType() == null || form.getBusinessType().length == 0) {
			this.addActionError(getText("ECM00009",new String[]{getText("PPL00020")}));
		}
		// 操作类型必须入力验证
		if(form.getOperationType() == null || form.getOperationType().length == 0) {
			this.addActionError(getText("ECM00009",new String[]{getText("PPL00021")}));
		}
		if(!this.hasFieldErrors() && !this.hasActionErrors()) {
			Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			int count = binOLPLPLT02_BL.getPltCount(map);
			if(count > 0) {
				this.addActionError(getText("EPL00020"));
			}
		}
	}

	/** 岗位类别List */
	private List<Map<String, Object>> posCategoryList;

	public List<Map<String, Object>> getPosCategoryList() {
		return posCategoryList;
	}

	public void setPosCategoryList(List<Map<String, Object>> posCategoryList) {
		this.posCategoryList = posCategoryList;
	}

	@Override
	public BINOLPLPLT02_Form getModel() {
		return form;
	}

}
