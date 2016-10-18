/*
 * @(#)BINOLBSDEP10_Action.java     1.0 2011.2.10
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
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.dep.bl.BINOLBSDEP10_BL;
import com.cherry.bs.dep.form.BINOLBSDEP10_Form;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 品牌一览画面Action
 * 
 * @author WangCT
 * @version 1.0 2011.2.10
 */
public class BINOLBSDEP10_Action extends BaseAction implements ModelDriven<BINOLBSDEP10_Form> {

	private static final long serialVersionUID = -5292607141934399498L;
	
	/** 品牌一览画面Form */
	private BINOLBSDEP10_Form form = new BINOLBSDEP10_Form();
	
	/** 品牌一览画面BL */
	@Resource
	private BINOLBSDEP10_BL binOLBSDEP10_BL;
	
	/**
	 * 品牌一览画面初期处理
	 * 
	 * @return 品牌一览画面 
	 */
	public String init() {
		
		return SUCCESS;
	}
	
	/**
	 * 查询品牌一览处理
	 * 
	 * @return 品牌一览画面 
	 */
	public String brandInfoList() {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 组织ID
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		if(form.getBrandCode() != null && !"".equals(form.getBrandCode())) {
			// 品牌代码
			map.put("brandCode", form.getBrandCode());
		}
		if(form.getBrandNameKw() != null && !"".equals(form.getBrandNameKw())) {
			// 品牌名称
			map.put("brandNameKw", form.getBrandNameKw());
		}
		if(form.getValidFlag() != null && !"".equals(form.getValidFlag())) {
			// 有效区分
			map.put("validFlag", form.getValidFlag());
		}
		// dataTable上传的参数设置到map
		ConvertUtil.setForm(form, map);
		// 查询品牌总数
		int count = binOLBSDEP10_BL.getBrandInfoCount(map);
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		if(count != 0) {
			// 取得品牌信息List
			brandInfoList = binOLBSDEP10_BL.getBrandInfoList(map);
		}
		return SUCCESS;
	}
	
	/** 品牌信息List */
	private List<Map<String, Object>> brandInfoList;

	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}

	@Override
	public BINOLBSDEP10_Form getModel() {
		return form;
	}

}
