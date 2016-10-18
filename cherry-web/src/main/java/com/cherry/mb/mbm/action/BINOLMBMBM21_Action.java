/*
 * @(#)BINOLMBMBM21_Action.java     1.0 2013.08.01
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
package com.cherry.mb.mbm.action;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherrySecret;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mb.mbm.bl.BINOLMBMBM21_BL;
import com.cherry.mb.mbm.form.BINOLMBMBM21_Form;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 推荐会员画面Action
 * 
 * @author WangCT
 * @version 1.0 2013.08.01
 */
public class BINOLMBMBM21_Action extends BaseAction implements ModelDriven<BINOLMBMBM21_Form> {
	
	private static final long serialVersionUID = -4110411187431627226L;
	
	/** 推荐会员画面BL */
	@Resource
	private BINOLMBMBM21_BL binOLMBMBM21_BL;
	
	/**
	 * 推荐会员画面
	 * 
	 * @return 推荐会员画面
	 */
	public String init() throws Exception {
		
		if(form.getReferrerId() != null && !"".equals(form.getReferrerId())) {
			Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
			// 所属品牌Code
			map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
			// 用户ID
			// 查询推荐者基本信息
			referrerMap = binOLMBMBM21_BL.getReferrerInfo(map);
		}
		return SUCCESS;
	}
	
	/**
	 * AJAX取得推荐过的会员
	 * 
	 * @return 推荐过的会员
	 */
	public String search() throws Exception {
		
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// 用户ID
		map.put("userId", userInfo.getBIN_UserID());
		// 业务类型
		map.put("businessType", "2");
		// 操作类型
		map.put("operationType", "1");
		// 是否带权限查询
		map.put("privilegeFlag", session.get(CherryConstants.SESSION_PRIVILEGE_FLAG));
		// 所属品牌Code
		map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		// 品牌Code
		String brandCode = ConvertUtil.getString(map.get("brandCode"));
		// 会员【电话】字段加密
		if (!CherryChecker.isNullOrEmpty(map.get("mobilePhone"), true)) {
			String mobilePhone = ConvertUtil.getString(map.get("mobilePhone"));
			map.put("mobilePhone", CherrySecret.encryptData(brandCode,mobilePhone));
		}
		// 查询推荐过的会员总数
		int count = binOLMBMBM21_BL.getReferCount(map);
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if(count != 0) {
			// 查询推荐过的会员List
			referList = binOLMBMBM21_BL.getReferList(map);
		}
		return SUCCESS;
	}
	
	/** 推荐者基本信息 */
	private Map referrerMap;
	
	/** 推荐过的会员List */
	private List<Map<String, Object>> referList;
	
	public Map getReferrerMap() {
		return referrerMap;
	}

	public void setReferrerMap(Map referrerMap) {
		this.referrerMap = referrerMap;
	}

	public List<Map<String, Object>> getReferList() {
		return referList;
	}

	public void setReferList(List<Map<String, Object>> referList) {
		this.referList = referList;
	}

	/** 会员推荐会员画面Form */
	private BINOLMBMBM21_Form form = new BINOLMBMBM21_Form();

	@Override
	public BINOLMBMBM21_Form getModel() {
		return form;
	}

}
