package com.mqhelper.impl;

import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.activemq.MessageSender;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mq.mes.common.MessageConstants;
import com.mqhelper.common.bl.BINBEMQHELPERCM99_BL;
import com.mqhelper.common.util.MessageUtil;
import com.mqhelper.interfaces.MQHelper_IF;

/**
 * 发送纯JSON
 * @author Wangmz
 *
 */
public class JSONMQHelperImpl implements MQHelper_IF {

	@Resource
	private BINBEMQHELPERCM99_BL binBEMQHELPERCM99_BL;
	
	/** ActiveMQ消息发送类 */
	@Resource
	private MessageSender messageSender;
	
	@Override
	public boolean sendData(Map<String, Object> map, String queueName)
			throws Exception {
		return this.sendData(map, queueName, null);
	}

	@Override
	public boolean sendData(Map<String, Object> map, String queueName,
			String groupName) throws Exception {
		
		//将map中的数据组装成MQ消息体
		String mqMessage = null;
		
		mqMessage = CherryUtil.map2Json(map);
		if(null == mqMessage){
			return false;
		}
		if(queueName == null || "".equals(queueName)) {
			queueName = "posToCherryMsgQueueJSON";
		}
		
		//将MQ消息体设定到MQ_Log日志中
		Map<String,Object> MQ_Log = (Map<String, Object>) map.get("Mq_Log");
		MQ_Log.put("OriginalMsg", mqMessage);
		if(groupName != null && !"".equals(groupName)) {
			MQ_Log.put("JMSXGroupID", groupName);
		}
		
		//调用共通写MQ_Log日志表 
		int mqLogId = 0;
		mqLogId = binBEMQHELPERCM99_BL.insertMQ_Log(MQ_Log);
		if(mqLogId == 0){
			throw new Exception("MQHelper模块在插入MQ_Log时出错，没有成功插入！");
		}
		
		if(groupName != null && !"".equals(groupName)) {
			//往指定的MQ消息队列中发送消息
			messageSender.sendGroupMessage(mqMessage, queueName, groupName);
		} else {
			//往指定的MQ消息队列中发送消息
			messageSender.sendMessage(mqMessage, queueName);
		}
			
		return true;
		
		
	}

}
