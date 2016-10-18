/*	
 * @(#)BINBEDRPOI02_BL.java     1.0 2013/04/17
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
package com.cherry.dr.point.bl;

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
import com.cherry.dr.cmbussiness.dto.core.CampBaseDTO;
import com.cherry.dr.cmbussiness.interfaces.CampRuleExec_IF;
import com.cherry.dr.cmbussiness.service.BINBEDRCOM01_Service;
import com.cherry.dr.cmbussiness.util.DroolsMessageUtil;
import com.cherry.mq.mes.common.MessageConstants;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * 换卡扣积分处理 BL
 * 
 * @author hub
 * @version 1.0 2013.04.17
 */
public class BINBEDRPOI02_BL implements CampRuleExec_IF{
	
	@Resource(name="binOLMQCOM01_BL")
	private BINOLMQCOM01_IF binOLMQCOM01_BL;
	
	@Resource
	private BINBEDRCOM01_Service binbedrcom01_Service;
	
	@Resource(name="binOLCM03_BL")
	private BINOLCM03_BL binOLCM03_BL;
	
	/**
	 * 换卡扣积分处理执行
	 * 
	 * @param campBaseDTO
	 *            会员实体
	 * 
	 * @throws Exception
	 * 
	 */
	@Override
	public void ruleExec(CampBaseDTO campBaseDTO) throws Exception {
		//积分维护明细数据
		List<Map<String,Object>> detailDataList = new ArrayList<Map<String,Object>>();
		Map<String,Object> detailMap = new HashMap<String,Object>();
		//会员卡号
		detailMap.put("MemberCode", campBaseDTO.getMemCode());
		//修改的积分
		detailMap.put("ModifyPoint", "-200");
		//业务时间
		String ticketDateStr = campBaseDTO.getTicketDate();
		if (null == ticketDateStr) {
			ticketDateStr = binbedrcom01_Service.getForwardSYSDate();
		}
		detailMap.put("BusinessTime", ticketDateStr);
		//备注
		String reason = DroolsMessageUtil.getMessage(
				DroolsMessageUtil.IDR00003, new String[] {campBaseDTO.getBillId()});
		detailMap.put("Reason", reason);
		//员工Code
		detailMap.put("EmployeeCode", campBaseDTO.getEmployeeCode());
		detailDataList.add(detailMap);
		//设定MQ消息DTO
		MQInfoDTO mqInfoDTO = new MQInfoDTO();
		// 品牌代码
		String brandCode = campBaseDTO.getBrandCode();
		mqInfoDTO.setBrandCode(brandCode);
		// 组织代码
		String orgCode = campBaseDTO.getOrgCode();
		mqInfoDTO.setOrgCode(orgCode);
		// 组织ID
		mqInfoDTO.setOrganizationInfoId(campBaseDTO.getOrganizationInfoId());
		// 品牌ID
		mqInfoDTO.setBrandInfoId(campBaseDTO.getBrandInfoId());
		//单据类型
		String billType = CherryConstants.MESSAGE_TYPE_PT;;
		//单据号
		String billCode = binOLCM03_BL.getTicketNumber(campBaseDTO.getOrganizationInfoId(), 
				campBaseDTO.getBrandInfoId(), "", billType);
		// 业务类型
		mqInfoDTO.setBillType(billType);
		// 单据号
		mqInfoDTO.setBillCode(billCode);
		// 消息发送队列名
		mqInfoDTO.setMsgQueueName(CherryConstants.CHERRYPOINTMSGQUEUE);
		// 设定消息内容
		Map<String,Object> msgDataMap = new HashMap<String,Object>();
		// 设定消息版本号
		msgDataMap.put("Version", CherryConstants.MESSAGE_VERSION_PT);
		// 设定消息命令类型
		msgDataMap.put("Type", CherryConstants.MESSAGE_TYPE_1004);
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
		// 数据来源
		mainData.put("Sourse", ConvertUtil.getString(campBaseDTO.getChannel()));
		//修改模式
		mainData.put("SubTradeType","2");
		dataLine.put("MainData", mainData);
		//积分明细
		dataLine.put("DetailDataDTOList", detailDataList);
		msgDataMap.put("DataLine", dataLine);
		mqInfoDTO.setMsgDataMap(msgDataMap);
		// 设定插入到MongoDB的信息
		DBObject dbObject = new BasicDBObject();
		// 组织代码
		dbObject.put("OrgCode", orgCode);
		// 品牌代码
		dbObject.put("BrandCode", brandCode);
		// 业务类型
		dbObject.put("TradeType", billType);
		// 单据号
		dbObject.put("TradeNoIF", billCode);
		// 数据来源
		dbObject.put("Sourse", "Cherry");
		mqInfoDTO.setDbObject(dbObject);
		// 发送MQ消息
		binOLMQCOM01_BL.sendMQMsg(mqInfoDTO);
	}

	@Override
	public void beforExec(CampBaseDTO campBaseDTO) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterExec(CampBaseDTO campBaseDTO) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
