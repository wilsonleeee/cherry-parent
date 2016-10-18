package com.cherry.ct.common.bl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.activemq.dto.MQInfoDTO;
import com.cherry.cm.activemq.interfaces.BINOLMQCOM01_IF;
import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ct.common.interfaces.BINOLCTCOM07_IF;
import com.cherry.ct.common.service.BINOLCTCOM07_Service;
import com.cherry.mq.mes.common.MessageConstants;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class BINOLCTCOM07_BL implements BINOLCTCOM07_IF{

	@Resource
	private BINOLCTCOM07_Service binolctcom07_Service;
	
	@Resource
	private BINOLMQCOM01_IF binOLMQCOM01_BL;
	
	/** 取得各种业务类型的单据流水号 */
	@Resource
	private BINOLCM03_BL binOLCM03_BL;
	
	@Override
	public void tran_sendMsg(Map<String, Object> map) throws Exception {
		String brandCode = ConvertUtil.getString(map.get("brandCode"));
		String orgCode = ConvertUtil.getString(map.get("orgCode"));
		String customerSysId = ConvertUtil.getString(map.get("customerSysId"));
		String memberCode = ConvertUtil.getString(map.get("memberCode"));
		String mobilePhone = ConvertUtil.getString(map.get("mobilePhone"));
		String contents = ConvertUtil.getString(map.get("contents"));
		String sourse = ConvertUtil.getString(map.get("sourse"));
		
		if("".equals(customerSysId)){
			if(!"".equals(memberCode)){
				customerSysId = binolctcom07_Service.getMemberIdByCode(memberCode);
				if("".equals(customerSysId)){
					customerSysId = mobilePhone;
				}else{
					customerSysId = customerSysId + "&" + mobilePhone;
				}
			}else{
				customerSysId = mobilePhone;
			}
		}else{
			customerSysId = customerSysId + "&" + mobilePhone;
		}
		
		String sysTime = binolctcom07_Service.getSYSDate();
		// 发送MQ
		MQInfoDTO mqInfoDTO = new MQInfoDTO();
		// 品牌代码
		mqInfoDTO.setBrandCode(brandCode);
		
		String billType = CherryConstants.MESSAGE_TYPE_ES;
		
		String billCode = binOLCM03_BL.getTicketNumber(Integer.parseInt(ConvertUtil.getString(map.get("organizationInfoId"))), 
				Integer.parseInt(ConvertUtil.getString(map.get("brandInfoId"))), "", billType);
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
		// 事件类型
		mainData.put("EventType", CherryConstants.SENDMSGEVENTTYPE);
		// 事件ID
		mainData.put("EventId", customerSysId);
		// 沟通内容
		mainData.put("MessageContents", contents);
		// 沟通时间
		mainData.put("EventDate", sysTime);
		// 数据来源
		mainData.put("Sourse", sourse);
		
		dataLine.put("MainData", mainData);
		
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
		// 事件类型
		dbObject.put("EventType", CherryConstants.SENDMSGEVENTTYPE);
		// 事件ID
		dbObject.put("EventId", customerSysId);
		// 沟通内容
		dbObject.put("MessageContents", contents);
		// 沟通时间
		dbObject.put("EventDate", sysTime);
		// 数据来源
		dbObject.put("Sourse", sourse);
		mqInfoDTO.setDbObject(dbObject);
		
		// 发送MQ消息
		binOLMQCOM01_BL.sendMQMsg(mqInfoDTO, false);
	}

	@Override
	public Map<String, Object> getMemberInfoById(Map<String, Object> map)
			throws Exception {
		Map<String, Object> memberInfoMap = binolctcom07_Service.getMemberInfoById(map);
		return memberInfoMap;
	}

	@Override
	public String getMemberIdByCode(String memberCode) throws Exception {
		String memberInfoId = binolctcom07_Service.getMemberIdByCode(memberCode);
		return memberInfoId;
	}
}
