/*
 * @(#)BINOLPLIFC01_Action.java     1.0 2013/06/28
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

package com.cherry.pl.ifc.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.pl.ifc.form.BINOLPLIFC01_Form;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 沟通模板一览Action
 * 
 * @author ZhangGS
 * @version 1.0 2013.06.28
 */
public class BINOLPLIFC01_Action extends BaseAction implements ModelDriven<BINOLPLIFC01_Form>{
	
	private static final long serialVersionUID = 1L;
	
	private BINOLPLIFC01_Form form = new BINOLPLIFC01_Form();
	
	@Resource
	private BINOLCM05_BL binolcm05_BL;
	
	/** 品牌List */
	private List<Map<String, Object>> brandList;
	
	public List<Map<String, Object>> getBrandList() {
		return brandList;
	}

	public void setBrandList(List<Map<String, Object>> brandList) {
		this.brandList = brandList;
	}

	@SuppressWarnings("unchecked")
	public String init() throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 品牌ID
		int brandId = userInfo.getBIN_BrandInfoID();
		// 总部用户登录的时候
		if (CherryConstants.BRAND_INFO_ID_VALUE == brandId) {
			// 取得所管辖的品牌List
			brandList = binolcm05_BL.getBrandInfoList(map);
		}else {
			String brandName = userInfo.getBrandName();
			form.setBrandName(brandName);
			form.setBrandInfoId(ConvertUtil.getString(brandId));
		}
		
		return SUCCESS;
	}
	
	@Override
	public BINOLPLIFC01_Form getModel() {
		return form;
	}
}
