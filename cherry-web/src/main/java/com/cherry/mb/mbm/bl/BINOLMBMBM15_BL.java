/*
 * @(#)BINOLMBMBM15_BL.java     1.0 2013/04/26
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
package com.cherry.mb.mbm.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.activemq.dto.MQInfoDTO;
import com.cherry.cm.activemq.interfaces.BINOLMQCOM01_IF;
import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM27_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.mb.mbm.service.BINOLMBMBM15_Service;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * 会员发卡柜台变更处理BL
 * 
 * @author WangCT
 * @version 1.0 2013/04/26
 */
public class BINOLMBMBM15_BL {
	
	/** 会员发卡柜台变更处理Service **/
	@Resource
	private BINOLMBMBM15_Service binOLMBMBM15_Service;
	
	/** WebService共通BL */
	@Resource
	private BINOLCM27_BL binOLCM27_BL;
	
	/** 取得各种业务类型的单据流水号  **/
	@Resource
	private BINOLCM03_BL binOLCM03_BL;
	
	/** 发送MQ消息共通处理  **/
	@Resource
	private BINOLMQCOM01_IF binOLMQCOM01_BL;
	
	/** 系统配置项 共通BL */
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	
	/**
	 * 会员发卡柜台转柜处理
	 * 
	 * @param map 更新条件
	 */
	public void tran_moveMemCounter(Map<String, Object> map) throws Exception {
		
		// 取得会员转柜批次号
		String batchNo = binOLCM03_BL.getTicketNumber(Integer.parseInt(map.get("organizationInfoId").toString()), 
				Integer.parseInt(map.get("brandInfoId").toString()), "", CherryConstants.MSG_MEMBER_MF);
		map.put("batchNo", batchNo);
		// 取得系统时间
		String sysDate = binOLMBMBM15_Service.getSYSDateTime();
		map.put("sysDate", sysDate);
		// 新柜台ID
		Object newOrgId = map.get("newOrgId");
		// 新柜台号
		Object newCounterCode = map.get("newCounterCode");
		// 新柜台测试区分
		Object newCounterKind = map.get("newCounterKind");
		String testType = null;
		// 发卡柜台为测试柜台的场合设定该会员为测试会员
		if(newCounterKind != null && "1".equals(newCounterKind.toString())) {
			testType = "0";
		} else {
			testType = "1";
		}
		// 组织ID
		Object organizationInfoId = map.get("organizationInfoId");
		// 品牌ID
		Object brandInfoId = map.get("brandInfoId");
		// 操作员工代码
		Object modifyEmployee = map.get("modifyEmployee");
		Object createdBy = map.get(CherryConstants.CREATEDBY);
		Object createPgm = map.get(CherryConstants.CREATEPGM);
		Object updateBy = map.get(CherryConstants.UPDATEDBY);
		Object updatePgm = map.get(CherryConstants.UPDATEPGM);
		// 数据查询长度
		int dataSize = CherryConstants.BATCH_PAGE_MAX_NUM;
		// 查询数据量
		map.put("COUNT", dataSize);
		while (true) {
			// 查询原发卡柜台的会员信息List
			List<Map<String, Object>> oldCounterMemInfoList = binOLMBMBM15_Service.getOldCounterMemInfoList(map);
			if(oldCounterMemInfoList != null && !oldCounterMemInfoList.isEmpty()) {
				List<String> memInfoIdList = new ArrayList<String>();
				List<Map<String, Object>> _oldCounterMemInfoList = new ArrayList<Map<String,Object>>();
				for(int i = 0; i < oldCounterMemInfoList.size(); i++) {
					Map<String, Object> oldCounterMemInfo = oldCounterMemInfoList.get(i);
					String memberInfoId = String.valueOf(oldCounterMemInfo.get("memberInfoId"));
					if(!memInfoIdList.contains(memberInfoId)) {
						memInfoIdList.add(memberInfoId);
						oldCounterMemInfo.put("newOrgId", newOrgId);
						oldCounterMemInfo.put("newCounterCode", newCounterCode);
						oldCounterMemInfo.put("testType", testType);
						oldCounterMemInfo.put("organizationInfoId", organizationInfoId);
						oldCounterMemInfo.put("brandInfoId", brandInfoId);
						oldCounterMemInfo.put("modifyTime", sysDate);
						oldCounterMemInfo.put("modifyEmployee", modifyEmployee);
						oldCounterMemInfo.put("modifyType", "4");
						oldCounterMemInfo.put("sourse", "Cherry");
						oldCounterMemInfo.put("batchNo", batchNo);
						oldCounterMemInfo.put(CherryConstants.CREATEDBY, createdBy);
						oldCounterMemInfo.put(CherryConstants.CREATEPGM, createPgm);
						oldCounterMemInfo.put(CherryConstants.UPDATEDBY, updateBy);
						oldCounterMemInfo.put(CherryConstants.UPDATEPGM, updatePgm);
						_oldCounterMemInfoList.add(oldCounterMemInfo);
					}
				}
				// 批量更新会员发卡柜台
				binOLMBMBM15_Service.updMemCounter(_oldCounterMemInfoList);
				// 批量添加会员修改履历主信息
				binOLMBMBM15_Service.addMemInfoRecord(_oldCounterMemInfoList);
				// 少于一次抽取的数量，即为最后一页，跳出循环
				if(oldCounterMemInfoList.size() < dataSize) {
					break;
				}
			} else {
				break;
			}
		}
		// 修改字段为发卡柜台
		map.put("modifyField", "13");
		binOLMBMBM15_Service.addMemInfoRecordDetail(map);
		
		// 调用WebService和老后台同步会员转柜
		if(binOLCM14_BL.isConfigOpen("1297", String.valueOf(organizationInfoId), String.valueOf(brandInfoId))) {
			this.synMemberInfo(map);
		}
		
		// 会员转柜需要发送沟通MQ
		if(binOLCM14_BL.isConfigOpen("1085", String.valueOf(organizationInfoId), String.valueOf(brandInfoId))) {
			// 发送发卡柜台变更沟通事件MQ消息
			this.sendGTMQ(map);
		}
	}
	
	/**
	 * 撤销会员发卡柜台转柜处理
	 * 
	 * @param map 更新条件
	 */
	public void tran_reMoveMemCounter(Map<String, Object> map) throws Exception {
		
		// 取得会员转柜批次号
		String batchNo = binOLCM03_BL.getTicketNumber(Integer.parseInt(map.get("organizationInfoId").toString()), 
				Integer.parseInt(map.get("brandInfoId").toString()), "", CherryConstants.MSG_MEMBER_MF);
		map.put("batchNo", batchNo);
		// 取得系统时间
		String sysDate = binOLMBMBM15_Service.getSYSDateTime();
		map.put("sysDate", sysDate);
		// 新柜台ID
		Object newOrgId = map.get("newOrgId");
		// 新柜台号
		Object newCounterCode = map.get("newCounterCode");
		// 新柜台测试区分
		Object newCounterKind = map.get("newCounterKind");
		String testType = null;
		// 发卡柜台为测试柜台的场合设定该会员为测试会员
		if(newCounterKind != null && "1".equals(newCounterKind.toString())) {
			testType = "0";
		} else {
			testType = "1";
		}
		// 组织ID
		Object organizationInfoId = map.get("organizationInfoId");
		// 品牌ID
		Object brandInfoId = map.get("brandInfoId");
		// 操作员工代码
		Object modifyEmployee = map.get("modifyEmployee");
		Object createdBy = map.get(CherryConstants.CREATEDBY);
		Object createPgm = map.get(CherryConstants.CREATEPGM);
		Object updateBy = map.get(CherryConstants.UPDATEDBY);
		Object updatePgm = map.get(CherryConstants.UPDATEPGM);
		// 备注信息
		String remark = (String)map.get("remark");
		// 数据查询长度
		int dataSize = CherryConstants.BATCH_PAGE_MAX_NUM;
		// 数据抽出次数
		int currentNum = 0;
		// 查询开始位置
		int startNum = 0;
		// 查询结束位置
		int endNum = 0;
		// 排序字段
		map.put(CherryConstants.SORT_ID, "memberInfoId");
		while (true) {
			// 查询开始位置
			startNum = dataSize * currentNum + 1;
			// 查询结束位置
			endNum = startNum + dataSize - 1;
			// 数据抽出次数累加
			currentNum++;
			// 查询开始位置
			map.put(CherryConstants.START, startNum);
			// 查询结束位置
			map.put(CherryConstants.END, endNum);
			// 查询需要撤销转柜的会员信息List
			List<Map<String, Object>> reMoveCouMemInfoList = binOLMBMBM15_Service.getReMoveCouMemInfoList(map);
			if(reMoveCouMemInfoList != null && !reMoveCouMemInfoList.isEmpty()) {
				for(int i = 0; i < reMoveCouMemInfoList.size(); i++) {
					Map<String, Object> reMoveCouMemInfoMap = reMoveCouMemInfoList.get(i);
					reMoveCouMemInfoMap.put("newOrgId", newOrgId);
					reMoveCouMemInfoMap.put("newCounterCode", newCounterCode);
					reMoveCouMemInfoMap.put("testType", testType);
					reMoveCouMemInfoMap.put("organizationInfoId", organizationInfoId);
					reMoveCouMemInfoMap.put("brandInfoId", brandInfoId);
					reMoveCouMemInfoMap.put("modifyTime", sysDate);
					reMoveCouMemInfoMap.put("modifyEmployee", modifyEmployee);
					reMoveCouMemInfoMap.put("modifyType", "4");
					reMoveCouMemInfoMap.put("sourse", "Cherry");
					reMoveCouMemInfoMap.put("batchNo", batchNo);
					reMoveCouMemInfoMap.put("remark", remark);
					reMoveCouMemInfoMap.put(CherryConstants.CREATEDBY, createdBy);
					reMoveCouMemInfoMap.put(CherryConstants.CREATEPGM, createPgm);
					reMoveCouMemInfoMap.put(CherryConstants.UPDATEDBY, updateBy);
					reMoveCouMemInfoMap.put(CherryConstants.UPDATEPGM, updatePgm);
				}
				// 批量更新会员发卡柜台
				binOLMBMBM15_Service.updMemCounter(reMoveCouMemInfoList);
				// 批量添加会员修改履历主信息
				binOLMBMBM15_Service.addMemInfoRecord(reMoveCouMemInfoList);
				// 少于一次抽取的数量，即为最后一页，跳出循环
				if(reMoveCouMemInfoList.size() < dataSize) {
					break;
				}
			} else {
				break;
			}
		}
		// 修改字段为发卡柜台
		map.put("modifyField", "13");
		binOLMBMBM15_Service.addMemInfoRecordDetail(map);
		
		// 调用WebService和老后台同步会员转柜
		if(binOLCM14_BL.isConfigOpen("1297", String.valueOf(organizationInfoId), String.valueOf(brandInfoId))) {
			this.synMemberInfo(map);
		}
		
		// 转柜撤销需要发送沟通MQ
		if(binOLCM14_BL.isConfigOpen("1098", String.valueOf(organizationInfoId), String.valueOf(brandInfoId))) {
			// 发送发卡柜台变更沟通事件MQ消息
			this.sendGTMQ(map);
		}
	}
	
	/**
	 * 调用WebService和老后台同步会员转柜
	 * 
	 * @param map 会员信息
	 */
	public void synMemberInfo(Map<String, Object> map) throws Exception {
		
		Map<String, Object> memberInfoMap = new HashMap<String, Object>();
		// 品牌代码
		memberInfoMap.put("BrandCode", map.get(CherryConstants.BRAND_CODE));
		// 业务类型
		memberInfoMap.put("BussinessType", "changeCounter");
		// 版本号
		memberInfoMap.put("Version", "1.0");
		// 转柜方式：1.正常转柜；2.转柜撤销
		String subType = (String)map.get("subType");
		memberInfoMap.put("SubType", subType);
		// 老柜台号
		memberInfoMap.put("OldCounterCode", map.get("oldCounterCode"));
		// 新柜台号
		memberInfoMap.put("NewCounterCode", map.get("newCounterCode"));
		if("1".equals(subType)) {
			// 转柜批次号
			memberInfoMap.put("BatchCode", map.get("batchNo"));
		} else {
			// 转柜撤销批次号
			memberInfoMap.put("BatchCode", map.get("batchCode"));
		}
		// 访问webService同步老后台会员转柜
		Map<String, Object> resultMap = binOLCM27_BL.accessWebService(memberInfoMap);
		String State = (String)resultMap.get("State");
		String Data = (String)resultMap.get("Data");
		if(State.equals("ERROR")) {
			CherryException CherryException = new CherryException("ECM00089");
			CherryException.setErrMessage(Data);
			throw CherryException;
		}
	}
	
	/**
	 * 发送发卡柜台变更沟通事件MQ消息
	 * 
	 * @param map
	 * @throws Exception
	 */
	public void sendGTMQ(Map<String, Object> map) throws Exception {
		
		// 设定MQ消息DTO
		MQInfoDTO mqInfoDTO = new MQInfoDTO();
		// 品牌代码
		String brandCode = (String)map.get("brandCode");
		mqInfoDTO.setBrandCode(brandCode);
		// 组织代码
		mqInfoDTO.setOrgCode((String)map.get("orgCode"));
		// 组织ID
		mqInfoDTO.setOrganizationInfoId(Integer.parseInt(map.get("organizationInfoId").toString()));
		// 品牌ID
		mqInfoDTO.setBrandInfoId(Integer.parseInt(map.get("brandInfoId").toString()));
		String billType = CherryConstants.MESSAGE_TYPE_ES;
		String billCode = binOLCM03_BL.getTicketNumber(Integer.parseInt(map.get("organizationInfoId").toString()), 
				Integer.parseInt(map.get("brandInfoId").toString()), "", billType);
		// 业务类型
		mqInfoDTO.setBillType(billType);
		// 单据号
		mqInfoDTO.setBillCode(billCode);
		// 消息发送队列名
		mqInfoDTO.setMsgQueueName(CherryConstants.CHERRYEVENTSCHEDULEMSGQUEUE);
		
		
		// 设定消息内容
		Map<String,Object> msgDataMap = new HashMap<String,Object>();
		// 设定消息版本号
		msgDataMap.put("Version", CherryConstants.MESSAGE_VERSION_ES);
		// 设定消息命令类型
		msgDataMap.put("Type", CherryConstants.MESSAGE_TYPE_1007);
		// 设定消息数据类型
		msgDataMap.put("DataType", CherryConstants.DATATYPE_APPLICATION_JSON);
		// 设定消息的数据行
		Map<String,Object> dataLine = new HashMap<String,Object>();
		// 消息的主数据行
		Map<String,Object> mainData = new HashMap<String,Object>();
		mainData.put("BrandCode", map.get("brandCode"));
		mainData.put("TradeType", billType);
		mainData.put("TradeNoIF", billCode);
		mainData.put("EventType", "6");
		mainData.put("EventId", map.get("batchNo"));
		mainData.put("EventDate", map.get("sysDate"));
		mainData.put("Sourse", "BINOLMBMBM15");
		mainData.put("OldCounterCode", map.get("oldCounterCode"));
		mainData.put("NewCounterCode", map.get("newCounterCode"));
		dataLine.put("MainData", mainData);
		msgDataMap.put("DataLine", dataLine);
		mqInfoDTO.setMsgDataMap(msgDataMap);
		
		// 设定插入到MongoDB的信息
		DBObject dbObject = new BasicDBObject();
		// 组织代码
		dbObject.put("OrgCode", map.get("orgCode"));
		// 品牌代码
		dbObject.put("BrandCode", map.get("brandCode"));
		// 业务类型
		dbObject.put("TradeType", billType);
		// 单据号
		dbObject.put("TradeNoIF", billCode);
		// 修改次数
		dbObject.put("ModifyCounts", "0");
		mqInfoDTO.setDbObject(dbObject);
		
		// 发送MQ消息
		binOLMQCOM01_BL.sendMQMsg(mqInfoDTO);
	}
	
	/**
	 * 根据转柜台批次号查询转柜信息
	 * 
	 * @param map 查询条件
	 * @return 转柜信息
	 */
	public Map<String, Object> getMemInfoRecordInfo(Map<String, Object> map) {
		
		// 根据转柜台批次号查询任意一个会员修改履历信息
		Map<String, Object> memInfoRecordInfo = binOLMBMBM15_Service.getMemInfoRecordInfo(map);
		if(memInfoRecordInfo != null) {
			// 根据转柜台批次号查询转柜会员总数
			int count = binOLMBMBM15_Service.getMemInfoRecordCount(map);
			memInfoRecordInfo.put("moveCount", count);
		}
		return memInfoRecordInfo;
	}

}
