/*		
 * @(#)CherryMessageReceiver_IF.java     1.0 2012.04.28
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
package com.cherry.mq.mes.interfaces;

/**
 * 接收MQ消息接口
 * 
 * @author WangCT
 * @version 1.0 2012.04.28
 */
public interface CherryMessageReceiver_IF {
	
	/**
	 * 接收MQ消息处理（接收老后台到新后台的MQ消息）
	 * 
	 * @param msg MQ消息
	 * @throws Exception 
	 */
    public void handleMessage(String msg) throws Exception;
    
    /**
	 * 接收MQ消息处理（新后台内部接收MQ消息用）
	 * 
	 * @param msg MQ消息
	 * @throws Exception 
	 */
    public void receiveMessage(String msg) throws Exception;
}
