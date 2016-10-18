package com.cherry.cm.activemq.bl;

import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.activemq.MessageSender;
import com.cherry.cm.activemq.interfaces.BINOLMQCOM02_IF;
import com.cherry.cm.activemq.service.BINOLMQCOM02_Service;
import com.cherry.cm.activemq.util.MessageUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.mq.mes.common.CherryMQException;
import com.cherry.mq.mes.common.MessageConstants;

public class BINOLMQCOM02_BL implements BINOLMQCOM02_IF{
	
	@Resource(name="binOLMQCOM02_Service")
	private BINOLMQCOM02_Service binOLMQCOM02_Service;

	@Resource(name="messageSender")
	private MessageSender messageSender;
	
	@Override
	public boolean sendData(Map<String, Object> map, String queueName)
			throws Exception {
		return this.sendData(map, queueName, null);
	}

	@Override
	public boolean sendData(Map<String, Object> map, String queueName,
			String groupName) throws Exception {
		
		String dataType = ConvertUtil.getString(map.get("DataType"));
	    
		//将map中的数据组装成MQ消息体
		String mqMessage = null;
		if(dataType.equals(MessageConstants.DATATYPE_APPLICATION_JSON)){
		    mqMessage = MessageUtil.getMQMessageJSON(map);
		}else{
		    mqMessage = MessageUtil.getMQMessage(map);
		}
		
		if(null == mqMessage){
			return false;
		}
		if(queueName == null || "".equals(queueName)) {
			queueName = "posToCherryMsgQueue";
		}
		
		//将MQ消息体设定到MQ_Log日志中
		@SuppressWarnings("unchecked")
		Map<String,Object> MQ_Log = (Map<String, Object>) map.get("Mq_Log");
		MQ_Log.put("OriginalMsg", mqMessage);
		if(groupName != null && !"".equals(groupName)) {
			MQ_Log.put("JMSXGroupID", groupName);
		}
		
		//调用共通写MQ_Log日志表 
		String insertResult = this.insertMQ_Log(MQ_Log);
		if(!"SUCCESS".equals(insertResult)){
			throw new Exception("MQHelper模块在插入MQ_Log时出错，没有成功插入！" + insertResult);
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
	
	/**
	 * 在进行MQLog日志写入前期做验证
	 * 
	 * 
	 * */
	public String insertMQ_Log(Map<String,Object> mqMap) throws Exception{
		
		//检验单据类型
		if(null == mqMap.get("BillType")|| "".equals(mqMap.get("BillType"))){
			throw new CherryMQException("MQHelper共通在验证写MQlog日志的共通时出错，原因是:【BillType】字段为空！");
		}
		
		//检验单据号
		if(null == mqMap.get("BillCode")|| "".equals(mqMap.get("BillCode"))){
			throw new CherryMQException("MQHelper共通在验证写MQlog日志的共通时出错，原因是:【BillCode】字段为空！");
		}
		
		//如果是销售数据修改回溯一定要带上
		if(MessageConstants.MSG_TRADETYPE_SALE.equals(mqMap.get("BillType")) && (null == mqMap.get("ModifyCounts")|| "".equals(mqMap.get("ModifyCounts")))){
			throw new CherryMQException("MQHelper共通在验证写MQlog日志的共通时出错，原因是:"+mqMap.get("BillType")+"业务的消息体没有带上销售回溯！");
		}
		
		//验证消息体
		if(null == mqMap.get("OriginalMsg")|| "".equals(mqMap.get("OriginalMsg"))){
			throw new CherryMQException("MQHelper共通在验证写MQlog日志的共通时出错，原因是:【OriginalMsg】字段为空！");
		}
		
		//验证收发数据方标志
		if(null == mqMap.get("SendOrRece")|| "".equals(mqMap.get("SendOrRece"))){
			mqMap.put("SendOrRece","S");
		}
		
		//验证消息源
		if(null == mqMap.get("Source")|| "".equals(mqMap.get("Source"))){
			throw new CherryMQException("MQHelper共通在验证写MQlog日志的共通时出错，原因是:【Source】字段为空！");
		}
		
		mqMap.put("Result", "SUCCESS");
		Map<String,Object> resultMap = binOLMQCOM02_Service.insertMQ_Log(mqMap);
		String result = ConvertUtil.getString(resultMap.get("Result"));
		
		return result;
	}

}
