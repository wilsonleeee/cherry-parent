/*
 * @(#)BINOLMBMBM01_Action.java     1.0 2011/03/22
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mb.mbm.bl.BINOLMBMBM01_BL;
import com.cherry.mb.mbm.form.BINOLMBMBM01_Form;
import com.cherry.mb.mbm.service.BINOLMBMBM09_Service;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 会员一览画面Action
 * 
 * @author WangCT
 * @version 1.0 2011.03.22
 */
public class BINOLMBMBM01_Action extends BaseAction implements ModelDriven<BINOLMBMBM01_Form> {

	private static final long serialVersionUID = 130221305111380662L;
	
	/** 会员一览画面BL */
	@Resource
	private BINOLMBMBM01_BL binOLMBMBM01_BL;
	
	/** 会员搜索画面Service */
	@Resource
	private BINOLMBMBM09_Service binOLMBMBM09_Service;
	
	/**
	 * 会员一览画面初期处理
	 * 
	 * @return 会员一览画面
	 */
	public String init() throws Exception {
		
		monthList = new ArrayList<Integer>();
		for(int i = 1; i <= 12; i++) {
			monthList.add(i);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 不是总部的场合
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		}
		// 设置品牌ID
		form.setBrandInfoId(String.valueOf(userInfo.getBIN_BrandInfoID()));
		// 取得会员扩展信息List
		extendPropertyList = binOLMBMBM01_BL.getExtendProperty(map);
		// 查询会员等级信息List
		memLevelList = binOLMBMBM09_Service.getMemberLevelInfoList(map);
		
		return SUCCESS;
	}
	
	/**
	 * AJAX取得会员一览信息
	 * 
	 * @return 会员一览画面
	 */
	public String search() throws Exception {
		
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 不是总部的场合
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		}
		binOLMBMBM01_BL.setCondition(map);
		
		// 用户ID
		map.put("userId", userInfo.getBIN_UserID());
		// 业务类型
		map.put("businessType", "2");
		// 操作类型
		map.put("operationType", "1");
		// 是否带权限查询
		map.put("privilegeFlag", session.get(CherryConstants.SESSION_PRIVILEGE_FLAG));
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		int count = binOLMBMBM01_BL.getMemberInfoCount(map);
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if(count != 0) {
			memberInfoList = binOLMBMBM01_BL.getMemberInfoList(map);
		}
		return SUCCESS;
	}
	
	/** 月信息List */
	private List<Integer> monthList;
	
	/** 会员扩展信息List */
	private List<Map<String, Object>> extendPropertyList;
	
	/** 会员等级List */
	private List<Map<String, Object>> memLevelList;
	
	/** 会员信息List */
	private List<Map<String, Object>> memberInfoList;

	public List<Integer> getMonthList() {
		return monthList;
	}

	public void setMonthList(List<Integer> monthList) {
		this.monthList = monthList;
	}

	public List<Map<String, Object>> getExtendPropertyList() {
		return extendPropertyList;
	}

	public void setExtendPropertyList(List<Map<String, Object>> extendPropertyList) {
		this.extendPropertyList = extendPropertyList;
	}

	public List<Map<String, Object>> getMemLevelList() {
		return memLevelList;
	}

	public void setMemLevelList(List<Map<String, Object>> memLevelList) {
		this.memLevelList = memLevelList;
	}

	public List<Map<String, Object>> getMemberInfoList() {
		return memberInfoList;
	}

	public void setMemberInfoList(List<Map<String, Object>> memberInfoList) {
		this.memberInfoList = memberInfoList;
	}

	/** 会员一览画面Form */
	private BINOLMBMBM01_Form form = new BINOLMBMBM01_Form();

	@Override
	public BINOLMBMBM01_Form getModel() {
		return form;
	}
}
