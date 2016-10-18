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
import com.cherry.cm.cmbussiness.bl.BINOLCM32_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ct.common.form.BINOLCTCOM01_Form;
import com.cherry.ct.common.interfaces.BINOLCTCOM01_IF;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 新建沟通计划Action
 * 
 * @author ZhangGS
 * @version 1.0 2012.12.11
 */
public class BINOLCTCOM01_Action extends BaseAction implements ModelDriven<BINOLCTCOM01_Form> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5106077797596746195L;
	/** 新建沟通计划From */
	private BINOLCTCOM01_Form form = new BINOLCTCOM01_Form();
	
	/**异常日志*/
	private static final Logger logger = LoggerFactory.getLogger(BINOLCTCOM01_Action.class);
	
	@Resource
	private BINOLCM05_BL binolcm05_BL;
	
	@Resource
	private BINOLCM32_BL binolcm32_BL;
	
	/** 共通BL */
	@Resource(name="binOLCM00_BL")
	private BINOLCM00_BL binolcm00BL;
	
	/** 系统配置项 共通BL */
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource
	private BINOLCTCOM01_IF binolctcom01_IF;
	
	/** 品牌List */
	private List<Map<String, Object>> brandList;
	
	/** 活动List */
	private List<Map<String, Object>> activityList;
	
	/** 渠道List */
	private List<Map<String, Object>> channelList;
	
	/** 活动详细信息 */
	private Map<String, Object> activityInfo;
	
	/** 是否显示渠道和柜台属性 */
	private String showChannel;
	
	/** 是否启用数据权限 */
	private String privilegeFlag;
	
	
	public List<Map<String, Object>> getBrandList() {
		return brandList;
	}

	public void setBrandList(List<Map<String, Object>> brandList) {
		this.brandList = brandList;
	}

	public List<Map<String, Object>> getActivityList() {
		return activityList;
	}

	public void setActivityList(List<Map<String, Object>> activityList) {
		this.activityList = activityList;
	}
	
	public List<Map<String, Object>> getChannelList() {
		return channelList;
	}

	public void setChannelList(List<Map<String, Object>> channelList) {
		this.channelList = channelList;
	}

	public String getShowChannel() {
		return showChannel;
	}

	public void setShowChannel(String showChannel) {
		this.showChannel = showChannel;
	}

	public String getPrivilegeFlag() {
		return privilegeFlag;
	}

	public void setPrivilegeFlag(String privilegeFlag) {
		this.privilegeFlag = privilegeFlag;
	}

	/** 页面初始化 */
	public String init() throws Exception{
		try{
			setAttributeValue();
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
	
	// 活动信息更改时的处理
	public String changeActivity() throws Exception{
		try{
			setAttributeValue();
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
	public void setAttributeValue() throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> planMap = new HashMap<String, Object>();
		
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
			String brandName = userInfo.getBrandName();
			form.setBrandName(brandName);
			form.setBrandInfoId(ConvertUtil.getString(brandId));
		}
		map.put(CherryConstants.BRANDINFOID, brandId);
		// 获取活动List
		activityList = binolcm32_BL.getActvityList(map);
		// 取得渠道List
		channelList = binolcm00BL.getChannelList(map);
		// 是否显示渠道和柜台属性
		showChannel = binOLCM14_BL.getConfigValue("1309",ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()),ConvertUtil.getString(brandId));
		// 获取是否启用数据权限配置
		privilegeFlag = binOLCM14_BL.getConfigValue("1317", ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID()), ConvertUtil.getString(userInfo.getBIN_BrandInfoID()));
		
		if(!CherryChecker.isNullOrEmpty(form.getPlanCode(), true)){
			map.put("planCode", form.getPlanCode());
			// 获取沟通计划
			planMap = binolctcom01_IF.getPlanInfo(map);
			// 设置沟通计划属性
			setPlaninfo(userInfo, planMap);
			// 获取沟通活动名称
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
		}else{
			// 当活动编号不为空时获取活动名称
			if(!CherryChecker.isNullOrEmpty(form.getCampaignCode(), true)){
				// 活动编号
				map.put("campaignCode", form.getCampaignCode());
				// 根据活动编号获取活动名称
				activityInfo = binolcm32_BL.getActivityInfo(map);
				if(null != activityInfo && !activityInfo.isEmpty()){
					form.setCampaignName(ConvertUtil.getString(activityInfo.get("campaignName")));
					form.setPlanName(ConvertUtil.getString(activityInfo.get("campaignName"))+CherryConstants.PLANNAMEFIX);
					form.setMemo(ConvertUtil.getString(activityInfo.get("campaignName")));
					form.setBeginDate(ConvertUtil.getString(activityInfo.get("beginDate")));
					form.setEndDate(ConvertUtil.getString(activityInfo.get("endDate")));
					form.setConditionUseFlag("1");
				}
				// 获取活动对应的沟通计划
				planMap = binolcm32_BL.getPlanInfoByCampaign(map);
				// 设置沟通计划属性
				setPlaninfo(userInfo, planMap);
			}
		}
	}
	
	// 获取已有的沟通计划属性
	private void setPlaninfo(UserInfo userInfo, Map<String, Object> planMap){
		if(null != planMap && !planMap.isEmpty()){
			// 若已存在沟通计划设置信息则将已有信息设置到对应属性中
			if(!CherryChecker.isNullOrEmpty(planMap.get("planCode"), true)){
				if(CherryChecker.isNullOrEmpty(form.getUserId(), true))
				{
					form.setUserId(ConvertUtil.getString(planMap.get("userId")));
				}
				if(CherryChecker.isNullOrEmpty(form.getPlanCode(), true))
				{
					form.setPlanCode(ConvertUtil.getString(planMap.get("planCode")));
				}
				if(CherryChecker.isNullOrEmpty(form.getPlanName(), true))
				{
					form.setPlanName(ConvertUtil.getString(planMap.get("planName")));
				}
				if(CherryChecker.isNullOrEmpty(form.getChannelId(), true))
				{
					form.setChannelId(ConvertUtil.getString(planMap.get("channelId")));
				}
				if(CherryChecker.isNullOrEmpty(form.getChannelName(), true))
				{
					form.setChannelName(ConvertUtil.getString(planMap.get("channelName")));
				}
				if(CherryChecker.isNullOrEmpty(form.getCounterCode(), true))
				{
					form.setCounterCode(ConvertUtil.getString(planMap.get("counterCode")));
				}
				if(CherryChecker.isNullOrEmpty(form.getCounterName(), true))
				{
					form.setCounterName(ConvertUtil.getString(planMap.get("counterName")));
				}
				if(CherryChecker.isNullOrEmpty(form.getCommResultInfo(), true))
				{
					form.setCommResultInfo(ConvertUtil.getString(planMap.get("planCondition")));
				}
				if(CherryChecker.isNullOrEmpty(form.getMemo(), true))
				{
					form.setMemo(ConvertUtil.getString(planMap.get("memo")));
				}
			}
			if(!CherryChecker.isNullOrEmpty(form.getEditType(), true) && "1".equals(form.getEditType())){
				//复制模式清除并重新生成计划code与计划名称
				form.setPlanCode(null);
				form.setPlanName(null);
				form.setUserId(ConvertUtil.getString(userInfo.getBIN_UserID()));
				if(CherryChecker.isNullOrEmpty(form.getChannelId(), true))
				{
					form.setChannelId(ConvertUtil.getString(planMap.get("channelId")));
				}
				if(CherryChecker.isNullOrEmpty(form.getChannelName(), true))
				{
					form.setChannelName(ConvertUtil.getString(planMap.get("channelName")));
				}
				if(CherryChecker.isNullOrEmpty(form.getCounterCode(), true))
				{
					form.setCounterCode(ConvertUtil.getString(planMap.get("counterCode")));
				}
				if(CherryChecker.isNullOrEmpty(form.getCounterName(), true))
				{
					form.setCounterName(ConvertUtil.getString(planMap.get("counterName")));
				}
			}
		}
	}

	@Override
	public BINOLCTCOM01_Form getModel() {
		// TODO Auto-generated method stub
		return form;
	}
}
