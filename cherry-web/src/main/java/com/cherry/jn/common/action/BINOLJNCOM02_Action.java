/*	
 * @(#)BINOLJNCOM02_Action.java     1.0 2011/4/18		
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
package com.cherry.jn.common.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.jn.common.form.BINOLJNCOM02_Form;
import com.cherry.jn.common.interfaces.BINOLJNCOM02_IF;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 会员活动组一览 Action
 * 
 * @author hub
 * @version 1.0 2011.4.18
 */
public class BINOLJNCOM02_Action extends BaseAction implements
	ModelDriven<BINOLJNCOM02_Form>{

	private static final long serialVersionUID = 250444132665523563L;
	
	/** 参数FORM */
	private BINOLJNCOM02_Form form = new BINOLJNCOM02_Form();
	
	@Resource
	private BINOLCM05_BL binOLCM05_BL;
	
	@Resource
	private BINOLJNCOM02_IF binoljncom02IF;
	
	/** 促销产品List */
	private List campaignGrpList;
	
	/** 所管辖的品牌List */
	private List brandInfoList;
	
	@Override
	public BINOLJNCOM02_Form getModel() {
		return form;
	}
	
	public List getCampaignGrpList() {
		return campaignGrpList;
	}

	public void setCampaignGrpList(List campaignGrpList) {
		this.campaignGrpList = campaignGrpList;
	}
	
	public List getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List brandInfoList) {
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
		// 参数Map
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 品牌ID
		map.put(CherryConstants.BRANDINFOID, userInfo
				.getBIN_BrandInfoID());
		// 总部用户登录的时候
		if (CherryConstants.BRAND_INFO_ID_VALUE == userInfo
				.getBIN_BrandInfoID()) {
			// 取得所管辖的品牌List
			brandInfoList = binOLCM05_BL.getBrandInfoList(map);
		}else{
			brandInfoList = new ArrayList<Map<String, Object>>();
			Map<String, Object> temp = new HashMap<String, Object>();
			temp.put(CherryConstants.BRANDINFOID, userInfo
				.getBIN_BrandInfoID());
			String brandName = binOLCM05_BL.getBrandName(map);
			temp.put(CherryConstants.BRAND_NAME, brandName);
			brandInfoList.add(temp);
		}
		return SUCCESS;
	}
	
	/**
	 * <p>
	 * AJAX会员活动组查询
	 * </p>
	 * 
	 * @return
	 */
	public String search() throws Exception {
		// 取得参数MAP
		Map<String, Object> searchMap = getSearchMap();
		// 取得会员活动组总数
		int count = binoljncom02IF.getCampaignGrpCount(searchMap);
		if (count > 0) {
			// 取得会员活动组信息List
			campaignGrpList = binoljncom02IF.getCampaignGrpList(searchMap);
		}
		// form表单设置
		// 显示记录
		form.setITotalDisplayRecords(count);
		// 总记录
		form.setITotalRecords(count);
		// AJAX返回至dataTable结果页面
		return SUCCESS;
	}
	
	/**
	 * 查询参数MAP取得
	 * 
	 * @param tableParamsDTO
	 * @throws Exception 
	 */
	private Map<String, Object> getSearchMap() throws Exception {
		// 参数MAP
		Map<String, Object> map = (Map<String, Object>)Bean2Map.toHashMap(form);
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// form参数设置到paramMap中
		ConvertUtil.setForm(form, map);
		return map;
	}
}
