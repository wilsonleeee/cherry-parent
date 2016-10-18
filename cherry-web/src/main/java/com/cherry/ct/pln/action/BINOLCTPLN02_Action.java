/*
 * @(#)BINOLCTPLN02_Action.java     1.0 2013/07/10
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
import com.cherry.cm.cmbussiness.bl.BINOLCM32_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ct.pln.form.BINOLCTPLN02_Form;
import com.cherry.ct.pln.interfaces.BINOLCTPLN02_IF;
import com.opensymphony.xwork2.ModelDriven;
/**
 * 沟通触发事件设置Action
 * 
 * @author ZhangGS
 * @version 1.0 2013.07.10
 */
public class BINOLCTPLN02_Action extends BaseAction implements ModelDriven<BINOLCTPLN02_Form>{

	private static final long serialVersionUID = 1L;

	private BINOLCTPLN02_Form form = new BINOLCTPLN02_Form();
	
	@Resource
	private BINOLCM32_BL binolcm32_BL;
	
	@Resource
	private BINOLCTPLN02_IF binOLCTPLN02_IF;
	
	/** 沟通模板变量List */
	private List<Map<String, Object>> msgList;
	
	/** 活动List */
	private List<Map<String, Object>> activityList;
	
	public List<Map<String, Object>> getMsgList() {
		return msgList;
	}

	public void setMsgList(List<Map<String, Object>> msgList) {
		this.msgList = msgList;
	}

	public List<Map<String, Object>> getActivityList() {
		return activityList;
	}

	public void setActivityList(List<Map<String, Object>> activityList) {
		this.activityList = activityList;
	}

	/**异常日志*/
	private static final Logger logger = LoggerFactory.getLogger(BINOLCTPLN02_Action.class);
	
	@SuppressWarnings("unchecked")
	public String init() throws Exception{
		try{
			Map<String, Object> map = new HashMap<String, Object>();
			// 用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
					.getBIN_OrganizationInfoID());
			map.put(CherryConstants.BRANDINFOID, userInfo
					.getBIN_BrandInfoID());
			// 获取活动List
			activityList = binolcm32_BL.getActvityList(map);
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
	
	/**
	 * 更改触发事件
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String change() throws Exception{
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
			// 事件类型
			map.put("eventType", form.getEventType());
			// 获取活动List
			activityList = binolcm32_BL.getActvityList(map);
			// 获取事件延时设置
			Map<String, Object> delaySetMap = binOLCTPLN02_IF.getDelaySetInfo(map);
			if(delaySetMap != null && !delaySetMap.isEmpty()){
				String frequencyCode = ConvertUtil.getString(delaySetMap.get("frequencyCode"));
				if("0".equals(frequencyCode)){
					form.setFrequencyCode(frequencyCode);
					form.setSendBeginTime(CherryConstants.SMGTIMEBEGINOFDAY);
					form.setSendEndTime(CherryConstants.SMGTIMEENDOFDAY);
				}else{
					String sendBeginTime = ConvertUtil.getString(delaySetMap.get("sendBeginTime"));
					String sendEndTime = ConvertUtil.getString(delaySetMap.get("sendEndTime"));
					if(!"".equals(sendBeginTime)){
						sendBeginTime = sendBeginTime.substring(0,5);
					}
					if(!"".equals(sendEndTime)){
						sendEndTime = sendEndTime.substring(0,5);
					}
					form.setFrequencyCode(frequencyCode);
					form.setSendBeginTime(sendBeginTime);
					form.setSendEndTime(sendEndTime);
				}
			}else{
				form.setFrequencyCode("1");
				form.setSendBeginTime(CherryConstants.SMGTIMEBEGINOFDAY);
				form.setSendEndTime(CherryConstants.SMGTIMEENDOFDAY);
			}
			msgList = binOLCTPLN02_IF.getEventSetList(map);
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
	
	public void save() throws Exception{
		try{
			//参数Map
			Map<String, Object> map = new HashMap<String, Object>();
			// 用户信息
			UserInfo userInfo = (UserInfo) session     
					.get(CherryConstants.SESSION_USERINFO);
			// 品牌代码
			String brandCode = userInfo.getBrandCode();
			// 品牌ID
			int brandId = userInfo.getBIN_BrandInfoID();
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
					.getBIN_OrganizationInfoID());
			// 品牌ID
			map.put("brandInfoId", ConvertUtil.getString(brandId));
			// 品牌代码
			map.put("brandCode", brandCode);
			// 作成者
			map.put(CherryConstants.CREATEDBY, userInfo.getLoginName());
			// 更新者
			map.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());
			// 事件延时频率
			map.put("frequencyCode", form.getFrequencyCode());
			// 事件允许延时开始时间
			map.put("sendBeginTime", form.getSendBeginTime());
			// 事件允许执行截止时间
			map.put("sendEndTime", form.getSendEndTime());
			// 事件类型
			map.put("eventType", form.getEventType());
			// 设置信息
			map.put("eventSetInfo", form.getEventSetInfo());
			
			// 保存沟通事件设置
			binOLCTPLN02_IF.tran_saveEventSet(map);
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
		//处理成功
		ConvertUtil.setResponseByAjax(response, "");
	}
	
	public void stop() throws Exception{
		try{
			//参数Map
			Map<String, Object> map = new HashMap<String, Object>();
			// 用户信息
			UserInfo userInfo = (UserInfo) session     
					.get(CherryConstants.SESSION_USERINFO);
			// 品牌ID
			int brandId = userInfo.getBIN_BrandInfoID();
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
					.getBIN_OrganizationInfoID());
			// 品牌ID
			map.put("brandInfoId", ConvertUtil.getString(brandId));
			// 事件类型
			map.put("eventType", form.getEventType());
			// 更新程序
			map.put(CherryConstants.UPDATEPGM, "BINOLCTPLN02");
			// 更新者
			map.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());
			
			// 停用沟通事件设置
			binOLCTPLN02_IF.tran_stopEventSet(map);
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
		//处理成功
		ConvertUtil.setResponseByAjax(response, "");
	}
	
	@Override
	public BINOLCTPLN02_Form getModel() {
		return form;
	}

}
