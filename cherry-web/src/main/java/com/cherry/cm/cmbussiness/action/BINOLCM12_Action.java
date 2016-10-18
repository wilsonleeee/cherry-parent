/*
 * @(#)BINOLCM12_Action.java     1.0 2011/03/22
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
package com.cherry.cm.cmbussiness.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM12_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;

/**
 * 扩展属性共通Action
 * 
 * @author lipc
 *
 */
public class BINOLCM12_Action extends BaseAction{

	private static final long serialVersionUID = 395966372586167187L;
	
	@Resource
	private BINOLCM12_BL binOLCM12_BL;
	
	/** 基础属性列数奇校验,1:奇  */
	private int parity;
	
	/** 产品或会员ID  */
	private String id;
	
	/** 品牌ID  */
	private String brandInfoId;
	
	/** 产品扩展属性List */
	@SuppressWarnings("unchecked")
	private List prtExtList;
	
	public int getParity() {
		return parity;
	}

	public void setParity(int parity) {
		this.parity = parity;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}
	@SuppressWarnings("unchecked")
	public List getPrtExtList() {
		return prtExtList;
	}
	@SuppressWarnings("unchecked")
	public void setPrtExtList(List prtExtList) {
		this.prtExtList = prtExtList;
	}
	/**
	 * 添加_编辑产品扩展属性值
	 * 
	 * @return
	 * @throws Exception
	 */
	public String addPrtExt()throws Exception{
		Map<String, Object> map = getMap();
		// 产品扩展属性
		map.put("extendedTable", CherryConstants.EXTENDED_TABLE_PRODUCT);
		// 取得扩展属性List
		prtExtList = binOLCM12_BL.getExtPropertyList(map);
		// 添加添加_编辑画面
		return "BINOLCM12_1";
	}
	
	/**
	 * 显示产品扩展属性及属性值
	 * 
	 * @return
	 * @throws Exception
	 */
	public String showPrtExt()throws Exception{
		Map<String, Object> map = getMap();
		// 产品扩展属性
		map.put("extendedTable", CherryConstants.EXTENDED_TABLE_PRODUCT);
		// 取得扩展属性List
		prtExtList = binOLCM12_BL.getExtValList(map);
		// 详细画面
		return "BINOLCM12_2";
	}
	
	/**
	 * 取得共通信息Map
	 * @return
	 */
	private Map<String, Object> getMap(){
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 品牌信息ID
		map.put(CherryConstants.BRANDINFOID, brandInfoId);
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 产品Id
		map.put("productId", id);
		return map;
	}
}
