/*
 * @(#)BINOLCTTPL01_Action.java     1.0 2012/12/11
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
package com.cherry.ct.common.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM32_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ct.common.form.BINOLCTCOM02_Form;
import com.cherry.ct.common.interfaces.BINOLCTCOM02_IF;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 新建沟通计划Action
 * 
 * @author ZhangGS
 * @version 1.0 2012.12.11
 */
public class BINOLCTCOM02_Action extends BaseAction implements ModelDriven<BINOLCTCOM02_Form>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -18940961246737487L;
	/** 沟通设置From */
	private BINOLCTCOM02_Form form = new BINOLCTCOM02_Form();
	
	/**异常日志*/
	private static final Logger logger = LoggerFactory.getLogger(BINOLCTCOM02_Action.class);
	
	@Resource
	private BINOLCTCOM02_IF binOLCTCOM02_BL;
	/** 取号共通 */
	@Resource
	private BINOLCM03_BL binOLCM03_BL;
	
	@Resource(name = "binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	/** 沟通模块共通BL */
	@Resource
	private BINOLCM32_BL binolcm32_BL;
	/** 沟通信息内容List */
	private List<Map<String, Object>> messageContentsList;
	/** 沟通设置结果List */
	private List<Map<String, Object>> commResultList;
	/** 沟通设置初始默认值List */
	private List<Map<String, Object>> commInitList;
	/** 活动对象List */
	private List<String> campaignObjList;
	/** 活动详细信息 */
	private Map<String, Object> activityInfo;
	/** 沟通对象搜索记录名称 */
	private String recordName;
	/** 当前日期 */
	private String nowDate;
	/** 沟通对象搜索结果记录List*/
	private List<Map<String, Object>> searchRecordList;
	
	public List<Map<String, Object>> getMessageContentsList() {
		return messageContentsList;
	}

	public void setMessageContentsList(List<Map<String, Object>> messageContentsList) {
		this.messageContentsList = messageContentsList;
	}

	public List<Map<String, Object>> getCommResultList() {
		return commResultList;
	}

	public void setCommResultList(List<Map<String, Object>> commResultList) {
		this.commResultList = commResultList;
	}

	public List<Map<String, Object>> getCommInitList() {
		return commInitList;
	}

	public void setCommInitList(List<Map<String, Object>> commInitList) {
		this.commInitList = commInitList;
	}

	public List<String> getCampaignObjList() {
		return campaignObjList;
	}

	public void setCampaignObjList(List<String> campaignObjList) {
		this.campaignObjList = campaignObjList;
	}

	public String getRecordName() {
		return recordName;
	}

	public void setRecordName(String recordName) {
		this.recordName = recordName;
	}

	public String getNowDate() {
		return nowDate;
	}

	public void setNowDate(String nowDate) {
		this.nowDate = nowDate;
	}

	public List<Map<String, Object>> getSearchRecordList() {
		return searchRecordList;
	}

	public void setSearchRecordList(List<Map<String, Object>> searchRecordList) {
		this.searchRecordList = searchRecordList;
	}

	/** 页面初始化 */
	public String init() throws Exception{
		try{
			Map<String, Object> map = new HashMap<String, Object>();
			// 用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
					.getBIN_OrganizationInfoID());
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
			// 获取当前时间
			nowDate = CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN);
			// 当活动编号不为空时获取活动名称
			if(!CherryChecker.isNullOrEmpty(form.getCampaignCode(), true)){
				// 活动编号
				map.put("campaignCode", form.getCampaignCode());
				// 根据活动编号获取活动名称
				activityInfo = binolcm32_BL.getActivityInfo(map);
				if(null != activityInfo && !activityInfo.isEmpty()){
					form.setCampaignName(ConvertUtil.getString(activityInfo.get("campaignName")));
					form.setBeginDate(ConvertUtil.getString(activityInfo.get("beginDate")));
					form.setEndDate(ConvertUtil.getString(activityInfo.get("endDate")));
				}
			}
			commInitList = new ArrayList<Map<String, Object>>();
			commInitList.add(getInitCommList(map));
			
			// 当沟通设置信息不为空时获取已有的设置信息，为空时设置默认值
			if (null != form.getCommResultInfo() && !form.getCommResultInfo().isEmpty()){
				commResultList = ConvertUtil.json2List(form.getCommResultInfo());
			}else{
				commResultList = new ArrayList<Map<String, Object>>();
				commResultList.add(getInitCommList(map));
			}
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
	
	/** 判断沟通事件是否执行成功*/
	public void getSendType(){
		try {
			UserInfo userInfo = (UserInfo) session     
					.get(CherryConstants.SESSION_USERINFO);
			Map<String,Object> paramMap=new HashMap<String, Object>();
			paramMap.put("Bin_PlanCode", form.getPlanCode());
			paramMap.put("BIN_OrganizationInfoID", userInfo.getBIN_OrganizationInfoID());
			paramMap.put("BIN_BrandInfoID", userInfo.getBIN_BrandInfoID());
			paramMap.put("PhaseNum",form.getPhaseNum());
			int result=binOLCTCOM02_BL.getSendType(paramMap);
			ConvertUtil.setResponseByAjax(response, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 页面初始化时设置页面默认值
	@SuppressWarnings("unchecked")
	public Map<String, Object> getInitCommList(Map<String, Object> map){
		Map<String, Object> commmap = new HashMap<String, Object>();
		String beginDate = form.getBeginDate();
		String endDate = form.getEndDate();
		String nowDate = CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN);
		String sendDate = CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN);
		if(null==beginDate || "".equals(beginDate)){
			sendDate = nowDate;
		}else{
			if(binolcm32_BL.dateBefore(nowDate, beginDate, CherryConstants.DATE_PATTERN)){
				sendDate = beginDate;
			}else{
				sendDate = nowDate;
				if(binolcm32_BL.dateBefore(nowDate, endDate, CherryConstants.DATE_PATTERN)){
					beginDate = nowDate;
				}else{
					beginDate = "";
					endDate = "";
				}
			}
		}
		// 设置沟通名称、时间等默认值
		commmap.put("RowNumber", CherryConstants.COMMUNICATION_INIT_ROWNUMBER);
		commmap.put("communicationCode", CherryConstants.COMMUNICATION_INIT_CODE);
		commmap.put("communicationName", CherryConstants.COMMUNICATION_INIT_NAME);
		commmap.put("sendDate", sendDate);
		commmap.put("runBeginTime", "");
		commmap.put("runEndTime", "");
		commmap.put("sendBeginTime", beginDate);
		commmap.put("sendEndTime", endDate);
		commmap.put("runBegin", beginDate);
		commmap.put("runEnd", endDate);
		commmap.put("sendTime", CherryUtil.getSysDateTime("HH:mm"));
		commmap.put("dateValue", CherryConstants.COMMUNICATION_INIT_DATEVALUE);
		commmap.put("timeValue", CherryConstants.COMMUNICATION_INIT_SENDTIME);
		commmap.put("dayValue", CherryConstants.COMMUNICATION_INIT_DAYVALUE);
		commmap.put("tValue", CherryConstants.COMMUNICATION_INIT_SENDTIME);
		commmap.put("conditionValue", CherryConstants.COMMUNICATION_INIT_CONDITIONVALUE);
		commmap.put("frequencyCode", "2");
		if(!CherryChecker.isNullOrEmpty(form.getCampaignCode(), true)
				|| !CherryChecker.isNullOrEmpty(form.getCampaignID(), true))
		{
			if(!CherryChecker.isNullOrEmpty(form.getConditionUseFlag(), true) && "1".equals(form.getConditionUseFlag()))
			{
				List<Map<String, Object>> objList = new ArrayList<Map<String,Object>>();
				map.put("campaignID", form.getCampaignID());
				map.put("campaignCode", form.getCampaignCode());
				// 根据活动编号获取活动对象
				campaignObjList = binolcm32_BL.getCommObjListByCampaign(map);
				if(null != campaignObjList && !campaignObjList.isEmpty()){
					for(String campaignObj : campaignObjList){
						Map<String, Object> objmap = new HashMap<String, Object>();
						Map<String, Object> recordmap = new HashMap<String, Object>();
						map.put("searchCode", campaignObj);
						String recordCode = "";
						String recordName = "";
						String customerType = "";
						String recordType = "";
						String recordCount = "";
						String conditionInfo = "";
						String disableCondition = "";
						String comments = "";
						String fromType = "";
						String orgId = ConvertUtil.getString(map.get("organizationInfoId"));
						String brandId = ConvertUtil.getString(map.get("brandInfoId"));
						if(campaignObj.equals("ALL")){
							recordCode = binOLCM03_BL.getTicketNumber(orgId, brandId, "", "EC");
							recordName = CherryConstants.COMMUNICATION_INIT_ALL;
							customerType = CherryConstants.CUSTOMERTYPE_2;
							recordType = CherryConstants.RECORDTYPE;
							fromType = CherryConstants.FROMTYPE;
						}else if(campaignObj.equals("ALL_MEB")){
							recordCode = binOLCM03_BL.getTicketNumber(orgId, brandId, "", "EC");
							recordName = CherryConstants.COMMUNICATION_INIT_ALLMEMBER;
							customerType = CherryConstants.CUSTOMERTYPE_1;
							recordType = CherryConstants.RECORDTYPE;
							fromType = CherryConstants.FROMTYPE;
						}else{
							// 获取搜索记录详细信息
							recordmap = binolcm32_BL.getObjRecordInfo(map);
							if(null != recordmap && !recordmap.isEmpty()){
								recordCode = campaignObj;
								recordName = ConvertUtil.getString(recordmap.get("recordName"));
								customerType = ConvertUtil.getString(recordmap.get("customerType"));
								recordType = ConvertUtil.getString(recordmap.get("recordType"));
								recordCount = ConvertUtil.getString(recordmap.get("recordCount"));
								conditionInfo = ConvertUtil.getString(recordmap.get("conditionInfo"));
								disableCondition = ConvertUtil.getString(recordmap.get("conditionInfo"));
								comments = ConvertUtil.getString(recordmap.get("comments"));
								fromType = ConvertUtil.getString(recordmap.get("fromType"));
							}
						}
						// 设置搜索记录默认值
						objmap.put("recordCode", recordCode);
						objmap.put("recordName", recordName);
						objmap.put("customerType", customerType);
						objmap.put("recordType", recordType);
						objmap.put("recordCount", recordCount);
						objmap.put("conditionInfo", conditionInfo);
						objmap.put("disableCondition", disableCondition);
						objmap.put("conditionDisplay", comments);
						objmap.put("fromType", fromType);
						objList.add(objmap);
					}
					// 将沟通对象默认值加入沟通默认值map
					commmap.put("objList", objList);
				}
			}
		}
		return commmap;
	}
	
	/**
	 * 
	 * 沟通计划基本信息验证处理
	 * 
	 */
	public void validateInit() throws Exception {
		//品牌必选验证
		if(CherryChecker.isNullOrEmpty(form.getBrandInfoId())) {
			this.addFieldError("brandInfoId", getText("ECM00009",new String[]{getText("CTM00003")}));
		}
		// 沟通计划名称验证
		if(CherryChecker.isNullOrEmpty(form.getPlanName())) {
			this.addFieldError("planName", getText("ECM00009",new String[]{getText("CTM00001")}));
		} else {
			// 超过50位验证
			if(form.getPlanName().length() > 50) {
				this.addFieldError("planName", getText("ECM00020",new String[]{getText("CTM00001"),"50"}));
			}
		}
		// 超过20位验证
		if(form.getMemo() != null && form.getMemo().length() > 500) {
			this.addFieldError("memo", getText("ECM00020",new String[]{getText("CTM00002"),"500"}));
		}
	}
	
	@Override
	public BINOLCTCOM02_Form getModel() {
		// TODO Auto-generated method stub
		return form;
	}
	
	/***
	 * 查询沟通对象搜索结果记录
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String searchSearchReCord() {
		try{
			Map<String, Object> map = new HashMap<String, Object>();
			map = (Map<String, Object>) Bean2Map.toHashMap(form);
			// 用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
					.getBIN_OrganizationInfoID());
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			//将form表格参数添加到map
			ConvertUtil.setForm(form, map);
			
			// 获取是否启用数据权限配置
			String pvgFlag = binOLCM14_BL.getConfigValue("1317", ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()), ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
			if(!"".equals(ConvertUtil.getString(form.getUserId()))){
				map.put(CherryConstants.USERID, form.getUserId());
			}else{
				map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
			}
			if("1".equals(pvgFlag)){
				map.put("privilegeFlag", "1");
			}else{
				map.put("privilegeFlag", "0");
			}
			// 业务类型
			map.put("businessType", "4");
			// 操作类型
			map.put("operationType", "1");
			//移除空值
			map = CherryUtil.removeEmptyVal(map);
			int count = 0;
			count = binOLCTCOM02_BL.getSearchRecordCount(map);
			if(count > 0){
				searchRecordList = binOLCTCOM02_BL.getSearchRecordList(map);
			}
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
	
	public void getCustomerCount() throws Exception{
		try{
			// 参数Map
			Map<String, Object> map = new HashMap<String, Object>();
			UserInfo userInfo = (UserInfo) request.getSession().getAttribute(CherryConstants.SESSION_USERINFO);
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// form参数设置到map中
			ConvertUtil.setForm(form, map);
			map.put("recordCode", form.getSearchCode());
			map.put("recordType", form.getRecordType());
			map.put("customerType", form.getCustomerType());
			map.put("conditionInfo", form.getConditionInfo());
			
			// 获取是否启用数据权限配置
			String pvgFlag = binOLCM14_BL.getConfigValue("1317", ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()), ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
			if(!"".equals(ConvertUtil.getString(form.getUserId()))){
				map.put(CherryConstants.USERID, form.getUserId());
			}else{
				map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
			}
			if("1".equals(pvgFlag)){
				map.put("privilegeFlag", "1");
			}else{
				map.put("privilegeFlag", "0");
			}
			// 业务类型
			map.put("businessType", "4");
			// 操作类型
			map.put("operationType", "1");
			// 排序字段
			map.put("SORT_ID", "memId");
			// 取得客户列表
			Map<String, Object> resultListMap = binolcm32_BL.getSearchCustomerInfo(map);
			int count = 0;
			if(!CherryChecker.isNullOrEmpty(resultListMap, true) && !CherryChecker.isNullOrEmpty(resultListMap.get("total"), true)){
				count = Integer.parseInt(resultListMap.get("total").toString());
			}
			ConvertUtil.setResponseByAjax(response, count);
		} catch(Exception e){
			logger.error(e.getMessage(), e);
			// 自定义异常的场合
			if(e instanceof CherryException){
				CherryException temp = (CherryException)e;
				this.addActionError(temp.getErrMessage());
				ConvertUtil.setResponseByAjax(response, "0");
			 }else{
				//系统发生异常，请联系管理人员。
				this.addActionError(getText("ECM00036"));
				ConvertUtil.setResponseByAjax(response, "0");
			 }
		}
	}
}
