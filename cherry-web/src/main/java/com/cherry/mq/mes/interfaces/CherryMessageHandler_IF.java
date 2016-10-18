/*	
 * @(#)CherryMessageHandler_IF.java     1.0 2012.04.28	
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

import java.util.Map;

/**
 * 处理MQ消息接口
 * 
 * @author WangCT
 * @version 1.0 2012.04.28
 */
public interface CherryMessageHandler_IF {
	
	/**
	 * 接收MQ消息处理
	 * 
	 * @param map 消息信息
	 * @throws Exception 
	 */
    public void handleMessage(Map<String, Object> map) throws Exception;

}
