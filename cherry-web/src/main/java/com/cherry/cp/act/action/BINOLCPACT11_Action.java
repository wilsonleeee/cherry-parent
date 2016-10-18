/*	
 * @(#)BINOLCPACT11_Action.java     1.0 @2014-01-13		
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
package com.cherry.cp.act.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherrySecret;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cp.act.form.BINOLCPACT11_Form;
import com.cherry.cp.act.interfaces.BINOLCPACT11_IF;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 活动履历一览Action
 * 
 * @author LUOHONG
 * 
 */
public class BINOLCPACT11_Action extends BaseAction implements
		ModelDriven<BINOLCPACT11_Form> {

	private static final long serialVersionUID = 7923159111288271440L;

	/** 活动履历参数FORM */
	private BINOLCPACT11_Form form = new BINOLCPACT11_Form();

	/** 活动履历接口 */
	@Resource(name = "binOLCPACT11_IF")
	private BINOLCPACT11_IF binOLCPACT11_IF;

	/** 活动履历信息List */
	private List<Map<String,Object>> campHistoryList;

	public List<Map<String, Object>> getCampHistoryList() {
		return campHistoryList;
	}

	public void setCampHistoryList(List<Map<String, Object>> campHistoryList) {
		this.campHistoryList = campHistoryList;
	}

	public String init() {
		// 取得活动履历基本信息
		return "BINOLCPACT11";
	}

	/**
	 * 活动履历查询
	 * @return
	 * @throws Exception 
	 */
	public String search() throws Exception {
		Map<String, Object> map = getSearchMap();
		//活动Code
		map.put("campaignCode", form.getCampCode().trim());
		//批次号
		map.put("batchNo", form.getBatchNo().trim());
		//单据号
		map.put("tradeNoIF", form.getTradeNoIF().trim());
		//会员卡号
		map.put("memCode", form.getMemCode().trim());
		//手机号码
		map.put("mobilePhone", form.getMobilePhone().trim());
		// 品牌Code
		String brandCode = ConvertUtil.getString(map.get("brandCode"));
		// 会员【手机号码】字段加密
		if (!CherryChecker.isNullOrEmpty(map.get("mobilePhone"), true)) {
			String mobilePhone = ConvertUtil.getString(map.get("mobilePhone"));
			map.put("mobilePhone", CherrySecret.encryptData(brandCode,mobilePhone));
		}
//		//活动柜台
//		map.put("departName", form.getDepartName().trim());
		//单据状态
		map.put("state", form.getState());
		int count = binOLCPACT11_IF.getCampHistoryCount(map);
		if(count>0){
			campHistoryList = binOLCPACT11_IF.getCampHistoryList(map);
		}
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		return "BINOLCPACT11_1";
	}
	
	/**
	 * 取得查询参数
	 * 
	 * @return
	 */
	private Map<String, Object> getSearchMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		ConvertUtil.setForm(form, map);
		// 当前用户的ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 当前用户的所属品牌
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 当前用户的所属品牌Code
		map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
		// 当前用户的所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID,
				userInfo.getBIN_OrganizationInfoID());
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE,
				session.get(CherryConstants.SESSION_LANGUAGE));
		//开始时间
		map.put("startDate", form.getStartDate().trim());	
		//结束时间
		map.put("endDate", form.getEndDate().trim());	
		return map;
	}

	@Override
	public BINOLCPACT11_Form getModel() {
		return form;
	}

}
