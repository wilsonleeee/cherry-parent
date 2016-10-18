/*	
 * @(#)BINOLCPACT09_Action.java     1.0 @2013-07-25	
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
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherrySecret;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cp.act.form.BINOLCPACT07_Form;
import com.cherry.cp.act.interfaces.BINOLCPACT07_IF;
import com.cherry.cp.act.interfaces.BINOLCPACT08_IF;
import com.googlecode.jsonplugin.JSONException;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 领用结果一览Action
 * 
 * @author luohong
 * 
 * @version 1.0 2013/07/25
 */
public class BINOLCPACT09_Action extends BaseAction implements
		ModelDriven<BINOLCPACT07_Form> {

	private static final long serialVersionUID = 8401859292573607265L;

	@Resource(name = "binOLCM00_BL")
	private BINOLCM00_BL binOLCM00_BL;

	@Resource(name = "binOLCM05_BL")
	private BINOLCM05_BL binOLCM05_BL;

	/** 接口 */
	@Resource(name = "binOLCPACT07_IF")
	private BINOLCPACT07_IF binOLCPACT07_IF;

	/** 参数FORM */
	private BINOLCPACT07_Form form = new BINOLCPACT07_Form();

	/** 品牌List */
	private List<Map<String, Object>> brandInfoList;

	private List<Map<String, Object>> giftDrawList;
	
	/** 接口 */
	@Resource(name = "binOLCPACT08_IF")
	private BINOLCPACT08_IF binOLCPACT08_IF;

	/** 礼品领用单据基本信息 */
	private Map getGiftDrawDetail;

	/** 礼品领用产品信息List */
	private List getGiftDrawPrtDetail;

	/**
	 * 初始化
	 * 
	 * @return
	 * @throws JSONException
	 */
	public String init() throws JSONException {
		Map<String, Object> map = getSearchMap();
//		// 开始日期
//		form.setStartDate(binOLCM00_BL.getFiscalDate(
//				ConvertUtil.getInt(map.get(CherryConstants.ORGANIZATIONINFOID)), new Date()));
//		// 截止日期
//		form.setEndDate(CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN));
		// 品牌List
		brandInfoList = binOLCM05_BL.getBrandList(session);
		return "BINOLCPACT09";
	}

	/**
	 * 查询
	 * 
	 * @return
	 * @throws Exception 
	 */
	public String search() throws Exception {
		Map<String, Object> map = getSearchMap();
		// 开始时间
		map.put("startDate", form.getStartDate());
		// 结束时间
		map.put("endDate", form.getEndDate());
		// coupon码
		map.put("couponCode", form.getCouponCode().trim());
		// 礼品领用单据号
		map.put("billNoIF", form.getBillNoIF().trim());
		// 领用柜台号
		map.put("counterCode", form.getCounterCode().trim());
		// 会员卡号
		map.put("memberCode", form.getMemberCode().trim());
		// 会员手机号
		map.put("mobilePhone", form.getMobilePhone().trim());
		// 主题活动代号
		map.put("activityCode", form.getActivityCode().trim());
		// 会员测试区分
		map.put("testTypes", form.getTestType());
		//业务类型
		map.put("businessType", "1");
		// 品牌Code
		String brandCode = ConvertUtil.getString(map.get("brandCode"));
		// 会员【手机号码】字段加密
		if (!CherryChecker.isNullOrEmpty(map.get("mobilePhone"), true)) {
			String mobilePhone = ConvertUtil.getString(map.get("mobilePhone"));
			map.put("mobilePhone", CherrySecret.encryptData(brandCode,mobilePhone));
		}
		int count = binOLCPACT07_IF.getGiftDrawCount(map);
		if (count > 0) {
			giftDrawList = binOLCPACT07_IF.getGiftDrawList(map);
		}
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		return "BINOLCPACT09_1";
	}
	/**
	 * 领用结果详细
	 * @return
	 */
	public String getPrtDetail() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("giftDrawId", form.getGiftDrawId());
		// 取得此礼品领用单据的基本信息
		getGiftDrawDetail = binOLCPACT08_IF.getGiftDrawDetail(map);
		if (!CherryChecker.isNullOrEmpty(getGiftDrawDetail)) {
			map.put("giftDrawId", getGiftDrawDetail.get("giftDrawId"));
			getGiftDrawPrtDetail = binOLCPACT08_IF.getGiftDrawPrtDetail(map);
		}
		return "BINOLCPACT09_2";
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
		// 当前用户的所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID,
				userInfo.getBIN_OrganizationInfoID());
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE,
				session.get(CherryConstants.SESSION_LANGUAGE));
		// 品牌
		if (!CherryChecker.isNullOrEmpty(form.getBrandInfoId())) {
			map.put("brandInfoId", form.getBrandInfoId());
		}
		map.put("brandCode", userInfo.getBrandCode());
		return map;
	}

	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}
	public List<Map<String, Object>> getGiftDrawList() {
		return giftDrawList;
	}

	public void setGiftDrawList(List<Map<String, Object>> giftDrawList) {
		this.giftDrawList = giftDrawList;
	}

	public Map getGetGiftDrawDetail() {
		return getGiftDrawDetail;
	}

	public void setGetGiftDrawDetail(Map getGiftDrawDetail) {
		this.getGiftDrawDetail = getGiftDrawDetail;
	}

	public List getGetGiftDrawPrtDetail() {
		return getGiftDrawPrtDetail;
	}

	public void setGetGiftDrawPrtDetail(List getGiftDrawPrtDetail) {
		this.getGiftDrawPrtDetail = getGiftDrawPrtDetail;
	}

	@Override
	public BINOLCPACT07_Form getModel() {

		return form;
	}

}
