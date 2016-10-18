/*
 * @(#)BINOLBSPOS09_Action.java     1.0 2010/10/27
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

package com.cherry.bs.pos.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.pos.bl.BINOLBSPOS09_BL;
import com.cherry.bs.pos.form.BINOLBSPOS09_Form;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.Bean2Map;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 添加岗位类别画面Action
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
@SuppressWarnings("unchecked")
public class BINOLBSPOS09_Action extends BaseAction implements ModelDriven<BINOLBSPOS09_Form> {

	private static final long serialVersionUID = -437776761053553910L;
	
	/** 添加岗位类别画面BL */
	@Resource
	private BINOLBSPOS09_BL binOLBSPOS09_BL;
	
	/** 共通BL */
	@Resource
	private BINOLCM05_BL binOLCM05_BL;
	
	/** 添加岗位类别画面Form */
	private BINOLBSPOS09_Form form = new BINOLBSPOS09_Form();
	
	/**
	 * 
	 * 添加岗位画面初期处理
	 * 
	 * @return 添加岗位画面
	 */
	public String init() {
		
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 总部的场合
		if(userInfo.getBIN_BrandInfoID() == CherryConstants.BRAND_INFO_ID_VALUE) {
			Map<String, Object> map = new HashMap<String, Object>();
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 语言
			String language = (String)session.get(CherryConstants.SESSION_LANGUAGE);
			if(language != null) {
				map.put(CherryConstants.SESSION_LANGUAGE, language);
			}
			brandInfoList = binOLCM05_BL.getBrandInfoList(map);
		}
		
		return SUCCESS;
	}
	
	/**
	 * 
	 * 添加岗位类别
	 * 
	 * @return 添加完了画面
	 */
	public String addPosCategory() throws Exception {
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌存在的场合
		if(form.getBrandInfoId() != null && !"".equals(form.getBrandInfoId())) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		} else {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		}
		// 作成者
		map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
		// 作成程序名
		map.put(CherryConstants.CREATEPGM, "BINOLBSPOS09");
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
		// 更新程序名
		map.put(CherryConstants.UPDATEPGM, "BINOLBSPOS09");
		// 添加岗位类别
		binOLBSPOS09_BL.tran_addPosCategory(map);
		this.addActionMessage(getText("ICM00001"));
		return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
	}
	
	/**
	 * 
	 * 添加岗位类型前字段验证处理
	 * 
	 */
	public void validateAddPosCategory() throws Exception {
		
		// 类别代码必须入力验证
		if(CherryChecker.isNullOrEmpty(form.getCategoryCode())) {
			this.addFieldError("categoryCode", getText("ECM00009",new String[]{"类别代码"}));
		} else {
			// 类别代码不能超过2位验证(一个汉字代表两位)
			if(form.getCategoryCode().getBytes().length > 2) {
				this.addFieldError("categoryCode", getText("ECM00020",new String[]{"类别代码","2"}));
			}
		}
		// 类别名称必须入力验证
		if(CherryChecker.isNullOrEmpty(form.getCategoryName())) {
			this.addFieldError("categoryName", getText("ECM00009",new String[]{"类别名称"}));
		} else {
			// 类别名称不能超过20位验证
			if(form.getCategoryName().length() > 20) {
				this.addFieldError("categoryName", getText("ECM00020",new String[]{"类别名称","20"}));
			}
		}
		// 岗位级别必须入力验证
		if(CherryChecker.isNullOrEmpty(form.getPosGrade())) {
			this.addFieldError("posGrade", getText("ECM00009",new String[]{"岗位级别"}));
		} else {
			// 岗位级别必须是数字验证
			if(!CherryChecker.isNumeric(form.getPosGrade())) {
				this.addFieldError("posGrade", getText("ECM00021",new String[]{"岗位级别"}));
			}
		}
		// 类别外文名称不能超过20位验证
		if(form.getCategoryNameForeign() != null && form.getCategoryNameForeign().length() > 20) {
			this.addFieldError("categoryNameForeign", getText("ECM00020",new String[]{"类别外文名称","20"}));
		}
		// 验证同一组织中是否存在同样的岗位类别
		if(!this.hasFieldErrors()) {
			Map<String, Object> map = new HashMap<String, Object>();
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 所属品牌存在的场合
			if(form.getBrandInfoId() != null && !"".equals(form.getBrandInfoId())) {
				// 所属品牌
				map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
			} else {
				// 所属品牌
				map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			}
			// 类别代码
			map.put("categoryCode", form.getCategoryCode());
			// 类别名称
			map.put("categoryName", form.getCategoryName());
			String posCategoryId = binOLBSPOS09_BL.getPosCategoryCheck(map);
			String posCategoryNameId = binOLBSPOS09_BL.getPosCategoryNameCheck(map);
			if(posCategoryId != null && !"".equals(posCategoryId)) {
				//岗位代码已存在，无法执行该操作！
				this.addActionError(getText("EBS00006"));
			}else if(posCategoryNameId != null && !"".equals(posCategoryNameId)){
				// 岗位名称已存在，无法执行该操作！
				this.addActionError(getText("EBS00094"));
			}
		}
	}
	
	/** 品牌List */
	private List<Map<String, Object>> brandInfoList;

	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}

	@Override
	public BINOLBSPOS09_Form getModel() {
		return form;
	}
}
