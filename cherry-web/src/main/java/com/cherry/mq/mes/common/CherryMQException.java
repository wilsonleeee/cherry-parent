/*		
 * @(#)CherryMQException.java     1.0 2010/12/29		
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
package com.cherry.mq.mes.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MQ消息异常
 * @author huzude
 *
 */
@SuppressWarnings("serial")
public class CherryMQException extends Exception{
	
	private static final Logger logger = LoggerFactory.getLogger(CherryMQException.class);
	
	private String errMsg;

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	
	public String getMessage(){
		return errMsg;
	}
	
	public CherryMQException(String errMsg){
		this.setErrMsg(errMsg);
		logger.debug(errMsg);
	}
	
}
