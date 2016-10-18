/*	
 * @(#)BINBEDRCOM02_IF.java     1.0 2011/09/02	
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
package com.cherry.dr.cmbussiness.interfaces;

import com.cherry.dr.cmbussiness.dto.mq.MQLogDTO;

/**
 * MQ收发日志表共通处理 IF
 * 
 * @author hub
 * @version 1.0 2011.09.02
 */
public interface BINBEDRCOM02_IF {
	
	/**
	 * 保存并发送MQ消息
	 * 
	 * @param mqLogDTO
	 * 				MQ收发日志DTO
	 * @throws Exception 
	 */
	public void saveAndSendMsg(MQLogDTO mqLogDTO) throws Exception;

}
