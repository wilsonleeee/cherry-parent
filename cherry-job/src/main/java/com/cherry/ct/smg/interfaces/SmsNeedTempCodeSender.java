/*	
 * @(#)SmsNeedTempCodeSender.java     1.0 2016/04/28
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
package com.cherry.ct.smg.interfaces;

import com.cherry.ct.smg.dto.SendResult;

/**
 * 短信发送者(需要指定第三方短信模板)
 * 
 * @author hub
 * @version 1.0 2016.04.28
 */
public interface SmsNeedTempCodeSender {
	
	/**
	 * 
	 * 发送短信
	 * 
	 * @param phones 手机号码，多个号码英文逗号分隔
	 * @param signName 短信签名
	 * @param templateCode 模板编号
	 * @param Params 模板里的内容
	 * 
	 * @return SendResult 短信发送结果
	 * 
	 */
	public SendResult smsSend(String phones, String signName, String templateCode, String params); 
}
