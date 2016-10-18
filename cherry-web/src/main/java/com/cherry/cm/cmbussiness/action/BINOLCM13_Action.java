/*
 * @(#)BINOLCM13_Action.java     1.0 2011/03/29
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

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM42_BL;
import com.cherry.cm.cmbussiness.form.BINOLCM13_Form;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 选择范围共通Action
 * 
 * @author lipc
 * 
 */
public class BINOLCM13_Action extends BaseAction implements
		ModelDriven<BINOLCM13_Form> {

	private static final long serialVersionUID = 395966372586167187L;

	@Resource
	private BINOLCM00_BL binOLCM00_BL;
	
	@Resource
	private BINOLCM42_BL binOLCM42_BL;
	
	/** 参数FORM */
	private BINOLCM13_Form form = new BINOLCM13_Form();

	public BINOLCM13_Form getModel() {
		return form;
	}

	/** 筛选模式 */
	private String[] modeArr;

	/** 部门等级List */
	private List<Map<String, Object>> gradeList;
	
	/** 实体仓库List */
	private List<Map<String, Object>> depotList;
	
	/** 逻辑仓库List */
	private List<Map<String, Object>> lgcInventoryList;
	
	/** 区域List */
	private List<Map<String, Object>> areaList;

	/** 渠道List */
	private List<Map<String, Object>> channelList;

	/** 柜台List */
	private List<Map<String, Object>> counterList;

	public String[] getModeArr() {
		return modeArr;
	}

	public void setModeArr(String[] modeArr) {
		this.modeArr = modeArr;
	}

	public List getGradeList() {
		return gradeList;
	}

	public void setGradeList(List gradeList) {
		this.gradeList = gradeList;
	}

	public List<Map<String, Object>> getAreaList() {
		return areaList;
	}

	public void setAreaList(List<Map<String, Object>> areaList) {
		this.areaList = areaList;
	}

	public List<Map<String, Object>> getDepotList() {
		return depotList;
	}

	public void setDepotList(List<Map<String, Object>> depotList) {
		this.depotList = depotList;
	}

	public List<Map<String, Object>> getLgcInventoryList() {
		return lgcInventoryList;
	}

	public void setLgcInventoryList(List<Map<String, Object>> lgcInventoryList) {
		this.lgcInventoryList = lgcInventoryList;
	}

	public List<Map<String, Object>> getChannelList() {
		return channelList;
	}

	public void setChannelList(List<Map<String, Object>> channelList) {
		this.channelList = channelList;
	}

	public List<Map<String, Object>> getCounterList() {
		return counterList;
	}

	public void setCounterList(List<Map<String, Object>> counterList) {
		this.counterList = counterList;
	}

	/**
	 * 取得部门联动画面
	 * 
	 * @return
	 * @throws Exception
	 */
	public String queryBINOLCM13() throws Exception {
		// 参数信息Map
		Map<String, Object> paramMap = getMap();
		// 取得显示模式数组
		modeArr = binOLCM42_BL.getMode(paramMap);
		if(null != modeArr){
			for (int i = 0; i < modeArr.length; i++) {
				if (CherryConstants.MODE_DEPART.equals(modeArr[i])) {
					// 部门等级List
					gradeList = binOLCM42_BL.getGradeList(paramMap);
				} else if (CherryConstants.MODE_AREA.equals(modeArr[i])) {
					// 区域list
					areaList = binOLCM00_BL.getReginList(paramMap);
				}else if (CherryConstants.MODE_CHANNEL.equals(modeArr[i])) {
					// 渠道
					channelList = binOLCM42_BL.getChannelList(paramMap);
				} else if (CherryConstants.MODE_DEPOT.equals(modeArr[i])) {
					if(form.getShowLgcDepot() > 0){
						// 逻辑仓库
						lgcInventoryList = binOLCM42_BL.getLgcInventoryList(paramMap);
					}
				}
			}
		}
		return SUCCESS;
	}

	/**
	 * AJAX 取得部门List
	 * 
	 * @throws Exception
	 */
	public void queryDepartBINOLCM13() throws Exception {
		Map<String, Object> map = getMap();
		// 响应JSON对象
		ConvertUtil.setResponseByAjax(response, binOLCM42_BL.getNextDepartList(map));
	}
	/**
	 * AJAX 取得部门List
	 * 
	 * @throws Exception
	 */
	public void queryDepart2BINOLCM13() throws Exception {
		Map<String, Object> map = getMap();
		String departInfo = binOLCM42_BL.getCounterList(map);
		// 响应JSON对象
		ConvertUtil.setResponseByAjax(response, departInfo);
	}

	/**
	 * AJAX 取得实体仓库List
	 * 
	 * @throws Exception
	 */
	public void queryDepotBINOLCM13() throws Exception {
		Map<String, Object> map = getMap();
		// 取得部门下实体仓库List
		String depotInfo = binOLCM42_BL.getDepotInfo(map);
		// 响应JSON对象
		ConvertUtil.setResponseByAjax(response, depotInfo);
	}
	
	/**
	 * AJAX 取得逻辑仓库List
	 * 
	 * @throws Exception
	 */
	public void queryLgcDepotBINOLCM13() throws Exception {
		Map<String, Object> map = getMap();
		map.put("depotId", form.getDepotId());
		// 响应JSON对象
		ConvertUtil.setResponseByAjax(response, binOLCM42_BL.getLgcInventoryList(map));
	}

	/**
	 * 取得共通参数Map
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> getMap() throws Exception{
		// 参数MAP
		Map<String, Object> map = (Map<String, Object>) Bean2Map
				.toHashMap(form);
		if(!CherryChecker.isNullOrEmpty(form.getParams())){
			// JSON参数
			Map<String, Object> paramsMap = (Map<String, Object>) JSONUtil.deserialize(form.getParams());
			map.putAll(paramsMap);
			map.remove("params");
		}
		map = CherryUtil.removeEmptyVal(map);
		map.put("DEPARTTYPE", form.getDepartType());
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// userId
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 组织Id
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 品牌Id
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 品牌code
		map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
		if (CherryChecker.isNullOrEmpty(form.getShowType())) {
			// 默认是否显示部门类型
			form.setShowType("1");
		}
		// 测试类型
		String testType = ConvertUtil.getString(map.get("testType"));
		// 部门有效性
		String orgValid = ConvertUtil.getString(map.get("orgValid"));
		// 包含启用停用部门
		String orgValidAll = ConvertUtil.getString(map.get("orgValidAll"));
		if("".equals(testType)){
			map.put("testType", "0");
			form.setTestType("0");
		}
		if("".equals(orgValid)){
			map.put("orgValid", "1");
			form.setOrgValid("1");
		}
		if("".equals(orgValidAll)) {
			map.put("orgValidAll", "0");
			form.setOrgValidAll("0");
		}
		
		return map;
	}
}
