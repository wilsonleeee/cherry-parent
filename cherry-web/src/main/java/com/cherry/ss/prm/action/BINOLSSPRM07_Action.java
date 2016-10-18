/*	
 * @(#)BINOLSSPRM07_Action.java     1.0 2010/11/29		
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.Bean2Map;
import com.cherry.ss.prm.bl.BINOLSSPRM07_BL;
import com.cherry.ss.prm.bl.BINOLSSPRM08_BL;
import com.cherry.ss.prm.form.BINOLSSPRM07_Form;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 促销品分类编辑Action
 * 
 * 
 */
@SuppressWarnings("unchecked")
public class BINOLSSPRM07_Action extends BaseAction implements
		ModelDriven<BINOLSSPRM07_Form> {

	private static final long serialVersionUID = 1514635732607867514L;
	/** 参数FORM */
	private BINOLSSPRM07_Form form = new BINOLSSPRM07_Form();
	@Resource
	private BINOLCM05_BL binOLCM05_BL;
	@Resource
	private BINOLSSPRM07_BL binolssprm07_BL;
	@Resource
	private BINOLSSPRM08_BL binolssprm08_BL;

	/** 促销品分类 */
	private Map prmType;
	/** 所属品牌List */
	private List brandInfoList;

	@Override
	public BINOLSSPRM07_Form getModel() {
		return form;
	}

	/** 促销品分类 */
	public void setPrmType(Map prmType) {
		this.prmType = prmType;
	}

	public Map getPrmType() {
		return prmType;
	}

	/** 所属品牌List */
	public void setBrandInfoList(List brandInfoList) {
		this.brandInfoList = brandInfoList;
	}

	public List getBrandInfoList() {
		return brandInfoList;
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
	public String init() throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 业务类型
		map.put(CherryConstants.BUSINESS_TYPE, CherryConstants.BUSINESS_TYPE1);
		// 操作类型
		map.put(CherryConstants.OPERATION_TYPE,
						CherryConstants.OPERATION_TYPE1);
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 总部用户登录的时候
		if (CherryConstants.BRAND_INFO_ID_VALUE == userInfo
				.getBIN_BrandInfoID()) {
			// 取得所管辖的品牌List
			brandInfoList = binOLCM05_BL.getBrandInfoList(map);
		} else {
			// 品牌信息
			Map brandInfo = new HashMap();
			// 品牌ID
			brandInfo.put("brandInfoId", userInfo.getBIN_BrandInfoID());
			// 品牌名称
			brandInfo.put("brandName", userInfo.getBrandName());
			brandInfoList = new ArrayList();
			brandInfoList.add(brandInfo);
		}
		// 促销产品分类ID
		map.put("prmTypeId", form.getPrmTypeId());
		// 促销品分类信息
		prmType = binolssprm08_BL.searchPrmTypeInfo(map);

		return SUCCESS;
	}

	/**
	 * <p>
	 * 保存处理
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * 
	 */
	public String save() throws Exception {
		if (!validateForm()) {
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		Map<String, Object> map = (Map<String, Object>) Bean2Map
				.toHashMap(form);
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		try {
			binolssprm07_BL.tran_updatePrmType(map);
		} catch (CherryException e) {
			this.addActionError(e.getErrMessage());
			return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
		}
		this.addActionMessage(getText("ICM00001"));
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
		
		// 大分类中文名不能超过20位验证
		if (form.getPrimaryCategoryNameCN() != null
				&& form.getPrimaryCategoryNameCN().length() > 20) {
			this.addFieldError("primaryCategoryNameCN", getText("ECM00020",
					new String[] { getText("PSS00031"), "20" }));
			isCorrect = false;
		}
		// 大分类英文名不能超过20位验证
		if (form.getPrimaryCategoryNameEN() != null
				&& form.getPrimaryCategoryNameEN().length() > 20) {
			this.addFieldError("primaryCategoryNameEN", getText("ECM00020",
					new String[] { getText("PSS00032"), "20" }));
			isCorrect = false;
		}
		// 中分类中文名不能超过20位验证
		if (form.getSecondryCategoryNameCN() != null
				&& form.getSecondryCategoryNameCN().length() > 20) {
			this.addFieldError("secondryCategoryNameCN", getText("ECM00020",
					new String[] { getText("PSS00034"), "20" }));
			isCorrect = false;
		}
		// 中分类英文名不能超过20位验证
		if (form.getSecondryCategoryNameEN() != null
				&& form.getSecondryCategoryNameEN().length() > 20) {
			this.addFieldError("secondryCategoryNameEN", getText("ECM00020",
					new String[] { getText("PSS00035"), "20" }));
			isCorrect = false;
		}
		// 小分类中文名不能超过20位验证
		if (form.getSmallCategoryNameCN() != null
				&& form.getSmallCategoryNameCN().length() > 20) {
			this.addFieldError("smallCategoryNameCN", getText("ECM00020",
					new String[] { getText("PSS00037"), "20" }));
			isCorrect = false;
		}
		// 小分类英文名不能超过20位验证
		if (form.getSmallCategoryNameEN() != null
				&& form.getSmallCategoryNameEN().length() > 20) {
			this.addFieldError("smallCategoryNameEN", getText("ECM00020",
					new String[] { getText("PSS00038"), "20" }));
			isCorrect = false;
		}
		return isCorrect;
	}

}
