/*
 * @(#)BINOLCMPL04_BL.java     1.0 2012/11/01
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
package com.cherry.cm.privilege.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.activemq.dto.MQInfoDTO;
import com.cherry.cm.activemq.interfaces.BINOLMQCOM01_IF;
import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.mongo.MongoDB;
import com.cherry.mq.mes.common.MessageConstants;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * 实时刷新数据权限BL
 * 
 * @author WangCT
 * @version 1.0 2012/11/01
 */
public class BINOLCMPL04_BL {
	
	@Resource
	private BINOLCM03_BL binOLCM03_BL;
	
	@Resource
	private BINOLMQCOM01_IF binOLMQCOM01_BL;
	
	/** 系统配置项 共通BL */
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	
	
	/**
	 * 发送实时刷新数据权限MQ消息
	 * 
	 * @param map 发送信息
	 * @throws Exception 
	 */
	public void sendRefreshPlMsg(Map<String, Object> map) throws Exception {
		
		String refreshFlag = (String)map.get("refreshFlag");
		if(refreshFlag == null || !"1".equals(refreshFlag)) {
			if(!binOLCM14_BL.isConfigOpen("1308", String.valueOf(map.get(CherryConstants.ORGANIZATIONINFOID)), String.valueOf(map.get(CherryConstants.BRANDINFOID)))) {
				return;
			}
		}
		
		// 是否刷新部门权限
		String isReOrgPl = (String)map.get("isReOrgPl");
		if(isReOrgPl == null || "".equals(isReOrgPl)) {
			isReOrgPl = "0";
		}
		// 是否刷新人员权限
		String isReEmpPl = (String)map.get("isReEmpPl");
		if(isReEmpPl == null || "".equals(isReEmpPl)) {
			isReEmpPl = "0";
		}
		// 是否刷新部门从属权限
		String isReOrgRelPl = (String)map.get("isReOrgRelPl");
		if(isReOrgRelPl == null || "".equals(isReOrgRelPl)) {
			isReOrgRelPl = "0";
		}
		// 部门权限和部门从属权限只要有一个需要刷新那么设置两个都刷新
		if("1".equals(isReOrgPl) || "1".equals(isReOrgRelPl)) {
			isReOrgPl = "1";
			isReOrgRelPl = "1";
		}
		// 刷新权限类别（0：全部刷新 1：刷新部门权限和部门从属权限 2：刷新人员权限）
		String type = "0";
		// 部门权限和人员权限都需要刷新的场合
		if("1".equals(isReOrgPl) && "1".equals(isReEmpPl)) {
			type = "0";
		} else if("1".equals(isReOrgPl) && "0".equals(isReEmpPl)) {// 部门权限需要刷新，人员权限不需要刷新的场合
			type = "1";
		} else if("1".equals(isReEmpPl) && "0".equals(isReOrgPl)) {// 人员权限需要刷新，部门权限不需要刷新的场合
			type = "2";
		} else {// 都不需要刷新的场合，不进行处理
			return;
		}
		// 查询数据权限控制表，如果刷新数据权限状态为"有一条刷新数据权限的消息等待中"，那么不发送新的刷新权限消息
		DBObject dbObject = new BasicDBObject();
		dbObject.put("OrgCode", map.get("orgCode"));
		dbObject.put("BrandCode", map.get("brandCode"));
		List<DBObject> privilegeControlList = MongoDB.findAll("PrivilegeControl", dbObject);
		if(privilegeControlList != null && !privilegeControlList.isEmpty()) {
			for(DBObject privilegeControl : privilegeControlList) {
				String _type = (String)privilegeControl.get("type");
				int status = (Integer)privilegeControl.get("status");
				// 刷新数据权限状态为"有一条刷新数据权限的消息等待中"而且刷新权限类别为全部刷新或者等于本次刷新权限类别的场合，不发送刷新权限消息
				if(status == 2 && _type != null && ("0".equals(_type) || type.equals(_type))) {
					return;
				}
			}
		}

		// 设定MQ消息DTO
		MQInfoDTO mqInfoDTO = new MQInfoDTO();
		// 品牌代码
		mqInfoDTO.setBrandCode((String)map.get("brandCode"));
		// 组织代码
		mqInfoDTO.setOrgCode((String)map.get("orgCode"));
		// 组织ID
		mqInfoDTO.setOrganizationInfoId(Integer.parseInt(map.get("organizationInfoId").toString()));
		// 品牌ID
		mqInfoDTO.setBrandInfoId(Integer.parseInt(map.get("brandInfoId").toString()));
		String billType = CherryConstants.MESSAGE_TYPE_RP;
		String billCode = binOLCM03_BL.getTicketNumber(Integer.parseInt(map.get("organizationInfoId").toString()), 
				Integer.parseInt(map.get("brandInfoId").toString()), "", billType);
		// 业务类型
		mqInfoDTO.setBillType(billType);
		// 单据号
		mqInfoDTO.setBillCode(billCode);
		// 消息发送队列名
		mqInfoDTO.setMsgQueueName(CherryConstants.CHERRYPRIVILEGEMSGQUEUE);
		
		// 设定消息内容
		Map<String,Object> msgDataMap = new HashMap<String,Object>();
		// 设定消息版本号
		msgDataMap.put("Version", CherryConstants.MESSAGE_VERSION_RP);
		// 设定消息命令类型
		msgDataMap.put("Type", CherryConstants.MESSAGE_TYPE_1005);
		// 设定消息数据类型
		msgDataMap.put("DataType", MessageConstants.DATATYPE_APPLICATION_JSON);
		// 设定消息的数据行
		Map<String,Object> dataLine = new HashMap<String,Object>();
		// 消息的主数据行
		Map<String,Object> mainData = new HashMap<String,Object>();
		// 品牌代码
		mainData.put("BrandCode", map.get("brandCode"));
		// 业务类型
		mainData.put("TradeType", billType);
		// 单据号
		mainData.put("TradeNoIF", billCode);
		// 修改次数
		mainData.put("ModifyCounts", "0");
		// 是否刷新部门权限
		mainData.put("isReOrgPl", isReOrgPl);
		// 是否刷新人员权限
		mainData.put("isReEmpPl", isReEmpPl);
		// 是否刷新部门从属权限
		mainData.put("isReOrgRelPl", isReOrgRelPl);
		dataLine.put("MainData", mainData);
		msgDataMap.put("DataLine", dataLine);
		mqInfoDTO.setMsgDataMap(msgDataMap);
		// 发送MQ消息
		binOLMQCOM01_BL.sendMQMsg(mqInfoDTO, false);
		
		DBObject update = new BasicDBObject();
		dbObject.put("type", type);
		update.put("$inc", new BasicDBObject("status", 1));
		int result = MongoDB.update("PrivilegeControl", dbObject, update);
		if(result == 0) {
			dbObject.put("status", 1);
			MongoDB.insert("PrivilegeControl", dbObject);
		}
	}

}
