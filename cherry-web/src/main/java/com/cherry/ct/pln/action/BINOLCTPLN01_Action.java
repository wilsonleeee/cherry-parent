/*
 * @(#)BINOLCTPLN01_Action.java     1.0 2012/11/06
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

package com.cherry.ct.pln.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ct.pln.bl.BINOLCTPLN01_BL;
import com.cherry.ct.pln.form.BINOLCTPLN01_Form;
import com.cherry.ct.pln.interfaces.BINOLCTPLN01_IF;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 沟通模板一览Action
 * 
 * @author ZhangGS
 * @version 1.0 2012.12.06
 */
public class BINOLCTPLN01_Action extends BaseAction implements ModelDriven<BINOLCTPLN01_Form>{
	
	private static final long serialVersionUID = 6149376457170262890L;
	/** 沟通模板一览Form */
	private BINOLCTPLN01_Form form = new BINOLCTPLN01_Form();
	
	/**异常日志*/
	private static final Logger logger = LoggerFactory.getLogger(BINOLCTPLN01_Action.class);
	
	@Resource
	private BINOLCM05_BL binolcm05_BL;
	
	/** 共通BL */
	@Resource(name="binOLCM00_BL")
	private BINOLCM00_BL binolcm00BL;
	
	@Resource(name = "binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource
	private BINOLCTPLN01_IF binolctpln01_IF;
	
	@Resource
	private BINOLCTPLN01_BL binOLCTPLN01_BL;
	
	/** 品牌List */
	private List<Map<String, Object>> brandList;
	
	/** 渠道List */
	private List<Map<String, Object>> channelList;
	
	private List<Map<String, Object>> communicationPlanList;
	
	public List<Map<String, Object>> getBrandList() {
		return brandList;
	}

	public void setBrandList(List<Map<String, Object>> brandList) {
		this.brandList = brandList;
	}

	public List<Map<String, Object>> getChannelList() {
		return channelList;
	}

	public void setChannelList(List<Map<String, Object>> channelList) {
		this.channelList = channelList;
	}

	public List<Map<String, Object>> getCommunicationPlanList() {
		return communicationPlanList;
	}

	public void setCommunicationPlanList(
			List<Map<String, Object>> communicationPlanList) {
		this.communicationPlanList = communicationPlanList;
	}

	public String init() throws Exception{
		try{
			getConditionListValue();
		} catch(Exception e){
			logger.error(e.getMessage(), e);
			// 自定义异常的场合
			if(e instanceof CherryException){
				CherryException temp = (CherryException)e;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT_PAGE;
			 }else{
				//系统发生异常，请联系管理人员。
				this.addActionError(getText("ECM00036"));
				return CherryConstants.GLOBAL_ACCTION_RESULT_PAGE;
			 }
		}
		return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	public void getConditionListValue(){
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
			brandList = binolcm05_BL.getBrandInfoList(map);
		}else {
			form.setBrandInfoId(ConvertUtil.getString(brandId));
		}
		// 取得渠道List
		channelList = binolcm00BL.getChannelList(map);
	}
	
	@SuppressWarnings("unchecked")
	public String search() throws Exception{
		try{
			Map<String, Object> map = getSearchMap();
			//取得模板数量
			int count = binolctpln01_IF.getCommunicationPlanCount(map);
			if(count > 0){
				List<Map<String, Object>> tplList = binolctpln01_IF.getCommunicationPlanList(map);
				// 取得List
				this.setCommunicationPlanList(tplList);
			}
			// form表单设置
			form.setITotalDisplayRecords(count);
			form.setITotalRecords(count);
		} catch(Exception e){
			logger.error(e.getMessage(), e);
			// 自定义异常的场合
			if(e instanceof CherryException){
				CherryException temp = (CherryException)e;
				this.addActionError(temp.getErrMessage());
				return CherryConstants.GLOBAL_ACCTION_RESULT_PAGE;
			 }else{
				//系统发生异常，请联系管理人员。
				this.addActionError(getText("ECM00036"));
				return CherryConstants.GLOBAL_ACCTION_RESULT_PAGE;
			 }
		}
		return SUCCESS;
	}
	
	private Map<String, Object> getSearchMap() {
		//参数Map
		Map<String, Object> map = new HashMap<String, Object>();
		// form参数设置到map中
		ConvertUtil.setForm(form, map);
		// 用户信息
		UserInfo userInfo = (UserInfo) session     
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 所属品牌不存在的场合
		if(form.getBrandInfoId() == null || "".equals(form.getBrandInfoId())) {
			// 不是总部的场合
			if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			// 所属品牌
				map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			}
		} else {
				map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		}
		// 获取是否启用数据权限配置
		String pvgFlag = binOLCM14_BL.getConfigValue("1317", ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()), ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
					
		// 用户ID
		map.put(CherryConstants.USERID, userInfo
				.getBIN_UserID());
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		
		// 沟通计划名称
		map.put("planName", form.getPlanName());
		// 关联活动名称
		map.put("campaignName", form.getCampaignName());
		// 沟通计划创建时间From
		map.put("fromDate", form.getFromDate());
		// 沟通计划创建时间To
		map.put("toDate", form.getToDate());
		// 渠道
		map.put("channelId", form.getChannelId());
		// 柜台
		map.put("counterCode", form.getCounterCode());
		// 沟通计划运行状态
		map.put("status", form.getStatus());
		// 沟通计划当前状态
		map.put("validFlag", form.getValidFlag());
		
		if("1".equals(pvgFlag)){
			map.put("privilegeFlag", "1");
		}else{
			map.put("privilegeFlag", "0");
		}
		// 业务类型
		map.put("businessType", "4");
		// 操作类型
		map.put("operationType", "1");
		
		return map;
	}
	
	/**
	 * 停用沟通计划
	 * @throws Exception
	 */
	public  void stop() throws Exception{
		try{
			//参数Map
			Map<String, Object> map = new HashMap<String, Object>();
			// 用户信息
			UserInfo userInfo = (UserInfo) session     
					.get(CherryConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
					.getBIN_OrganizationInfoID());
			// 所属品牌不存在的场合
			if(form.getBrandInfoId() == null || "".equals(form.getBrandInfoId())) {
				// 不是总部的场合
				if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
				// 所属品牌
					map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
				}
			} else {
				map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
			}
			// 语言类型
			map.put(CherryConstants.SESSION_LANGUAGE, session
					.get(CherryConstants.SESSION_LANGUAGE));
			// 活动Id
			map.put("planCode", form.getPlanCode());
			// 任务类型
			map.put("taskType", CherryConstants.TASK_TYPE_VALUE);
			// 更新程序
			map.put(CherryConstants.UPDATEPGM, "BINOLCTPLN01");
			// 更新者
			map.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());
						
			binOLCTPLN01_BL.tran_stopCommunicationPlan(map);
		} catch(Exception e){
			logger.error(e.getMessage(), e);
			// 自定义异常的场合
			if(e instanceof CherryException){
				CherryException temp = (CherryException)e;
				this.addActionError(temp.getErrMessage());
				ConvertUtil.setResponseByAjax(response, "");
			 }else{
				//系统发生异常，请联系管理人员。
				this.addActionError(getText("ECM00036"));
				ConvertUtil.setResponseByAjax(response, "");
			 }
		}
		ConvertUtil.setResponseByAjax(response, "");
	}
	
	@Override
	public BINOLCTPLN01_Form getModel() {
		return form;
	}
}