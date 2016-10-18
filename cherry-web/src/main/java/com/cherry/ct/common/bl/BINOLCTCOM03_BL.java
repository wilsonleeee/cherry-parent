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
package com.cherry.ct.common.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.activemq.dto.MQInfoDTO;
import com.cherry.cm.activemq.interfaces.BINOLMQCOM01_IF;
import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM15_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM32_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.cherry.ct.common.interfaces.BINOLCTCOM03_IF;
import com.cherry.ct.common.service.BINOLCTCOM03_Service;
import com.cherry.mq.mes.common.MessageConstants;

/**
 * 沟通计划预览与确认BL
 * 
 * @author ZhangGS
 * @version 1.0 2012.12.11
 */
public class BINOLCTCOM03_BL implements BINOLCTCOM03_IF {
	@Resource
	private BINOLCTCOM03_Service binOLCTCOM03_Service;
	
	/** 系统配置项 共通BL */
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	
	/** 各类编号取号共通BL */
	@Resource
	private BINOLCM15_BL binOLCM15_BL;
	
	/** 沟通模块共通BL */
	@Resource
	private BINOLCM32_BL binolcm32_BL;
	
	@Resource
	private BINOLMQCOM01_IF binOLMQCOM01_BL;
	
	@Resource
	private BINOLCM03_BL binOLCM03_BL;
	
	@Override
	@SuppressWarnings("unchecked")
	public void tran_saveCommInfo(Map<String, Object> planMap,
			List<Map<String, Object>> commMapList, String type) throws Exception {
		// TODO Auto-generated method stub		
		planMap = CherryUtil.removeEmptyVal(planMap);
		String smsChannel = "";
		if(null != commMapList && !commMapList.isEmpty()){
			String brandInfoId = ConvertUtil.getString(planMap.get("brandInfoId"));
			String brandCode = ConvertUtil.getString(planMap.get("brandCode"));
			String organizationInfoId = ConvertUtil.getString(planMap.get("organizationInfoId"));
			String organizationId = ConvertUtil.getString(planMap.get("organizationId"));
			// 插表时的共通字段
			Map<String, Object> insertMap = new HashMap<String, Object>();
			// 系统时间
			String sysDate = binOLCTCOM03_Service.getSYSDate();
			// 任务类型
			insertMap.put("taskType", CherryConstants.TASK_TYPE_VALUE);
			// 作成日时
			insertMap.put(CherryConstants.CREATE_TIME, sysDate);
			// 更新日时
			insertMap.put(CherryConstants.UPDATE_TIME, sysDate);
			// 作成程序名
			insertMap.put(CherryConstants.CREATEPGM, "BINOLCTCOM03");
			// 更新程序名
			insertMap.put(CherryConstants.UPDATEPGM, "BINOLCTCOM03");
			// 增加共通字段
			planMap.putAll(insertMap);
		
			if(type.equals("INSERT")){
				if (null != planMap.get("campaignCode") && !"".equals(ConvertUtil.getString(planMap.get("campaignCode")))){
					// 插入沟通与活动关联信息
					binOLCTCOM03_Service.insertCommAsActivity(planMap);
				}
				// 插入沟通计划信息
				binOLCTCOM03_Service.insertCommPlanInfo(planMap);
			}else{
				// 更新沟通计划信息
				binOLCTCOM03_Service.updateCommPlanInfo(planMap);
				// 停用沟通信息
				binOLCTCOM03_Service.stopCommInfo(planMap);
				// 删除沟通设置信息
				binOLCTCOM03_Service.deleteCommSetInfo(planMap);
				// 将调度信息置为无效
				binOLCTCOM03_Service.updateSchedulesFlag(planMap);
			}
			
			for(Map<String,Object> commMap : commMapList){
				// 定义沟通信息Map
				Map<String, Object> commInfoMap = new HashMap<String, Object>();
				// 定义沟通对象List
				List<Map<String, Object>> objMapList = new ArrayList<Map<String,Object>>();
				// 获取沟通编号
				String commode = binOLCM15_BL.getSequenceId(ConvertUtil.getInt(planMap
						.get("organizationInfoId")), ConvertUtil.getInt(planMap.get("brandInfoId")), "9");
				// 获取沟通时间类型
				String timeType = ConvertUtil.getString(commMap.get("timeType"));
				// 获取阶段标识
				String phaseNum = ConvertUtil.getString(commMap.get("communicationCode"));
				// 沟通编号
				commInfoMap.put("communicationCode", commode);
				// 组织代号
				commInfoMap.put("organizationInfoId", organizationInfoId);
				// 品牌ID
				commInfoMap.put("brandInfoId", brandInfoId);
				// 沟通计划编号
				commInfoMap.put("planCode", planMap.get("planCode"));
				// 沟通阶段标识
				commInfoMap.put("phaseNum", phaseNum);
				// 沟通名称
				commInfoMap.put("communicationName", commMap.get("communicationName"));
				// 沟通时间类型
				commInfoMap.put("timeType", timeType);
				
				// 定义调度查询参数Map
				Map<String, Object> schedulesMap = new HashMap<String, Object>();
				// 组织代号
				schedulesMap.put("organizationInfoId", organizationInfoId);
				// 品牌ID
				schedulesMap.put("brandInfoId", brandInfoId);
				// 调度类型
				schedulesMap.put("taskType", CherryConstants.TASK_TYPE_VALUE);
				// 沟通编号
				schedulesMap.put("taskCode", commode);
				
				// 判断沟通时间类型以确定插入沟通表的时间值
				if(timeType.equals("1")){
					String sendDate = ConvertUtil.getString(commMap.get("sendDate"));
					String sendTime = ConvertUtil.getString(commMap.get("sendTime"))+":00";
					String nowTime = CherryUtil.getSysDateTime(CherryConstants.DATE_PATTERN_24_HOURS);
					String runTimeValue = sendDate + " " + sendTime;
					// 比较当前时间与设置时间，如果设置时间在当前时间之前则将设置时间修改为当前时间加上120秒，防止一次性执行的沟通由于在确认页面停留时间过长导致设置时间过期的情况
					if(binolcm32_BL.dateBefore(runTimeValue,nowTime,CherryConstants.DATE_PATTERN_24_HOURS)){
						runTimeValue = binolcm32_BL.addDateSecond(nowTime, 120);
						sendDate = DateUtil.coverTime2YMD(runTimeValue, CherryConstants.DATE_PATTERN);
						sendTime = DateUtil.getSpecificDate(runTimeValue, "HH:mm:ss");
					}
					// 发送日期
					commInfoMap.put("dateValue", sendDate);
					// 发送时间点
					commInfoMap.put("timeValue", sendTime);
					
					String runTime = CherryUtil.convertDateToCronExp(timeType, sendDate, sendTime, "");
					// 调度时间表达式
					schedulesMap.put("runTime",runTime);
					// 调度是否允许重运行（指定时间情况下不允许）
					schedulesMap.put("allowRepeat", "0");
				}else if(timeType.equals("2")){
					// 参考条件
					commInfoMap.put("condition", commMap.get("referTypeCode"));
					// 参考时间提前值
					if("1".equals(ConvertUtil.getString(commMap.get("referAttribute")))){
						int dateValue = ConvertUtil.getInt(commMap.get("dateValue"));
						commInfoMap.put("dateValue", -dateValue);
					}else{
						commInfoMap.put("dateValue", commMap.get("dateValue"));
					}
					// 信息发送时间点
					commInfoMap.put("timeValue", ConvertUtil.getString(commMap.get("timeValue"))+":00");
					// 有效开始日期
					commInfoMap.put("runBeginDate", commMap.get("runBeginTime"));
					// 有效截止日期
					commInfoMap.put("runEndDate", commMap.get("runEndTime"));
					
					String runTime = CherryUtil.convertDateToCronExp(timeType, "", ConvertUtil.getString(commMap.get("timeValue"))+":00", "");
					// 调度时间表达式
					schedulesMap.put("runTime",runTime);
					// 调度有效开始时间
					schedulesMap.put("runBeginDate", commMap.get("runBeginTime"));
					// 调度有效截止时间
					schedulesMap.put("runEndDate", commMap.get("runEndTime"));
					// 调度是否允许重运行（参考时间情况下每天只允许运行一次）
					schedulesMap.put("allowRepeat", "2");
				}else if(timeType.equals("3")){
					// 频率代号
					commInfoMap.put("frequency", commMap.get("frequencyCode"));
					// 参数属性
					commInfoMap.put("condition", commMap.get("forAttribute"));
					// 发送日期
					commInfoMap.put("dateValue", commMap.get("dayValue"));
					// 发送时间点
					commInfoMap.put("timeValue", ConvertUtil.getString(commMap.get("tValue"))+":00");
					// 有效开始日期
					commInfoMap.put("runBeginDate", commMap.get("sendBeginTime"));
					// 有效截止日期
					commInfoMap.put("runEndDate", commMap.get("sendEndTime"));
					
					String frequency = ConvertUtil.getString(commMap.get("frequencyCode"));
					String condition = ConvertUtil.getString(commMap.get("forAttribute"));
					String dayvalue = ConvertUtil.getString(commMap.get("dayValue"));
					String timeValue = ConvertUtil.getString(commMap.get("tValue"))+":00";
					String runTime = "";
					String runDate = "*";
					if("1".equals(frequency)){
						runTime = CherryUtil.convertDateToCronExp(timeType, runDate, timeValue, "DD");
					}else if("2".equals(frequency)){
						if("A".equals(condition)){
							runDate = dayvalue;
						}else if("B".equals(condition)){
							runDate = "1-"+dayvalue;
						}else{
							runDate = "*";
						}
						runTime = CherryUtil.convertDateToCronExp(timeType, runDate, timeValue, "MM");
					}else if("3".equals(frequency)){
						if("A".equals(condition)){
							runDate = dayvalue;
						}else if("B".equals(condition)){
							runDate = "1-*";
						}else if("L".equals(condition)){
							runDate = "12-*";
						}else{
							runDate = "*-*";
						}
						runTime = CherryUtil.convertDateToCronExp(timeType, runDate, timeValue, "YY");
					}
					// 调度时间表达式
					schedulesMap.put("runTime",runTime);
					// 调度有效开始时间
					schedulesMap.put("runBeginDate", commMap.get("sendBeginTime"));
					// 调度有效截止时间
					schedulesMap.put("runEndDate", commMap.get("sendEndTime"));
					// 调度是否允许重运行（循环情况下每天只允许运行一次）
					schedulesMap.put("allowRepeat", "2");
				}else if(timeType.equals("4")){
					if(!"".equals(ConvertUtil.getString(commMap.get("paramValue")))){
						// 购买日期值（条件为会员购买情况时）
						commInfoMap.put("dateValue", commMap.get("paramValue"));
						// 频率参数
						commInfoMap.put("frequency", commMap.get("paramAttribute"));
					}
					// 条件代号
					commInfoMap.put("condition", commMap.get("conditionCode"));
					// 条件值
					commInfoMap.put("value", commMap.get("conditionValue"));
					// 有效开始日期
					commInfoMap.put("runBeginDate", commMap.get("runBegin"));
					// 有效截止日期
					commInfoMap.put("runEndDate", commMap.get("runEnd"));
					
					String runTime = binOLCM14_BL.getConfigValue("1072", organizationInfoId, brandInfoId);
					// 调度时间表达式
					schedulesMap.put("runTime",runTime);
					// 调度有效开始时间
					schedulesMap.put("runBeginDate", commMap.get("runBegin"));
					// 调度有效截止时间
					schedulesMap.put("runEndDate", commMap.get("runEnd"));
					// 调度是否允许重运行（条件触发情况下每天允许多次运行）
					schedulesMap.put("allowRepeat", "1");
				}
				// 排除已发送对象标识
				commInfoMap.put("runType", commMap.get("runType"));
				// 创建人
				commInfoMap.put("createdBy", planMap.get("createdBy"));
				// 修改人
				commInfoMap.put("updatedBy", planMap.get("updatedBy"));
				// 增加共通字段
				commInfoMap.putAll(insertMap);
				// 增加沟通信息
				binOLCTCOM03_Service.insertCommInfo(commInfoMap);
				
				if(!"".equals(timeType) && !"5".equals(timeType)){
					// 初始运行状态
					schedulesMap.put("status", 1);
					// 初始加载状态
					schedulesMap.put("loadFlag", 0);
					// 初始重运行次数
					schedulesMap.put("runCount", 0);
					// 增加共通字段
					schedulesMap.putAll(insertMap);
					// 增加新调度信息
					binOLCTCOM03_Service.insertGtSchedules(schedulesMap);
				}
				
				// 从沟通List中获取沟通对象List
				objMapList = (List<Map<String, Object>>) commMap.get("objList");
				if (null != objMapList && !objMapList.isEmpty()){
					for(Map<String,Object> objMap : objMapList){
						// 定义沟通信息内容List
						List<Map<String, Object>> msgMapList = new ArrayList<Map<String,Object>>();
						// 定义沟通对象Map
						Map<String, Object> commObjMap = new HashMap<String, Object>();
						// 从沟通对象List中获取搜索记录编号
						String searchObjCode = ConvertUtil.getString(objMap.get("recordCode"));
						
						if(!"".equals(searchObjCode)){
							Map<String, Object> tempMap = new HashMap<String, Object>();
							tempMap.put("searchCode", searchObjCode);
							int recordCount = binOLCTCOM03_Service.getObjRecordCountByCode(tempMap);
							// 当获取的搜索记录为新增标识时进入搜索记录保存逻辑
							if (recordCount<1){
								String comments = ConvertUtil.getString(objMap.get("conditionDisplay"));
								if(comments.length() > 485){
									comments = comments.substring(0, 480) + "...";
								}
								// 组织代号
								commObjMap.put("organizationInfoId", organizationInfoId);
								// 品牌ID
								commObjMap.put("brandInfoId", brandInfoId);
								// 部门ID
								commObjMap.put("organizationId", organizationId);
								// 搜索记录编号
								commObjMap.put("recordCode", searchObjCode);
								// 搜索记录名称
								commObjMap.put("recordName", objMap.get("recordName"));
								// 客户类型
								commObjMap.put("customerType", objMap.get("customerType"));
								// 搜索记录类型（1.搜索条件/2.搜索结果）
								commObjMap.put("recordType", objMap.get("recordType"));
								// 沟通对象人数统计
								commObjMap.put("recordCount", objMap.get("recordCount"));
								// 沟通对象搜索条件
								commObjMap.put("conditionInfo", objMap.get("conditionInfo"));
								// 备注
								commObjMap.put("comments", comments);
								// 沟通对象来源
								commObjMap.put("fromType", objMap.get("fromType"));
								// 增加共通字段
								commObjMap.putAll(insertMap);
								
								// 增加沟通对象信息
								binOLCTCOM03_Service.insertCommObjectInfo(commObjMap);
							}
						}
						
						// 从沟通对象List中获取沟通信息内容List
						msgMapList = (List<Map<String, Object>>) objMap.get("msgList");
						if (null != msgMapList && !msgMapList.isEmpty()){
							for(Map<String,Object> msgMap : msgMapList){
								// 定义沟通设置Map
								Map<String, Object> commSetMap = new HashMap<String, Object>();
								// 组织代号
								commSetMap.put("organizationInfoId", organizationInfoId);
								// 品牌ID
								commSetMap.put("brandInfoId", brandInfoId);
								// 沟通计划编号
								commSetMap.put("planCode", planMap.get("planCode"));
								// 沟通编号
								commSetMap.put("communicationCode", commode);
								// 搜索记录编号
								commSetMap.put("recordCode", searchObjCode);
								// 是否使用信息模板
								commSetMap.put("isTemplate", msgMap.get("isTemplate"));
								// 信息模板编号
								commSetMap.put("templateCode", msgMap.get("templateCode"));
								// 信息内容
								commSetMap.put("messageInfo", msgMap.get("contents"));
								// 沟通信息类型
								commSetMap.put("commType", msgMap.get("messageType"));
								//短信通道类型
								smsChannel=ConvertUtil.getString(msgMap.get("smsChannel"));
								commSetMap.put("smsChannel",smsChannel);
								// 创建人
								commSetMap.put("createdBy", planMap.get("createdBy"));
								// 修改人
								commSetMap.put("updatedBy", planMap.get("updatedBy"));
								// 增加共通字段
								commSetMap.putAll(insertMap);
								// 增加沟通设置信息
								binOLCTCOM03_Service.insertCommSetInfo(commSetMap);
							}
						}
						
					}
				}
			}
			// 发送MQ
			MQInfoDTO mqInfoDTO = new MQInfoDTO();
			// 品牌代码
			mqInfoDTO.setBrandCode(brandCode);
			
			String billType = CherryConstants.MESSAGE_TYPE_RT;
			
			String billCode = binOLCM03_BL.getTicketNumber(Integer.parseInt(organizationInfoId), Integer.parseInt(brandInfoId), "", billType);
			// 业务类型
			mqInfoDTO.setBillType(billType);
			// 单据号
			mqInfoDTO.setBillCode(billCode);
			// 消息发送队列名
			mqInfoDTO.setMsgQueueName(CherryConstants.CHERRYSCHEDULETASKMSGQUEUE);
			
			// 设定消息内容
			Map<String,Object> msgDataMap = new HashMap<String,Object>();
			// 设定消息版本号
			msgDataMap.put("Version", CherryConstants.MESSAGE_VERSION_GT);
			// 设定消息命令类型
			msgDataMap.put("Type", CherryConstants.MESSAGE_TYPE_1006);
			// 设定消息数据类型
			msgDataMap.put("DataType", MessageConstants.DATATYPE_APPLICATION_JSON);
			// 设定消息的数据行
			Map<String,Object> dataLine = new HashMap<String,Object>();
			// 消息的主数据行
			Map<String,Object> mainData = new HashMap<String,Object>();
			// 品牌代码
			mainData.put("BrandCode", brandCode);
			// 业务类型
			mainData.put("TradeType", billType);
			// 单据号
			mainData.put("TradeNoIF", billCode);
			//短信通道类型
			mainData.put("smsChannel", smsChannel);
			dataLine.put("MainData", mainData);
			msgDataMap.put("DataLine", dataLine);
			mqInfoDTO.setMsgDataMap(msgDataMap);
			// 发送MQ消息
			binOLMQCOM01_BL.sendMQMsg(mqInfoDTO, false);
		}
		
	}
	
	/**
	 * 根据沟通计划编号取得沟通计划详细信息
	 * 
	 * @param Map
	 *			查询条件
	 * @return map
	 *			沟通计划信息
	 */
	@Override
	public Map<String, Object> getPlanInfoByCode(Map<String, Object> map)
			throws Exception {
		// TODO Auto-generated method stub
		return binOLCTCOM03_Service.getPlanInfoByCode(map);
	}
	
}
