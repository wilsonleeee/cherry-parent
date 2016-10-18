/*	
 * @(#)BINOLBSFAC01_Action.java     1.0 2011/02/14	
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
package com.cherry.bs.fac.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.fac.bl.BINOLBSFAC01_BL;
import com.cherry.bs.fac.form.BINOLBSFAC01_Form;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 生产厂商查询Action
 * 
 * 
 * 
 * @author lipc
 * @version 1.0 2011.02.14
 */
public class BINOLBSFAC01_Action extends BaseAction implements
		ModelDriven<BINOLBSFAC01_Form> {

	private static final long serialVersionUID = -613953742858069230L;

	/** 参数FORM */
	private BINOLBSFAC01_Form form = new BINOLBSFAC01_Form();

	@Resource
	private BINOLBSFAC01_BL binolbsfac01BL;

	/** 生产厂商List */
	private List<Map<String, Object>> factoryList;

	@Override
	public BINOLBSFAC01_Form getModel() {
		return form;
	}

	public List<Map<String, Object>> getFactoryList() {
		return factoryList;
	}

	public void setFactoryList(List<Map<String, Object>> factoryList) {
		this.factoryList = factoryList;
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

		return SUCCESS;
	}

	/**
	 * <p>
	 * AJAX生产厂商查询
	 * </p>
	 * 
	 * @return
	 */
	public String search() throws Exception {

		// 取得参数MAP
		Map<String, Object> searchMap = getSearchMap();
		// 生产厂商总数取得
		int count = binolbsfac01BL.getFacCount(searchMap);
		// 生产厂商List取得
		factoryList = binolbsfac01BL.getFactoryList(searchMap);
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		// AJAX返回至dataTable结果页面
		return "BINOLBSFAC01_1";
	}

	/**
	 * 查询参数MAP取得
	 * 
	 */
	private Map<String, Object> getSearchMap() {
		// 参数MAP
		Map<String, Object> map = new HashMap<String, Object>();

		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// form参数设置到paramMap中
		ConvertUtil.setForm(form, map);
		// 登陆用户所属组织ID
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 登陆用户所属品牌ID
		int brandInfoId = userInfo.getBIN_BrandInfoID();
		// 登陆用户不是总部员工
		if (brandInfoId != CherryConstants.BRAND_INFO_ID_VALUE) {
			map.put(CherryConstants.BRANDINFOID, brandInfoId);
		}
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 有效区分
		map.put("validFlag", form.getValidFlag());
		// 生产厂商编码
		map.put("manufacturerCode", form.getManufacturerCode().trim());
		// 生产厂商名
		map.put("factoryName", form.getFactoryName().trim());
		
		return map;
	}
}
