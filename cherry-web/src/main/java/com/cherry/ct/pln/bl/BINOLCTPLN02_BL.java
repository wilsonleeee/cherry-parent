package com.cherry.ct.pln.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.activemq.dto.MQInfoDTO;
import com.cherry.cm.activemq.interfaces.BINOLMQCOM01_IF;
import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ct.common.service.BINOLCTCOM03_Service;
import com.cherry.ct.pln.interfaces.BINOLCTPLN02_IF;
import com.cherry.ct.pln.service.BINOLCTPLN02_Service;
import com.cherry.mq.mes.common.MessageConstants;

public class BINOLCTPLN02_BL implements BINOLCTPLN02_IF{
	
	@Resource
	private BINOLCTPLN02_Service binOLCTPLN02_Service;
	
	@Resource
	private BINOLCTCOM03_Service binOLCTCOM03_Service;
	
	@Resource
	private BINOLCM03_BL binOLCM03_BL;
	
	@Resource
	private BINOLMQCOM01_IF binOLMQCOM01_BL;
	
	@Override
	public List<Map<String, Object>> getEventSetList(Map<String, Object> map) throws Exception{
		// 获取沟通模板List
		List<Map<String, Object>> templateList = binOLCTPLN02_Service.getEventSetList(map);
		return templateList;
	}
	
	@Override
	public void tran_saveEventSet(Map<String, Object> map)
			throws Exception {
		// 系统时间
		String sysDate = binOLCTPLN02_Service.getSYSDate();
		String brandCode = ConvertUtil.getString(map.get("brandCode"));
		String brandInfoId = ConvertUtil.getString(map.get("brandInfoId"));
		String organizationInfoId = ConvertUtil.getString(map.get("organizationInfoId"));
		String eventType = ConvertUtil.getString(map.get("eventType"));
		String frequencyCode = ConvertUtil.getString(map.get("frequencyCode"));
		String sendBeginTime ="";
		String sendEndTime ="";
		String runTime = "";
		
		// 插表时的共通字段
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(CherryConstants.BRANDINFOID, brandInfoId);
		paramMap.put(CherryConstants.ORGANIZATIONINFOID, organizationInfoId);
		paramMap.put(CherryConstants.CREATE_TIME, sysDate);
		paramMap.put(CherryConstants.UPDATE_TIME, sysDate);
		paramMap.put(CherryConstants.CREATEPGM, "BINOLCTPLN02");
		paramMap.put(CherryConstants.UPDATEPGM, "BINOLCTPLN02");
		paramMap.put(CherryConstants.CREATEDBY, ConvertUtil.getString(map.get("createdBy")));
		paramMap.put(CherryConstants.UPDATEDBY, ConvertUtil.getString(map.get("updatedBy")));
		
		// 插表时的所需字段
		Map<String, Object> insertMap = new HashMap<String, Object>();
		insertMap.put("eventType", eventType);
		insertMap.put("frequencyCode", frequencyCode);
		if(!"".equals(frequencyCode) && !"0".equals(frequencyCode)){
			if(!"".equals(ConvertUtil.getString(map.get("sendBeginTime")))){
				sendBeginTime = ConvertUtil.getString(map.get("sendBeginTime"))+":00";
			}
			insertMap.put("sendBeginTime", sendBeginTime);
			if(!"".equals(ConvertUtil.getString(map.get("sendEndTime")))){
				sendEndTime = ConvertUtil.getString(map.get("sendEndTime"))+":59";
			}
			insertMap.put("sendEndTime", sendEndTime);
		}
		insertMap.putAll(paramMap);
		
		Map<String, Object> eventScMap = new HashMap<String, Object>();
		eventScMap.put("eventType", eventType);
		eventScMap.putAll(paramMap);
		// 停用延时事件触发调度
		binOLCTPLN02_Service.updateEventSchedulesFlag(eventScMap);
		
		// 保存事件延时设置
		int setCount = binOLCTPLN02_Service.getDelaySetCount(insertMap);
		if(setCount > 0){
			binOLCTPLN02_Service.updateDelaySetInfo(insertMap);
		}else{
			binOLCTPLN02_Service.insertDelaySetInfo(insertMap);
		}
		// 停用事件设置
		binOLCTPLN02_Service.stopEventSet(insertMap);
		// 定义事件设置List
		List<Map<String, Object>> eventSetList = new ArrayList<Map<String,Object>>();
		eventSetList = ConvertUtil.json2List(ConvertUtil.getString(map.get("eventSetInfo")));
		if (null != eventSetList && !eventSetList.isEmpty()){
			for(Map<String,Object> eventSetMap : eventSetList){
				String searchCode = ConvertUtil.getString(eventSetMap.get("searchCode"));
				insertMap.put("searchCode", searchCode);
				insertMap.put("messageType", ConvertUtil.getString(eventSetMap.get("messageType")));
				insertMap.put("contents", ConvertUtil.getString(eventSetMap.get("contents")));
				insertMap.put("isTemplate", ConvertUtil.getString(eventSetMap.get("isTemplate")));
				insertMap.put("templateCode", ConvertUtil.getString(eventSetMap.get("templateCode")));
				insertMap.put("activityCode", ConvertUtil.getString(eventSetMap.get("activityCode")));
				insertMap.put("smsChannel", ConvertUtil.getString(eventSetMap.get("smsChannel")));
				// 信息内容不为空的情况下插入新的沟通设置信息
				if(!"".equals(ConvertUtil.getString(eventSetMap.get("contents")))){
					// 插入新的设置信息
					binOLCTPLN02_Service.insertEventSetInfo(insertMap);
				}
				// 搜索编号不为空的情况下判断是否存在对应的搜索记录，若不存在则新增
				if(!"".equals(searchCode)){
					Map<String, Object> tempMap = new HashMap<String, Object>();
					tempMap.put("searchCode", searchCode);
					int recordCount = binOLCTCOM03_Service.getObjRecordCountByCode(tempMap);
					// 当获取的搜索记录为新增标识时进入搜索记录保存逻辑
					if (recordCount<1){
						// 定义沟通对象Map
						Map<String, Object> commObjMap = new HashMap<String, Object>();
						// 组织代号
						commObjMap.put("organizationInfoId", organizationInfoId);
						// 品牌ID
						commObjMap.put("brandInfoId", brandInfoId);
						// 搜索记录编号
						commObjMap.put("recordCode", searchCode);
						// 搜索记录名称
						commObjMap.put("recordName", eventSetMap.get("recordName"));
						// 客户类型
						commObjMap.put("customerType", eventSetMap.get("customerType"));
						// 搜索记录类型（1.搜索条件/2.搜索结果）
						commObjMap.put("recordType", eventSetMap.get("recordType"));
						// 沟通对象人数统计
						commObjMap.put("recordCount", eventSetMap.get("recordCount"));
						// 沟通对象搜索条件
						commObjMap.put("conditionInfo", eventSetMap.get("conditionInfo"));
						// 备注
						commObjMap.put("comments", eventSetMap.get("comments"));
						// 沟通对象来源
						commObjMap.put("fromType", eventSetMap.get("fromType"));
						// 增加共通字段
						commObjMap.putAll(insertMap);
						
						// 增加沟通对象信息
						binOLCTCOM03_Service.insertCommObjectInfo(commObjMap);
					}
				}
			}
		}
		
		if(!"".equals(frequencyCode) && !"0".equals(frequencyCode)){
			if("".equals(sendBeginTime)){
				runTime = "00" + " " + "00" + " " + "09" + " " + "*" + " " + "*" + " " + "?";
			}else{
				String[] times = sendBeginTime.split(":");
				runTime = times[2] + " " + times[1] + " " + times[0] + " " + "*" + " " + "*" + " " + "?";
			}
			// 定义调度查询参数Map
			Map<String, Object> schedulesMap = new HashMap<String, Object>();
			// 调度类型
			schedulesMap.put("taskType", "DE");
			// 沟通编号
			schedulesMap.put("taskCode", eventType);
			// 调度时间表达式
			schedulesMap.put("runTime", runTime);
			// 调度是否允许重运行
			schedulesMap.put("allowRepeat", "1");
			// 初始运行状态
			schedulesMap.put("status", 1);
			// 初始加载状态
			schedulesMap.put("loadFlag", 0);
			// 初始重运行次数
			schedulesMap.put("runCount", 0);
			// 增加共通字段
			schedulesMap.putAll(paramMap);
			// 增加调度信息
			binOLCTCOM03_Service.insertGtSchedules(schedulesMap);
			
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
			
			dataLine.put("MainData", mainData);
			msgDataMap.put("DataLine", dataLine);
			mqInfoDTO.setMsgDataMap(msgDataMap);
			// 发送MQ消息
			binOLMQCOM01_BL.sendMQMsg(mqInfoDTO, false);
		}
		
	}
	
	// 停用沟通事件设置
	@Override
	public void tran_stopEventSet(Map<String, Object> map) throws Exception {
		// 停用延时事件触发调度
		binOLCTPLN02_Service.updateEventSchedulesFlag(map);
		// 停用事件设置
		binOLCTPLN02_Service.stopEventSet(map);	
		// 删除事件延时设置信息
		binOLCTPLN02_Service.deleteDelaySetInfo(map);
	}
	
	@Override
	public Map<String, Object> getDelaySetInfo(Map<String, Object> map) throws Exception{
		// 获取沟通模板List
		Map<String, Object> delayMap = binOLCTPLN02_Service.getDelaySetInfo(map);
		return delayMap;
	}
}
