/*
 * @(#)BINOLSSPRM10_Action.java     1.0 2010/02/24
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
package com.cherry.ss.prm.action;

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
import com.cherry.ss.prm.bl.BINOLSSPRM10_BL;
import com.cherry.ss.prm.form.BINOLSSPRM10_Form;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 促销品类别添加Action
 * 
 */
@SuppressWarnings("unchecked")
public class BINOLSSPRM10_Action extends BaseAction implements
		ModelDriven<BINOLSSPRM10_Form> {

	private static final long serialVersionUID = -6676412305227501560L;

	/** 参数FORM */
	private BINOLSSPRM10_Form form = new BINOLSSPRM10_Form();

	@Resource
	private BINOLSSPRM10_BL binolssprm10_BL;

	@Resource
	private BINOLCM05_BL binOLCM05_BL;

	/** 所属品牌List */
	private List brandInfoList;
	
	/** 上级促销品类别List */
	private List higherCategoryList;
	
	public BINOLSSPRM10_Form getModel() {
		return form;
	}

	/** 所属品牌List */
	public void setBrandInfoList(List brandInfoList) {
		this.brandInfoList = brandInfoList;
	}

	public List getBrandInfoList() {
		return brandInfoList;
	}

	/** 上级促销品类别List */
	public void setHigherCategoryList(List higherCategoryList) {
		this.higherCategoryList = higherCategoryList;
	}

	public List getHigherCategoryList() {
		return higherCategoryList;
	}

	/**
	 * <p>
	 * 画面初期显示
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * 
	 */
	public String init() {
		Map<String, Object> map = new HashMap<String, Object>();
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 总部用户
		if (CherryConstants.BRAND_INFO_ID_VALUE == userInfo.getBIN_BrandInfoID()) {
			// 取得所属品牌List
			brandInfoList =  binOLCM05_BL.getBrandInfoList(map);
			if(brandInfoList != null && !brandInfoList.isEmpty()) {
				map.put(CherryConstants.BRANDINFOID, ((Map)brandInfoList.get(0)).get("brandInfoId"));
			}
		}else{
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		}
		// 上级促销品类别List
		higherCategoryList = binolssprm10_BL.getHigherCategoryList(map);
		return SUCCESS;
	}

	/**
	 * 取得品牌ID下的上级类别List
	 * 
	 */
	public String filterByBrandInfo() throws Exception {
		
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
		// 不是总部的场合
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		} else {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		//  取得上级类别信息List
		resultMap.put("higherCategoryList", binolssprm10_BL.getHigherCategoryList(map));
		
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(JSONUtil.serialize(resultMap));
		return null;
	}
	/**
	 * <p>
	 * 保存
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * @throws Exception
	 * 
	 */
	public String save() throws Exception {

		// 验证提交的参数
		if (!validateForm()) {
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		
		Map<String, Object> map = (Map<String, Object>)Bean2Map.toHashMap(form);
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 不是总部的场合
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		} else {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		}
		// 作成者
		map.put(CherryConstants.CREATEDBY, userInfo.getLoginName());
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());
		// 作成程序名
		map.put(CherryConstants.CREATEPGM, "BINOLSSPRM06");
		// 作成程序名
		map.put(CherryConstants.UPDATEPGM, "BINOLSSPRM06");
		// 促销品添加插表处理
		binolssprm10_BL.trans_addPrmCategory(map);
		// 处理成功
		this.addActionMessage(getText("ICM00002"));
		return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
	}

	/**
	 * 验证提交的参数
	 * 
	 * @param 无
	 * @return boolean 验证结果
	 * @throws Exception
	 * 
	 */
	public boolean validateForm() throws Exception {
		// 验证结果
		boolean isCorrect = true;
		// 类别码不为空验证
		if (CherryChecker.isNullOrEmpty(form.getItemClassCode())) {
			this.addFieldError("itemClassCode", getText("ECM00009",
					new String[] { "类别码" }));
			isCorrect = false;
		} else {
			// 类别码不能超过4位验证
			if (form.getItemClassCode().length() > 20) {
				this.addFieldError("itemClassCode", getText("ECM00020",
						new String[] { "类别码", "4" }));
				isCorrect = false;
			}
		}
		// 类别中文名不能超过50位验证
		if(form.getItemClassNameCN() != null && form.getItemClassNameCN().length() > 50) {
			this.addFieldError("itemClassNameCN", getText("ECM00020",
					new String[]{"类别中文名","50"}));
			isCorrect = false;
		}
		// 类别英文名不能超过50位验证
		if(form.getItemClassNameEN() != null && form.getItemClassNameEN().length() > 50) {
			this.addFieldError("itemClassNameEN", getText("ECM00020",
					new String[]{"类别英文名","50"}));
			isCorrect = false;
		}
		
		// 类别特征码不能超过4位验证
		if(form.getCurClassCode() != null && form.getCurClassCode().length() > 4) {
			this.addFieldError("curClassCode", getText("ECM00020",
					new String[]{"类别特征码","4"}));
			isCorrect = false;
		}
		return isCorrect;
	}
}
