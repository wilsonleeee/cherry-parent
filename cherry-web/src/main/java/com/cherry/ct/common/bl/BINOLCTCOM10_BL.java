package com.cherry.ct.common.bl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.activemq.dto.MQInfoDTO;
import com.cherry.cm.activemq.interfaces.BINOLMQCOM01_IF;
import com.cherry.cm.cmbussiness.bl.BINOLCM03_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.ct.common.interfaces.BINOLCTCOM10_IF;
import com.cherry.ct.common.service.BINOLCTCOM04_Service;
import com.cherry.mq.mes.common.MessageConstants;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class BINOLCTCOM10_BL implements BINOLCTCOM10_IF{
	
	@Resource
	private BINOLCTCOM04_Service binolctcom04_Service;
	
	@Resource
	private BINOLMQCOM01_IF binOLMQCOM01_BL;
	
	/** 取得各种业务类型的单据流水号 */
	@Resource
	private BINOLCM03_BL binOLCM03_BL;
	
	@Override
	public void tran_sendWPMsg(Map<String, Object> map) throws Exception {
		String brandCode = ConvertUtil.getString(map.get("brandCode"));
		String orgCode = ConvertUtil.getString(map.get("orgCode"));
		String messageType = ConvertUtil.getString(map.get("messageType"));
		String contents = ConvertUtil.getString(map.get("contents"));
		String smsChannel=ConvertUtil.getString(map.get("smsChannel"));
		String eventType=ConvertUtil.getString(map.get("eventType"));
		String resCodeText = ConvertUtil.getString(map.get("resCodeList"));
		resCodeText = resCodeText.replace("；", ";");
		resCodeText = resCodeText.replace("，", ";");
		resCodeText = resCodeText.replace(",", ";");
		String[] resCodes = resCodeText.split(";");
		for (String resCode : resCodes) {
			if(!"".equals(resCode) && resCode.length()>0){
				if("1".equals(messageType)){
				
					String sysTime = binolctcom04_Service.getSYSDate();
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
					mainData.put("EventType", eventType);
					// 事件ID
					mainData.put("EventId", resCode);
//					// 沟通内容(非大鱼的不需要添加)
//					mainData.put("MessageContents", contents);
					// 沟通时间
					mainData.put("EventDate", sysTime);
					// 数据来源
					mainData.put("Sourse", "Cherry");
					//短信通道类型
					mainData.put("smsChannel", smsChannel);
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
					dbObject.put("EventType", eventType);
					// 事件ID
					dbObject.put("EventId", resCode);
					// 沟通内容
					dbObject.put("MessageContents", contents);
					// 沟通时间
					dbObject.put("EventDate", sysTime);
					// 数据来源
					dbObject.put("Sourse", "Cherry");
					mqInfoDTO.setDbObject(dbObject);
					
					// 发送MQ消息
					binOLMQCOM01_BL.sendMQMsg(mqInfoDTO, false);
				}else if("2".equals(messageType)){
					// 发送邮件
				}
			}
		}
	}
	
	public String toUtf8String(String s) throws Exception{  
		if (s == null || s.equals("")){  
			return "";
		}
		StringBuffer sb = new StringBuffer();
		try {
			char c;  
			for(int i = 0; i < s.length(); i++){
				c = s.charAt(i);
				if (c >= 0 && c <= 255){
					sb.append(c);
				}else{
					byte[] b;
					b = Character.toString(c).getBytes("utf-8");
					for (int j = 0; j < b.length; j++) {
						int k = b[j];
						if (k < 0)
							k += 256;
						sb.append("%" + Integer.toHexString(k).toUpperCase());
					}
				}
			}
		}catch (Exception e){
			throw e;
		}
		return sb.toString();
	}
}
