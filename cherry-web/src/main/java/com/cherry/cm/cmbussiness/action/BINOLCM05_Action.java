/*
 * @(#)BINOLCM05_BL.java     1.0 2010/11/19
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
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;

/**
 * 促销品共通 BL
 * 
 * @author hub
 * @version 1.0 2010.11.19
 */
@SuppressWarnings("unchecked")
public class BINOLCM05_Action extends BaseAction{

	private static final long serialVersionUID = 395966372586167187L;
	
	@Resource
	private BINOLCM05_BL binOLCM05_BL;
	
	/** 品牌信息ID */
	private String brandInfoId;
	
	/** 大分类代码 */
	private String primaryCateCode;
	
	/** 中分类代码 */
	private String secondCateCode;
	
	public String getBrandInfoId() {
		return brandInfoId;
	}

	public void setBrandInfoId(String brandInfoId) {
		this.brandInfoId = brandInfoId;
	}
	
	public String getPrimaryCateCode() {
		return primaryCateCode;
	}

	public void setPrimaryCateCode(String primaryCateCode) {
		this.primaryCateCode = primaryCateCode;
	}

	public String getSecondCateCode() {
		return secondCateCode;
	}

	public void setSecondCateCode(String secondCateCode) {
		this.secondCateCode = secondCateCode;
	}

	/**
	 * AJAX 取得类型和分类等List并返回JSON对象
	 * 
	 * @throws Exception
	 */
	public void queryCate() throws Exception {
		Map<String, Object> map = getMap();
//		// 取得促销产品类型List
//		List promPrtCateList = binOLCM05_BL.getPromPrtCateList(map);
		// 取得大分类List
		List PrimaryCateList = binOLCM05_BL.getPrimaryCateList(map);
//		// 取得最小包装类型List
//		List minPackTypeList = binOLCM05_BL.getMinPackageTypeList(map);
		Map<String, Object> jsonMap = new HashMap<String, Object>();
//		jsonMap.put("promPrtCateList", promPrtCateList);
		jsonMap.put("primaryCateList", PrimaryCateList);
//		jsonMap.put("minPackTypeList", minPackTypeList);
		// 响应JSON对象
		ConvertUtil.setResponseByAjax(response, jsonMap);
	}
	
	/**
	 * AJAX 取得产品类别List并返回JSON对象
	 * 
	 * @throws Exception
	 */
	public void queryPrtCate() throws Exception {
		Map<String, Object> map = getMap();
		// 取得产品类型List
		List prtCateList = binOLCM05_BL.getPrtCateList(map);
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("prtCateList", prtCateList);
		// 响应JSON对象
		ConvertUtil.setResponseByAjax(response, jsonMap);
	}
	
	/**
	 * AJAX 取得生产厂商信息并返回JSON对象
	 * 
	 * @throws Exception
	 */
	public void queryFactory() throws Exception {
		Map<String, Object> map = getMap();
		// 取得默认显示的生产厂商信息
		Map factoryInfo = binOLCM05_BL.getFactoryInfo(map);
		// 响应JSON对象
		ConvertUtil.setResponseByAjax(response, factoryInfo);
	}
	
	/**
	 * AJAX 取得中分类并返回JSON对象
	 * 
	 * @throws Exception
	 */
	public void querySecondCate() throws Exception {
		Map<String, Object> map = getMap();
		// 大分类代码
		map.put("primaryCateCode", primaryCateCode);
		// 取得中分类List
		List secondCateList = binOLCM05_BL.getSecondCateList(map);
		// 响应JSON对象
		ConvertUtil.setResponseByAjax(response, secondCateList);
	}
	
	/**
	 * AJAX 取得小分类并返回JSON对象
	 * 
	 * @throws Exception
	 */
	public void querySmallCate() throws Exception {
		Map<String, Object> map = getMap();
		// 大分类代码
		map.put("primaryCateCode", primaryCateCode);
		// 中分类代码
		map.put("secondCateCode", secondCateCode);
		// 取得小分类List
		List smallCateList = binOLCM05_BL.getSmallCateList(map);
		// 响应JSON对象
		ConvertUtil.setResponseByAjax(response, smallCateList);
	}
	
	/**
	 * AJAX 取得会员俱乐部列表并返回JSON对象
	 * 
	 * @throws Exception
	 */
	public void queryClub() throws Exception {
		Map<String, Object> map = getMap();
		// 取得会员俱乐部列表
		List clubList = binOLCM05_BL.getClubList(map);
		// 响应JSON对象
		ConvertUtil.setResponseByAjax(response, clubList);
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
		return map;
	}
	
	/**
	 * <p>
	 * 判断session时间是否失效
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * @throws Exception 
	 * 
	 */
    public String isSessionTimeout() throws Exception{
		return null;
    }
}
