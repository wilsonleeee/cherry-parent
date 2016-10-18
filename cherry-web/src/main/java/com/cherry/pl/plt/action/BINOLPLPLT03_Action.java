/*
 * @(#)BINOLPLPLT03_Action.java     1.0 2010/10/27
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
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.Bean2Map;
import com.cherry.pl.plt.bl.BINOLPLPLT01_BL;
import com.cherry.pl.plt.bl.BINOLPLPLT03_BL;
import com.cherry.pl.plt.form.BINOLPLPLT03_Form;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 更新权限类型Action
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
@SuppressWarnings("unchecked")
public class BINOLPLPLT03_Action extends BaseAction implements ModelDriven<BINOLPLPLT03_Form> {
	
	private static final long serialVersionUID = 1341198648795807915L;
	
	/** 权限类型一览BL */
	@Resource
	private BINOLPLPLT01_BL binOLPLPLT01_BL;
	
	/** 更新权限类型BL */
	@Resource
	private BINOLPLPLT03_BL binOLPLPLT03_BL;
	
	/** 更新权限类型Form */
	private BINOLPLPLT03_Form form = new BINOLPLPLT03_Form();
	
	/**
	 * 更新权限类型画面初期表示处理
	 * 
	 * @return 更新权限类型画面
	 */
	public String init() {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 权限类型记录ID
		map.put("privilegeTypeId", form.getPrivilegeTypeId());
		// 查询权限类型信息
		privilegeTypeInfo = binOLPLPLT03_BL.getPrivilegeTypeInfo(map);
		// 权限类型不存在的场合
		if(privilegeTypeInfo == null) {
			this.addActionError(getText("EPL00005"));    
            return SUCCESS;
		}
		// 数据过滤类型为按岗位类别的场合
		if("1".equals(privilegeTypeInfo.get("category"))) {
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
		}
		return SUCCESS;
	}
	
	/**
	 * 更新权限类型处理
	 * 
	 * @return 更新完了画面
	 */
	public String updatePlt() throws Exception {
		
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		if(form.getExclusive() == null || "".equals(form.getExclusive())) {
			map.put("exclusive", "0");
		}
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
		// 更新程序名
		map.put(CherryConstants.UPDATEPGM, "BINOLPLPLT03");
		
		try {
			// 更新权限类型处理
			binOLPLPLT03_BL.tran_updatePlt(map);
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
	 * 更新权限类型处理前参数验证处理
	 * 
	 */
	public void validateUpdatePlt() throws Exception {
		// 数据过滤类型为按部门类型的场合
		if("0".equals(form.getCategory())) {
			// 部门类型必须入力验证
			if(CherryChecker.isNullOrEmpty(form.getDepartType())) {
				this.addFieldError("departType", getText("ECM00009",new String[]{getText("PPL00019")}));
			}
		} 
		// 数据过滤类型为按岗位类别的场合
		else if("1".equals(form.getCategory())) {
			// 岗位类别必须入力验证
			if(CherryChecker.isNullOrEmpty(form.getPositionCategoryId())) {
				this.addFieldError("positionCategoryId", getText("ECM00009",new String[]{getText("PPL00023")}));
			}
		}
		// 业务类型必须入力验证
		if(CherryChecker.isNullOrEmpty(form.getBusinessType())) {
			this.addFieldError("businessType", getText("ECM00009",new String[]{getText("PPL00020")}));
		}
		// 操作类型必须入力验证
		if(CherryChecker.isNullOrEmpty(form.getOperationType())) {
			this.addFieldError("operationType", getText("ECM00009",new String[]{getText("PPL00021")}));
		}
		// 权限区分必须入力验证
		if(CherryChecker.isNullOrEmpty(form.getPrivilegeType())) {
			this.addFieldError("privilegeType", getText("ECM00009",new String[]{getText("PPL00022")}));
		}
		if(!this.hasFieldErrors()) {
			Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			String pltCd = binOLPLPLT03_BL.getPltCountVal(map);
			if(pltCd != null && !pltCd.equals(form.getPrivilegeTypeId())) {
				this.addActionError(getText("EPL00004"));
			}
		}
	}
	
	/** 权限类型信息 */
	private Map privilegeTypeInfo;
	
	/** 岗位类别List */
	private List<Map<String, Object>> posCategoryList;

	public Map getPrivilegeTypeInfo() {
		return privilegeTypeInfo;
	}

	public void setPrivilegeTypeInfo(Map privilegeTypeInfo) {
		this.privilegeTypeInfo = privilegeTypeInfo;
	}
	
	public List<Map<String, Object>> getPosCategoryList() {
		return posCategoryList;
	}

	public void setPosCategoryList(List<Map<String, Object>> posCategoryList) {
		this.posCategoryList = posCategoryList;
	}

	@Override
	public BINOLPLPLT03_Form getModel() {
		return form;
	}

}
