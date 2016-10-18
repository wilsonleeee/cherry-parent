/*	
 * @(#)BINOLMQCOM01_IF.java     1.0 2011/12/14
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
package com.cherry.cm.activemq.interfaces;

import com.cherry.cm.activemq.dto.MQInfoDTO;

/**
 * 发送MQ消息共通处理 IF
 * 
 * @author WangCT
 * @version 1.0 2011/12/14
 */
public interface BINOLMQCOM01_IF {
	
	/**
	 * 
	 * 发送MQ消息处理（默认保存消息记录到数据库MQ收发日志表）
	 * 
	 * @param mqInfoDTO MQ消息 DTO
	 * @throws Exception 
	 */
	public void sendMQMsg(MQInfoDTO mqInfoDTO) throws Exception;
	
	/**
	 * 
	 * 发送MQ消息处理
	 * 
	 * @param mqInfoDTO MQ消息 DTO
	 * @param saveLog 是否保存消息到数据库MQ收发日志表，true：保存，false：不保存
	 * @throws Exception 
	 */
	public void sendMQMsg(MQInfoDTO mqInfoDTO, boolean saveLog) throws Exception;

}
