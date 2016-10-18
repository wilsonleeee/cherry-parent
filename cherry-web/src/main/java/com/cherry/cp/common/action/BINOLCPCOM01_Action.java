/*	
 * @(#)BINOLCPCOM01_Action.java     1.0 2011/7/18		
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
package com.cherry.cp.common.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cp.common.dto.CampaignDTO;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cp.common.form.BINOLCPCOM01_Form;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 会员活动基本信息 Action
 * 
 * @author hub
 * @version 1.0 2011.7.18
 */
public class BINOLCPCOM01_Action extends BaseAction implements
	ModelDriven<BINOLCPCOM01_Form>{

	private static final long serialVersionUID = -3620552871767704377L;
	
	@Resource
	private BINOLCM05_BL binOLCM05_BL;
	
	/** 参数FORM */
	private BINOLCPCOM01_Form form = new BINOLCPCOM01_Form();
	
	/** 所管辖的品牌List */
	private List<Map<String, Object>> brandInfoList;
	
	@Override
	public BINOLCPCOM01_Form getModel() {
		return form;
	}
	
	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
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
		session.remove("wfId");
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE,  session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 总部用户登录的时候
		if (CherryConstants.BRAND_INFO_ID_VALUE == userInfo.getBIN_BrandInfoID()) {
			// 取得所管辖的品牌List
			brandInfoList = binOLCM05_BL.getBrandInfoList(map);
		} else {
			// 品牌信息
			Map<String, Object> brandInfo = new HashMap<String, Object>();
			// 品牌ID
			brandInfo.put("brandInfoId", userInfo.getBIN_BrandInfoID());
			// 品牌名称
			brandInfo.put("brandName", userInfo.getBrandName());
			brandInfoList = new ArrayList<Map<String, Object>>();
			brandInfoList.add(brandInfo);
		}
		CampaignDTO campInfo = new CampaignDTO();
		// 创建者
		//campInfo.setCampaignSetBy(userInfo.getLoginName());
		form.setCampInfo(campInfo);
		return SUCCESS;
	}
}
