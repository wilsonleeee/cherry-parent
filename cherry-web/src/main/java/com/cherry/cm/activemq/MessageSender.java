/*	
 * @(#)MessageSender.java     1.0 2010/12/01		
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
package com.cherry.cm.activemq;

import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

/**
 * ActiveMQ消息发送类
 * @author dingyc
 *
 */
public class MessageSender {
	private static final Logger logger = LoggerFactory.getLogger(MessageSender.class);
	
	public void sendMessageTest(String msg){
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.101.91:61616");   
				   Connection connection = null;   
				   Session session=null;   
				   try {   
				           
				        connection = (Connection) connectionFactory.createConnection();//创建连接   
				        session = (Session) connection.createSession(false,   
				                Session.AUTO_ACKNOWLEDGE);//创建会话   
				        Destination destination = session.createQueue("cherryMsgQueue");   
				        MessageProducer producer = session.createProducer(destination);   
				        TextMessage message = session.createTextMessage("哈哈哈哈哈哈哈安徽");   
				       message.setStringProperty("headname", "remoteB");   
				       producer.send(message);  
				      
				       connection.close();   
				    } catch (Exception e) {   
				        e.printStackTrace();   
				    }finally{   
				        try {   
				           if(session!=null){   
				               session.close();   
				           }   
			          if(connection!=null){   
				               connection=null;   
				          }   
				       } catch (Exception e) {   
				            
				     }   
				 }   
	}
	@Resource
	private JmsTemplate jmsTemplate;
	public void sendMessage(String msg){ 
		//jmsTemplate.getConnectionFactory().
//		jmsTemplate.send( new MessageCreator(){   
//		                public Message createMessage(Session session) throws JMSException {   
//		                	
//		                    return session.createTextMessage();   
//			//msg
//		               }   
//		 });
		try{
		jmsTemplate.send( new myMessageCreator(msg));
		}catch(Exception ex){
			
		}
	}
	
	/**
	 * 发送MQ消息
	 * 
	 * @param msg 发送的消息体
	 * @param desName 发送的目的队列
	 * @author dingyongchang
	 * dingyongchang M 2013-04-25 强制加入默认的分组ID，"000000000"
	 *
	 */
	public void sendMessage(String msg, String desName){

		try {
			jmsTemplate.send(desName, new myMessageCreator(msg));
		} catch (Exception e) {
			
		}
	}	
	
	class myMessageCreator implements MessageCreator{
		private String message;
		public myMessageCreator(String msg){
			
			this.message = msg;
		}
		public Message createMessage(Session session) throws JMSException {
			TextMessage tmpmsg = session.createTextMessage(message);
			tmpmsg.setStringProperty("JMSXGroupID","000000000");
			return tmpmsg;
		}
	}
	

	/**发送带有消息分组的MQ
	 * @param msg 消息体
	 * @param desName 队列名
	 * @param groupName 分组ID，为空则转换为"000000000"
	 * @author dingyongchang
	 * dingyongchang C 2013-04-23
	 * dingyongchang M 2013-04-25 将默认的分组ID从"0"转变为"000000000"
	 */
	public void sendGroupMessage(String msg, String desName,String groupName){
		try {
			jmsTemplate.send(desName, new groupMessageCreator(msg,groupName));
		} catch (Exception e) {
			logger.error("MQ发送发生异常，异常信息：" + e.getMessage(),e);
		}
	}
	
	class groupMessageCreator implements MessageCreator{
		private String msgStr;
		private String group;
		public groupMessageCreator(String msg,String groupName){
			
			this.msgStr = msg;
			this.group = groupName;
		}
		public Message createMessage(Session session) throws JMSException {  
			TextMessage message = session.createTextMessage(msgStr);
			message.setStringProperty("JMSXGroupID", null==group||"".equals(group)?"000000000":group);
			return message; 
		}
	}
}
