/*
 * @(#)BINOLSTJCS01_BL.java     1.0 2011/08/25
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.st.jcs.form.BINOLSTJCS09_Form;
import com.cherry.st.jcs.interfaces.BINOLSTJCS09_IF;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

@SuppressWarnings("unchecked")
public class BINOLSTJCS09_Action extends BaseAction implements
		ModelDriven<BINOLSTJCS09_Form> {
			
	private static final long serialVersionUID = 1940893461762579509L;
	/** 参数From */
	private BINOLSTJCS09_Form form = new BINOLSTJCS09_Form();
	/** 接口 */
	@Resource
	private BINOLSTJCS09_IF binOLSTJCS09_BL;
	@Resource
	private BINOLCM05_BL binOLCM05_BL;

	private List<Map<String, Object>> brandInfoList;
	private List<Map<String, Object>> businessLogicDepotsList;
	private List<Map<String, Object>> logicDepotList1;
	private Map getLogicDepot;
	
	/**
	 * 
	 * 初始画面
	 * 
	 */
	public String init() throws JSONException {
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.VALID_FLAG, form.getValidFlag());

		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());

		// 所属品牌
		if (userInfo.getBIN_BrandInfoID() == -9999) {
			brandInfoList = binOLCM05_BL.getBrandInfoList(map);
		} else {
			Map<String, Object> brandMap = new HashMap<String, Object>();
			// 品牌ID
			brandMap.put("brandInfoId", userInfo.getBIN_BrandInfoID());
			// 品牌名称
			brandMap.put("brandName", userInfo.getBrandName());
			brandInfoList = new ArrayList<Map<String, Object>>();
			brandInfoList.add(brandMap);
		}

		return SUCCESS;
	}

	/**
	 * 
	 * 获取列表
	 * */
	public String search() throws Exception {
		
		//查询参数
		Map<String, Object> map = new HashMap<String, Object>();
		
		//品牌
		map.put("brandInfoId", form.getBrandInfoId());
		//逻辑仓库名称
		map.put("logicInventory", form.getInventoryName());
		//产品类型
		map.put("productType", form.getProductType());
		//业务所属
		map.put("logicType", form.getLogicType());
		//业务类型
		map.put("businessType", form.getBusinessType());
		
		// form参数设置到paramMap中
		ConvertUtil.setForm(form, map);
		// 取得总数
		int count = binOLSTJCS09_BL.searchLogicDepotCount(map);
		if (count > 0) {
			// 仓库List
			logicDepotList1 = binOLSTJCS09_BL.searchLogicDepotList(map);
			form.setLogicDepotList(logicDepotList1);
		}
		// form表单设置
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		// AJAX返回至dataTable结果页面
		return "BINOLSTJCS09_1";
	}

	/**
	 * 删除逻辑仓库逻辑关系
	 * 
	 * */

	public String delete() throws Exception {
		Map<String,Object> map = getParamsMap();
		// 品牌Code,用于WebService接口
		map.put(CherryConstants.BRAND_CODE, binOLCM05_BL.getBrandCode(Integer.parseInt(form.getBrandInfoId())));
		
		map.put("logicDepotId", form.getLogicDepotId());
		
		try {
			binOLSTJCS09_BL.tran_deleteLogicDepot(map);
		} catch(Exception e) {
			if (e instanceof CherryException) {
				CherryException temp = (CherryException) e;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}else{
				this.addActionError(e.getMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}
		}
		
		this.addActionMessage(getText("ICM00002"));
		return CherryConstants.GLOBAL_ACCTION_RESULT;
	}
	
	/**
	 * ajax取得逻辑仓库
	 * 
	 * */
	public void getLogiDepotByAjax() throws Exception{
		try{
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
			Map<String,Object> map = new HashMap<String,Object>();
			
			//品牌
			map.put("BIN_BrandInfoID", form.getBrandInfoId());
			//语言
			map.put("language", userInfo.getLanguage());
			//业务类型
			map.put("BusinessType", form.getBusinessType());
			//子类型
			map.put("SubType", form.getSubType());
			//逻辑仓库类型
//			map.put("Type", form.getType());
			// 业务所属确定逻辑仓库类型
			map.put("Type", form.getLogicType());
			//产品类型
			map.put("ProductType", form.getProductType());
			//编辑区分
			map.put("editFlag", form.getEditFlag());
			//被编辑的SubType
			map.put("editedSubType", form.getEditedSubType());
			//被编辑的ProductType
			map.put("editedProductType", form.getEditedProductType());
			
			//调用接口取得逻辑仓库
			List<Map<String,Object>> resultList = binOLSTJCS09_BL.getLogicDepotByPraMap(map);
			
			ConvertUtil.setResponseByAjax(response, resultList);
			
		}catch(Exception e){
			
			ConvertUtil.setResponseByAjax(response, new ArrayList<Map<String,Object>>());
			
		}
	}
	
	
	/**
	 * 新增初始化
	 */
	public String addInit() throws Exception {
		
		return "BINOLSTJCS09_3";
	}
	/**
	 * 新增逻辑仓库逻辑关系
	 * 
	 * */
	public String add() throws Exception {
		try {
			Map<String, Object> map = getParamsMap();
			// 品牌Code,用于WebService接口
			map.put(CherryConstants.BRAND_CODE, binOLCM05_BL.getBrandCode(Integer.parseInt(form.getBrandInfoId())));
			
			// 业务类型
			map.put("businessType", form.getBusinessType());
			// 产品类型
			map.put("productType", form.getProductType());
			// 逻辑仓库
			map.put("logicInvId", form.getLogicInvId());
			// 入出库区分
			map.put("inOutFlag", form.getInOutFlag());
			//子类型
			map.put("subType", form.getSubType());
			// 优先级
			map.put("configOrder", form.getConfigOrder());
			//逻辑仓库类型
			map.put("type", form.getType());
			// 业务所属
			map.put("logicType", form.getLogicType());
			// 备注
			map.put("comments", form.getComments().trim());
			
			binOLSTJCS09_BL.tran_addLogicDepot(map);
		} catch (Exception e) {
			if (e instanceof CherryException) {
				CherryException temp = (CherryException) e;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}else{
				this.addActionError(e.getMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}
		}
		return SUCCESS;
	}
	/**
	 * 编辑初始化
	 */
	public String editInit() throws Exception {
		// 取得要编辑的记录ID
		int logicDepotId = form.getLogicDepotId();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("logicDepotId", logicDepotId);
		// 根据记录ID查询逻辑仓库业务配置INFO
		getLogicDepot = binOLSTJCS09_BL.getLogicDepots(map);
		// 语言类型
		String language = (String) session
				.get(CherryConstants.SESSION_LANGUAGE);
		String brandInfoID = form.getBrandInfoId();
		
		Map<String,Object> pram =  new HashMap<String,Object>();
        pram.put("BIN_BrandInfoID", brandInfoID);
        pram.put("Type", getLogicDepot.get("logicType"));
        pram.put("language", language);
        
		businessLogicDepotsList = binOLSTJCS09_BL.getLogicDepotByPraMap(pram);	
		
		return "BINOLSTJCS09_4";
	}

	/**
	 * 编辑逻辑仓库业务关系
	 */
	public String edit() throws Exception {
		try {
			Map<String, Object> map = getParamsMap();
			// 品牌Code,用于WebService接口
			map.put(CherryConstants.BRAND_CODE, binOLCM05_BL.getBrandCode(Integer.parseInt(form.getBrandInfoId())));
			// 业务类型
			map.put("businessType", form.getBusinessType());
			// 产品类型
			map.put("productType", form.getProductType());
			// 逻辑仓库
			map.put("logicInvId", form.getLogicInvId());
			// 入出库区分
			map.put("inOutFlag", form.getInOutFlag());
			// 优先级
			map.put("configOrder", form.getConfigOrder());
			// 备注
			map.put("comments", form.getComments().trim());
			//逻辑仓库类型
			map.put("type", form.getType());
			//逻辑仓库类型
			map.put("logicType", form.getLogicType());
			//子类型
			map.put("subType", form.getSubType());
			map.put("logicDepotId", form.getLogicDepotId());
			// 编辑前的数据更新时间
			map.put("updateTimeOld", form.getUpdateTime());
			// 编辑前的数据更新次数
			map.put("modifyCount", form.getModifyCount());
			
			binOLSTJCS09_BL.tran_EditInfosave(map);
		} catch (Exception e) {
			if (e instanceof CherryException) {
				CherryException temp = (CherryException) e;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}else{
				this.addActionError(e.getMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT;
			}
		}
		return SUCCESS;
	}
	
	/**
	 * 取得共通参数Map
	 * 
	 * @return
	 */
	private Map<String, Object> getParamsMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 用户信息
		map.put(CherryConstants.SESSION_USERINFO, userInfo);
		// 语言
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		
		map.put("createdBy", userInfo.getLoginName());
		map.put("createPGM", "BINOLSTJCS09");
		map.put("updatedBy",  userInfo.getLoginName());
		map.put("updatePGM", "BINOLSTJCS09");
		
		return map;
	}
	
	public List<Map<String, Object>> getLogicDepotList1() {
		return logicDepotList1;
	}

	public void setLogicDepotList1(List<Map<String, Object>> logicDepotList) {
		this.logicDepotList1 = logicDepotList;
	}

	@Override
	public BINOLSTJCS09_Form getModel() {
		return form;
	}
	
	public Map getGetLogicDepot() {
		return getLogicDepot;
	}
	
	public void setGetLogicDepot(Map getLogicDepot) {
		this.getLogicDepot = getLogicDepot;
	}
	
	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}

	public List<Map<String, Object>> getBusinessLogicDepotsList() {
		return businessLogicDepotsList;
	}

	public void setBusinessLogicDepotsList(
			List<Map<String, Object>> businessLogicDepotsList) {
		this.businessLogicDepotsList = businessLogicDepotsList;
	}
}
