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
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM15_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM32_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ct.common.form.BINOLCTCOM03_Form;
import com.cherry.ct.common.interfaces.BINOLCTCOM03_IF;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 沟通计划预览与确认Action
 * 
 * @author ZhangGS
 * @version 1.0 2012.12.11
 */
public class BINOLCTCOM03_Action extends BaseAction implements ModelDriven<BINOLCTCOM03_Form>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7107406552515903055L;
	private BINOLCTCOM03_Form form = new BINOLCTCOM03_Form();
	
	/**异常日志*/
	private static final Logger logger = LoggerFactory.getLogger(BINOLCTCOM03_Action.class);
	
	@Resource
	private BINOLCM05_BL binOLCM05_BL;
	
	/** 沟通模块共通BL */
	@Resource
	private BINOLCM32_BL binolcm32_BL;
	
	@Resource
	private BINOLCTCOM03_IF binOLCTCOM03_IF;
	
	/** 各类编号取号共通BL */
	@Resource
	private BINOLCM15_BL binOLCM15_BL;
	
	/** 系统配置项 共通BL */
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	
	/** 品牌名称 */
	private String brandName;
	/** 创建时间 */
	private String createTime;
	/** 是否提交审核 */
	private boolean verified;
	/** 是否显示渠道和柜台属性 */
	private String showChannel;
	/** 沟通计划设置提示符合条件人数 */
	private String overSetNumber;
	/** 是否启用数据权限 */
	private String privilegeFlag;
	
	private List<Map<String, Object>> commResultList;
	
	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public List<Map<String, Object>> getCommResultList() {
		return commResultList;
	}

	public void setCommResultList(List<Map<String, Object>> commResultList) {
		this.commResultList = commResultList;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	public String getShowChannel() {
		return showChannel;
	}

	public void setShowChannel(String showChannel) {
		this.showChannel = showChannel;
	}

	public String getOverSetNumber() {
		return overSetNumber;
	}

	public void setOverSetNumber(String overSetNumber) {
		this.overSetNumber = overSetNumber;
	}

	public String getPrivilegeFlag() {
		return privilegeFlag;
	}

	public void setPrivilegeFlag(String privilegeFlag) {
		this.privilegeFlag = privilegeFlag;
	}

	public String init() throws Exception{
		try{
			Map<String, Object> map = new HashMap<String, Object>();
			// 用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 品牌代码
			String brandCode = userInfo.getBrandCode();
			// 品牌ID
			String brandInfoId = ConvertUtil.getString(form.getBrandInfoId());
			// 组织ID
			String organizationInfoId = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
			// 语言类型
			map.put(CherryConstants.SESSION_LANGUAGE, session
					.get(CherryConstants.SESSION_LANGUAGE));
			// 品牌ID
			map.put("brandInfoId", brandInfoId);
			// 品牌代号
			map.put("brandCode", brandCode);
			// 取得品牌名称
			brandName = binOLCM05_BL.getBrandName(map);
			// 取得系统时间
			createTime = CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN_24_HOURS);
			// 取得沟通设置结果
			commResultList = ConvertUtil.json2List(form.getCommResultInfo());
			// 获取审核配置
			verified = binOLCM14_BL.isConfigOpen("1067",organizationInfoId,brandInfoId);
			// 是否显示渠道和柜台属性
			showChannel = binOLCM14_BL.getConfigValue("1309",organizationInfoId,brandInfoId);
			// 沟通计划设置符合条件人数超出显示提示信息垡值
			overSetNumber = binOLCM14_BL.getConfigValue("1310",organizationInfoId,brandInfoId);
			// 获取是否启用数据权限配置
			privilegeFlag = binOLCM14_BL.getConfigValue("1317", organizationInfoId,brandInfoId);
			
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
	
	public String viewInit() throws Exception{
		try{
			Map<String, Object> planInfoMap = new HashMap<String, Object>();
			Map<String, Object> CampaignMap = new HashMap<String, Object>();
			Map<String, Object> map = new HashMap<String, Object>();
			// 用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 品牌代码
			String brandCode = userInfo.getBrandCode();
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
					.getBIN_OrganizationInfoID());
			// 所属品牌
			map.put("brandCode", brandCode);
			// 沟通计划编号
			map.put("planCode", form.getPlanCode());
			// 获取沟通计划详细信息
			planInfoMap = binOLCTCOM03_IF.getPlanInfoByCode(map);
			
			if(null != planInfoMap && !planInfoMap.isEmpty()){
				// 取得品牌ID作为参数
				map.put("brandInfoId",ConvertUtil.getString(planInfoMap.get("brandInfoId")));
				// 取得品牌名称
				brandName = binOLCM05_BL.getBrandName(map);
				// 取得沟通计划创建时间
				createTime = (ConvertUtil.getString(planInfoMap.get("createTime")));
				// 取得用户ID
				form.setUserId(ConvertUtil.getString(planInfoMap.get("userId")));
				// 取得沟通计划名称
				form.setPlanName(ConvertUtil.getString(planInfoMap.get("planName")));
				// 取得渠道名称
				form.setChannelName(ConvertUtil.getString(planInfoMap.get("channelName")));
				// 取得柜台名称
				form.setCounterName(ConvertUtil.getString(planInfoMap.get("counterName")));
				
				String campaignCode = ConvertUtil.getString(planInfoMap.get("campaignCode"));
				if(!"".equals(campaignCode)){
					// 取得沟通计划关联的活动编号作为参数
					map.put("campaignCode",campaignCode);
					// 根据活动编号取得活动名称
					CampaignMap = binolcm32_BL.getActivityInfo(map);
					if(null != CampaignMap && !CampaignMap.isEmpty()){
						// 设置活动名称属性
						form.setCampaignName(ConvertUtil.getString(CampaignMap.get("campaignName")));
					}
				}
				// 取得沟通设置信息
				commResultList = ConvertUtil.json2List(ConvertUtil.getString(planInfoMap.get("planCondition")));
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
	
	public String save() throws Exception{
		try{
			//参数Map
			Map<String, Object> map = new HashMap<String, Object>();
			String type = "INSERT";
			// 用户信息
			UserInfo userInfo = (UserInfo) session     
					.get(CherryConstants.SESSION_USERINFO);
			// 品牌代码
			String brandCode = userInfo.getBrandCode();
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
					.getBIN_OrganizationInfoID());
			// 部门ID
			map.put(CherryConstants.ORGANIZATIONID, userInfo
					.getBIN_OrganizationID());
			// 用户ID
			map.put(CherryConstants.USERID, userInfo
					.getBIN_UserID());
			// 品牌ID
			map.put("brandInfoId", form.getBrandInfoId());
			// 品牌代码
			map.put("brandCode", brandCode);
			// 作成者
			map.put(CherryConstants.CREATEDBY, userInfo.getLoginName());
			// 更新者
			map.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());
			// 沟通计划编号
			if(!CherryChecker.isNullOrEmpty(form.getPlanCode(), true)){
				map.put("planCode", form.getPlanCode());
				type = "UPDATE";
			}else{
				String planCode = binOLCM15_BL.getSequenceId(userInfo
						.getBIN_OrganizationInfoID(), ConvertUtil.getInt(form.getBrandInfoId()), "8");
				map.put("planCode", planCode);
				type = "INSERT";
			}
			// 沟通计划名称
			map.put("planName", form.getPlanName());
			// 渠道ID
			map.put("channelId", form.getChannelId());
			// 柜台号
			map.put("counterCode", form.getCounterCode());
			// 沟通计划设置条件
			map.put("planCondition", form.getCommResultInfo());
			// 活动编号
			map.put("campaignCode", form.getCampaignCode());
			// 活动备注
			map.put("memo", form.getMemo());
			// 审核状态
			map.put("verifiedFlag", "0");
			// 沟通计划状态
			map.put("status", "1");
			// 获取沟通设置结果Json值
			commResultList = ConvertUtil.json2List(form.getCommResultInfo());
		
			// 保存沟通设置
			binOLCTCOM03_IF.tran_saveCommInfo(map, commResultList, type);
			
			//处理成功
			this.addActionMessage(getText("ICM00002"));
			return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
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
	}

	@SuppressWarnings("unchecked")
	public void validateInit() throws Exception {
		// 用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		int i = 0,j = 0,k = 0;
		// 获取沟通计划编号
		String planCode = ConvertUtil.getString(form.getPlanCode());
		// 获取沟通设置结果Json值
		commResultList = ConvertUtil.json2List(form.getCommResultInfo());
		if(null != commResultList && !commResultList.isEmpty()){
			for(Map<String,Object> commMap : commResultList){
				// 定义 boolean 变量用于判断每次沟通的设置是否存在通不过验证的情况
				boolean errorflag = false;
				// 定义沟通对象List
				List<Map<String, Object>> objMapList = new ArrayList<Map<String,Object>>();
				// 获取沟通编号
				String phaseNum = ConvertUtil.getString(commMap.get("communicationCode"));
				// 获取沟通时间类型
				String timeType = ConvertUtil.getString(commMap.get("timeType"));
				// 获取循环执行频率
				String frequency = ConvertUtil.getString(commMap.get("frequencyCode"));
				// 获取循环执行日期定义
				String forAttribute = ConvertUtil.getString(commMap.get("forAttribute"));
				// 沟通名称非空验证
				if(CherryChecker.isNullOrEmpty(commMap.get("communicationName"))) {
					errorflag = true;
					this.addFieldError("communicationName_"+i, getText("ECM00009",new String[]{getText("CTM00005")}));
				}else{
					// 沟通名称长度范围验证
					if(ConvertUtil.getString(commMap.get("communicationName")).length() > 50){
						errorflag = true;
						this.addFieldError("communicationName_"+i, getText("ECM00020",new String[]{getText("CTM00005"),"50"}));
					}
				}
				// 判断沟通时间类型
				if(timeType.equals("1")){
					// 指定时间发送信息时发送日期非空验证
					if(CherryChecker.isNullOrEmpty(commMap.get("sendDate"))) {
						errorflag = true;
						this.addFieldError("sendDate_"+i, getText("ECM00009",new String[]{getText("CTM00006")}));
					}else{
						// 指定时间发送信息时发送日期合法验证
						if(!CherryChecker.checkDate(ConvertUtil.getString(commMap.get("sendDate")))){
							errorflag = true;
							this.addFieldError("sendDate_"+i, getText("ECM00022",new String[]{getText("CTM00006")}));
						}
					}
					// 指定时间发送信息时发送时间点非空验证
					if(CherryChecker.isNullOrEmpty(commMap.get("sendTime"))) {
						errorflag = true;
						this.addFieldError("sendTime_"+i, getText("ECM00009",new String[]{getText("CTM00007")}));
					}else{
						// 指定时间发送信息时发送时间点合法验证
						if(!CherryChecker.checkTime(ConvertUtil.getString(commMap.get("sendTime"))+":00")){
							errorflag = true;
							this.addFieldError("sendTime_"+i, getText("ECM00026",new String[]{getText("CTM00007")}));
						}else{
							Map<String, Object> paramMap = new HashMap<String, Object>();
							paramMap.put("brandInfoId", CherryUtil.obj2int(form.getBrandInfoId()));
							paramMap.put("organizationInfoId", userInfo.getBIN_OrganizationInfoID());
							paramMap.put("taskType", CherryConstants.TASK_TYPE_VALUE);
							paramMap.put("planCode", planCode);
							paramMap.put("phaseNum", phaseNum);
							String commCode = binolcm32_BL.getCommunicationByPhaseNum(paramMap);
							if (null==commCode || "".equals(commCode)){
								if(!checkSendTime(ConvertUtil.getString(commMap.get("sendDate")),ConvertUtil.getString(commMap.get("sendTime"))+":00")){
									errorflag = true;
									this.addFieldError("sendTime_"+i, getText("ECM00070",new String[]{getText("CTM00009"),getText("CTM00016")}));
								}
							}
						}
					}
				}else if(timeType.equals("2")){
					// 参考时间发送信息时提前值非空验证
					if(CherryChecker.isNullOrEmpty(commMap.get("dateValue"))) {
						errorflag = true;
						this.addFieldError("dateValue_"+i, getText("ECM00009",new String[]{getText("CTM00008")}));
					}else{
						// 参考时间发送信息时提前值合法验证
						if(!CherryChecker.isPositiveAndNegative(ConvertUtil.getString(commMap.get("dateValue")))){
							errorflag = true;
							this.addFieldError("dateValue_"+i, getText("ECM00030",new String[]{getText("CTM00008")}));
						}else if(ConvertUtil.getInt(commMap.get("dateValue"))<0){
							errorflag = true;
							this.addFieldError("dateValue_"+i, getText("ECM00033",new String[]{getText("CTM00008"),"0"}));
						}else if(ConvertUtil.getInt(commMap.get("dateValue"))>365){
							errorflag = true;
							this.addFieldError("dateValue_"+i, getText("ECM00052",new String[]{getText("CTM00008"),"365"}));
						}else if ( 13 == ConvertUtil.getInt(commMap.get("referTypeCode"))){
							if(ConvertUtil.getInt(commMap.get("dateValue")) > 365){
								errorflag = true;
								this.addFieldError("dateValue_"+i, getText("ECM00052",new String[]{getText("CTM00008"),"365"}));
							}
						}
					}
					// 参考时间发送信息时发送时间点非空验证
					if(CherryChecker.isNullOrEmpty(commMap.get("timeValue"))) {
						errorflag = true;
						this.addFieldError("timeValue_"+i, getText("ECM00009",new String[]{getText("CTM00009")}));
					}else{
						// 参考时间发送信息时发送时间点合法验证
						if(!CherryChecker.checkTime(ConvertUtil.getString(commMap.get("timeValue"))+":00")){
							errorflag = true;
							this.addFieldError("timeValue_"+i, getText("ECM00026",new String[]{getText("CTM00009")}));
						}
					}
				}else if(timeType.equals("3")){
					// 循环发送信息时发送日期非空验证
					if(CherryChecker.isNullOrEmpty(commMap.get("dayValue"))) {
						errorflag = true;
						this.addFieldError("dayValue_"+i, getText("ECM00009",new String[]{getText("CTM00010")}));
					}else{
						// 循环发送信息时发送日期合法验证
						if(frequency.equals("3")){
							if(forAttribute.equals("A") || forAttribute.equals("E")){
								if(!checkMonthDay(ConvertUtil.getString(commMap.get("dayValue")))){
									errorflag = true;
									this.addFieldError("dayValue_"+i, getText("ECM00008",new String[]{getText("CTM00010")}));
								}
							}else{
								if(!checkDayNum(ConvertUtil.getString(commMap.get("dayValue")))){
									errorflag = true;
									this.addFieldError("dayValue_"+i, getText("CTM00014"));
								}
							}
						}else if(frequency.equals("2")){
							if(!checkDay(ConvertUtil.getString(commMap.get("dayValue")))){
								errorflag = true;
								this.addFieldError("dayValue_"+i, getText("ECM00008",new String[]{getText("CTM00010")}));
							}
						}
					}
					// 循环发送信息时发送时间点非空验证
					if(CherryChecker.isNullOrEmpty(commMap.get("tValue"))) {
						errorflag = true;
						this.addFieldError("tValue_"+i, getText("ECM00009",new String[]{getText("CTM00009")}));
					}else{
						// 循环发送信息时发送时间点合法验证
						if(!CherryChecker.checkTime(ConvertUtil.getString(commMap.get("tValue"))+":00")){
							errorflag = true;
							this.addFieldError("tValue_"+i, getText("ECM00026",new String[]{getText("CTM00009")}));
						}
					}
				}else if(timeType.equals("4")){
					// 按条件发送信息时条件值非空验证
					if(CherryChecker.isNullOrEmpty(commMap.get("conditionValue"))) {
						errorflag = true;
						this.addFieldError("conditionValue_"+i, getText("ECM00009",new String[]{getText("CTM00011")}));
					}else{
						// 按条件发送信息时条件值合法验证
						if(!CherryChecker.isPositiveAndNegative(ConvertUtil.getString(commMap.get("conditionValue")))){
							errorflag = true;
							this.addFieldError("conditionValue_"+i, getText("ECM00030",new String[]{getText("CTM00011")}));
						}else if(ConvertUtil.getInt(commMap.get("conditionValue"))<0){
							errorflag = true;
							this.addFieldError("conditionValue_"+i, getText("ECM00033",new String[]{getText("CTM00011"),"0"}));
						}
					}
					if(!"".equals(ConvertUtil.getString(commMap.get("paramValue")))){
						if(!CherryChecker.isNumeric(ConvertUtil.getString(commMap.get("paramValue")))){
							errorflag = true;
							this.addFieldError("paramValue_"+i, getText("ECM00030",new String[]{getText("CTM00015")}));
						}else if(ConvertUtil.getInt(commMap.get("paramValue"))<1){
							errorflag = true;
							this.addFieldError("paramValue_"+i, getText("ECM00033",new String[]{getText("CTM00015"),"1"}));
						}
					}
				}
				
				// 从沟通List中获取沟通对象List
				objMapList = (List<Map<String, Object>>) commMap.get("objList");
				if (null != objMapList && !objMapList.isEmpty()){
					for(Map<String,Object> objMap : objMapList){
						// 定义沟通信息内容List
						List<Map<String, Object>> msgMapList = new ArrayList<Map<String,Object>>();
						// 沟通对象记录集非空验证
						if(CherryChecker.isNullOrEmpty(objMap.get("recordCode"))) {
							errorflag = true;
							this.addFieldError("recordName_"+j, getText("ECM00009",new String[]{getText("CTM00012")}));
						}
						// 从沟通对象List中获取沟通信息内容List
						msgMapList = (List<Map<String, Object>>) objMap.get("msgList");
						if (null != msgMapList && !msgMapList.isEmpty()){
							for(Map<String,Object> msgMap : msgMapList){
								// 沟通内容非空验证
								if(CherryChecker.isNullOrEmpty(msgMap.get("contents"))) {
									errorflag = true;
									this.addFieldError("contents_"+k, getText("ECM00009",new String[]{getText("CTM00013")}));
								}
								k++;
							}
						}
						j++;
					}
				}
				i++;
				if(errorflag){
					this.addActionError(getText("CTM00017", new String[]{ConvertUtil.getString(commMap.get("communicationName"))}));
				}
			}
		}
	}
	
	/**
	 * 检测字符串是否符合月份日期格式MM-DD
	 * 
	 * @param value
	 *            
	 * @return 如果字符串符合月份日期MM-DD格式则返回true，否则返回false
	 */
	public boolean checkMonthDay(String value) {
		if (value == null || value.length() == 0) {
			return false;
		}
		boolean ret = value
				.matches("^((0\\d|1[0-2])-([0-2]\\d|3[0-1]))$");
		if (ret) {
			return true;
		}
		return false;
	}
	
	/**
	 * 检测字符串是否符合日期格式DD
	 * 
	 * @param value
	 *            
	 * @return 如果字符串符合日期格式DD则返回true，否则返回false
	 */
	public boolean checkDay(String value) {
		if (value == null || value.length() == 0) {
			return false;
		}
		boolean ret = value
				.matches("^([0-2]\\d|3[0-1]|\\d)$");
		if (ret) {
			return true;
		}
		return false;
	}
	
	/**
	 * 检测值是否在1到16之间
	 * 
	 * @param value
	 *            
	 * @return 如果符合格式则返回true，否则返回false
	 */
	public boolean checkDayNum(String value) {
		if (value == null || value.length() == 0) {
			return false;
		}
		boolean ret = value
				.matches("^(\\d|1[0-6])$");
		if (ret) {
			return true;
		}
		return false;
	}
	
	/**
	 * 检测发送时间是否在当前时间之后
	 * 
	 * @param value
	 *            
	 * @return 如果格式则返回true，否则返回false
	 */
	public boolean checkSendTime(String dateValue, String timeValue) {
		String nowTimeValue = CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN_24_HOURS);
		String setTimeValue = dateValue + " " + timeValue;
		nowTimeValue = binolcm32_BL.addDateSecond(nowTimeValue, 120);
		if(binolcm32_BL.dateBefore(nowTimeValue, setTimeValue, CherryConstants.DATE_PATTERN_24_HOURS)){
			return true;
		}
		return false;
	}
	
	@Override
	public BINOLCTCOM03_Form getModel() {
		// TODO Auto-generated method stub
		return form;
	}

}
