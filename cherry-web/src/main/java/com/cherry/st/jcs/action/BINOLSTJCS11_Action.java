/*
 * @(#)BINOLSTJCS11_Action.java     1.0 2014/06/20
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
package com.cherry.st.jcs.action;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.LoggerFactory;


import org.slf4j.Logger;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.st.jcs.form.BINOLSTJCS11_Form;
import com.cherry.st.jcs.interfaces.BINOLSTJCS11_IF;
import com.opensymphony.xwork2.ModelDriven;

/**
 * BB柜台维护Action
 * 
 * @author zhangle
 * @version 1.0 2014.06.20
 */
public class BINOLSTJCS11_Action extends BaseAction implements
		ModelDriven<BINOLSTJCS11_Form> {

	private static final long serialVersionUID = 1940893461762579509L;
	private static final Logger logger = LoggerFactory.getLogger(BINOLSTJCS11_Action.class);
	
	/** 参数From */
	private BINOLSTJCS11_Form form = new BINOLSTJCS11_Form();
	/** 接口 */
	@Resource(name = "binOLSTJCS11_BL")
	private BINOLSTJCS11_IF binOLSTJCS11_BL;
	
	/** 共通BL */
	@Resource(name="binOLCM05_BL")
	private BINOLCM05_BL binOLCM05_BL;
	
	private List<Map<String, Object>> BBCounterList;

	private Map<String, Object> BBCounterInfo;
	
	/** 上传的文件 */
	private File upExcel;

	/** 上传的文件名，不包括路径 */
	private String upExcelFileName;
	
	/** 品牌List */
	private List<Map<String, Object>> brandInfoList;

	/**
	 * 
	 * 初始画面
	 * 
	 */
	public String init() {
		return SUCCESS;
	}

	/**
	 * 
	 * 获取列表
	 * */
	public String search(){
		Map<String, Object> map = this.getParamsMap();
		map.put("departCode", form.getDepartCode());
		map.put("departName", form.getDepartName());
		map.put("batchCode", form.getBatchCode());
		map.put("validFlag", form.getValidFlag());
		ConvertUtil.setForm(form, map);
		int count = binOLSTJCS11_BL.getBBCounterCount(map);
		if(count > 0){
			BBCounterList = binOLSTJCS11_BL.getBBCounterList(map);
			form.setITotalDisplayRecords(count);
			form.setITotalRecords(count);
		}
		return SUCCESS;
	}

	public String view() {
		Map<String, Object> map = this.getParamsMap();
		map.put("BBCounterInfoId", form.getBBCounterInfoId());
		BBCounterInfo = binOLSTJCS11_BL.getBBCounter(map);
		return SUCCESS;
	}

	/**
	 * 编辑初始化
	 */
	public String edit() {
		Map<String, Object> map = this.getParamsMap();
		map.put("BBCounterInfoId", form.getBBCounterInfoId());
		BBCounterInfo = binOLSTJCS11_BL.getBBCounter(map);
		return SUCCESS;
	}
	
	/**
	 * 添加初始化
	 */
	public String add() {
		return SUCCESS;
	}

	/**
	 * 保存
	 */
	public String save() {
		try{
			String startTime = form.getStartDate() + " " + form.getStartTime();
			String endTime = form.getEndDate() + " " + form.getEndTime();
			if(!CherryChecker.isNullOrEmpty(endTime, true) && endTime.compareTo(startTime) <= 0){
				this.addActionError(getText("ECM00027", new String[]{getText("PCP00034"), getText("PCP00033")}));
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}
			Map<String, Object> map = this.getParamsMap();
			map.put("batchCode", form.getBatchCode());
			map.put("BBCounterInfoId", form.getBBCounterInfoId());
			map.put("startTime", startTime);
			map.put("endTime", endTime);
			map.put("comments", form.getComments());
			map.put("organizationId", form.getOrganizationId());
			map.put("brandInfoId", form.getBrandInfoId());
			binOLSTJCS11_BL.tran_save(map);
			this.addActionMessage(getText("ICM00001"));
		}catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			if(e instanceof CherryException){
				this.addActionError(e.getMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}else{
				this.addActionError(getText("ECM00005"));
			}
		}
		return CherryConstants.GLOBAL_ACCTION_RESULT_PAGE;
	}
	
	/**
	 * 停用或启用
	 * @return
	 */
	public String disabled() {
		try{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("allBBCounterInfoId", form.getAllBBCounterInfoId());
			map.put("validFlag", form.getValidFlag());
			binOLSTJCS11_BL.tran_disabled(map);
			this.addActionMessage(getText("ICM00001"));
		}catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			if(e instanceof CherryException){
				this.addActionError(e.getMessage());
			}else{
				this.addActionError(getText("ECM00005"));
			}
		}
		return CherryConstants.GLOBAL_ACCTION_RESULT;
	}

	/**
	 * 导入初始化
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String importInit() {
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
			brandInfoList = binOLCM05_BL.getBrandInfoList(map);
		}else {
			Map<String, Object> brandMap = new HashMap<String, Object>();
			brandMap.put("brandInfoId", brandId);
			brandMap.put("brandName", userInfo.getBrandName());
			brandInfoList = new ArrayList<Map<String,Object>>();
			brandInfoList.add(brandMap);
		}
		return SUCCESS;
	}

	/**
	 * 导入BB柜台
	 * @return
	 */
	public String importBBCounter() {
		try {
			Map<String, Object> map = this.getParamsMap();
			// 上传的文件
			map.put("upExcel", upExcel);
			map.put("batchCode", form.getBatchCode());
			Map<String, Object> resultMap = binOLSTJCS11_BL.tran_import(map);
			String successCount = ConvertUtil.getString(resultMap.get("successCount"));
			this.addActionMessage(getText("STM00048", new String[]{successCount}));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			if(e instanceof CherryException){
				this.addActionError(e.getMessage());
			}else{
				this.addActionError(getText("ECM00036"));
			}
		}
		return CherryConstants.GLOBAL_ACCTION_RESULT;
	}

	/**
	 * 取得共通参数Map
	 * 
	 * @return
	 */
	private Map<String, Object> getParamsMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		// 语言
		map.put(CherryConstants.SESSION_LANGUAGE, session.get(CherryConstants.SESSION_LANGUAGE));
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		map.put("author", userInfo.getBIN_UserID());
		map.put("createdBy", userInfo.getLoginName());
		map.put("createPGM", "BINOLSTJCS11");
		map.put("updatedBy", userInfo.getLoginName());
		map.put("updatePGM", "BINOLSTJCS11");

		return map;
	}

	@Override
	public BINOLSTJCS11_Form getModel() {
		return form;
	}

	public List<Map<String, Object>> getBBCounterList() {
		return BBCounterList;
	}

	public void setBBCounterList(List<Map<String, Object>> bBCounterList) {
		BBCounterList = bBCounterList;
	}

	public Map<String, Object> getBBCounterInfo() {
		return BBCounterInfo;
	}

	public void setBBCounterInfo(Map<String, Object> bBCounterInfo) {
		BBCounterInfo = bBCounterInfo;
	}

	public File getUpExcel() {
		return upExcel;
	}

	public void setUpExcel(File upExcel) {
		this.upExcel = upExcel;
	}

	public String getUpExcelFileName() {
		return upExcelFileName;
	}

	public void setUpExcelFileName(String upExcelFileName) {
		this.upExcelFileName = upExcelFileName;
	}

	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}
}
