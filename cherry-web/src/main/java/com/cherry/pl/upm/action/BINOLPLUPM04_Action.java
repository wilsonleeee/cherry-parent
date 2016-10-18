/*	
 * @(#)BINOLPLUPM04_Action.java     1.0 2010/12/28		
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
package com.cherry.pl.upm.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.pl.upm.bl.BINOLPLUPM04_BL;
import com.cherry.pl.upm.form.BINOLPLUPM04_Form;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 安全策略Action
 * 
 * 
 * 
 * @author hub
 * @version 1.0 2010.12.28
 */
@SuppressWarnings("unchecked")
public class BINOLPLUPM04_Action extends BaseAction implements
		ModelDriven<BINOLPLUPM04_Form> {

	private static final long serialVersionUID = -5957638690200650492L;
	
	@Resource
	private BINOLPLUPM04_BL binOLPLUPM04_BL;
	
	@Resource
	private BINOLCM05_BL binOLCM05_BL;
	
	/** 参数FORM */
	private BINOLPLUPM04_Form form = new BINOLPLUPM04_Form();
	
	/** 密码安全配置信息 */
	private Map pwConfInfo;
	
	/** 所管辖的品牌List */
	private List brandInfoList;
	
	/** 品牌名称 */
	private String brandName;
	
	public BINOLPLUPM04_Form getModel() {
		return form;
	}

	public Map getPwConfInfo() {
		return pwConfInfo;
	}

	public void setPwConfInfo(Map pwConfInfo) {
		this.pwConfInfo = pwConfInfo;
	}
	
	public List getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List brandInfoList) {
		this.brandInfoList = brandInfoList;
	}
	
	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	/**
	 * 画面初始化
	 * 
	 * @return
	 * @throws Exception
	 */
	public String init() throws Exception {
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
		int brandInfoId = userInfo.getBIN_BrandInfoID();
		// 总部用户登录的时候
		if (CherryConstants.BRAND_INFO_ID_VALUE == brandInfoId) {
			// 取得所管辖的品牌List
			brandInfoList = binOLCM05_BL.getBrandInfoList(map);
			Map brandMap = new HashMap();
			// 品牌ID
			brandMap.put("brandInfoId", brandInfoId);
			// 品牌名称
			brandMap.put("brandName", getText("PPL00006"));
			if (null != brandInfoList && !brandInfoList.isEmpty()) {
				brandInfoList.add(0, brandMap);
			} else {
				brandInfoList = new ArrayList();
				brandInfoList.add(brandMap);
			}
		} else {
			// 取得品牌名称
			brandName = userInfo.getBrandName();
			// 品牌ID
			form.setBrandInfoId(ConvertUtil.getString(brandInfoId));
		}
		map.put(CherryConstants.BRANDINFOID, brandInfoId);
		// 取得密码安全配置信息
		pwConfInfo = binOLPLUPM04_BL.getPwConfInfo(map);
		// 取得密码组合
		binOLPLUPM04_BL.getPwComb(pwConfInfo);
		return SUCCESS;
	}
	
	/**
	 * 根据品牌取得密码安全配置信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String change() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 品牌信息ID
		map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		// 取得密码安全配置信息
		pwConfInfo = binOLPLUPM04_BL.getPwConfInfo(map);
		// 取得密码组合
		binOLPLUPM04_BL.getPwComb(pwConfInfo);
		return SUCCESS;
	}

}