/*	
 * @(#)AliDayuSender.java     1.0 2016/04/28
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
package com.cherry.ct.smg.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.ct.smg.dto.SendResult;
import com.cherry.ct.smg.interfaces.SmsNeedTempCodeSender;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;

/**
 * 阿里大鱼短信供应商
 * 
 * @author hub
 * @version 1.0 2016.04.28
 */
public class AliDayuSender implements SmsNeedTempCodeSender{
	
	private static final Logger logger = LoggerFactory
			.getLogger(AliDayuSender.class);
	
	public static TaobaoClient client = null;
	
	static {
		client = createTaobaoClient();
	}
	
	/**
	 * 
	 * 创建短信客户端
	 * 
	 * 
	 * @return TaobaoClient 短信客户端
	 * 
	 */
	private static TaobaoClient createTaobaoClient() {
		if (null == client) {
			try {
				client = new DefaultTaobaoClient(PropertiesUtil.pps.getProperty("DayuUrl"),
				PropertiesUtil.pps.getProperty("DayuAppkey"), 
				PropertiesUtil.pps.getProperty("DayuSecret"));
			} catch (Exception e) {
				logger.error("创建短信客户端失败：" + e.getMessage(),e);
			}
		}
		return client;
	}
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
	public SendResult smsSend(String phones, String signName, String templateCode, String params) {
		SendResult result = new SendResult();
		AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
		req.setSmsType("normal");
		req.setSmsFreeSignName(signName);
		if (!CherryChecker.isNullOrEmpty(params)) {
			req.setSmsParamString(params);
		}
		req.setRecNum(phones);
		req.setSmsTemplateCode(templateCode);
		// 重新发送次数
		int retryTimes = CherryBatchConstants.RETRY_TIMES;
		TaobaoClient taobaoClient = createTaobaoClient();
		if (null == taobaoClient) {
			result.setResultCode(CherryBatchConstants.IFFLAG_IFCALLEXCEPTION);
			result.setErrMsg("创建短信客户端失败");
			return result;
		}
		do {
			retryTimes--;
			try {
				AlibabaAliqinFcSmsNumSendResponse rsp = taobaoClient.execute(req);
				if (rsp.isSuccess()) {
					// 短信发送成功
					result.setResultCode(CherryBatchConstants.IFFLAG_SUCCESS);
				} else {
					// 短信发送失败代码
					result.setResultCode(rsp.getSubCode());
					// 短信发送失败原因
					result.setErrMsg(rsp.getSubMsg());
				}
				break;
			} catch(Exception e) {
				if (retryTimes < 0) {
					result.setResultCode(CherryBatchConstants.IFFLAG_IFCALLEXCEPTION);
					result.setErrMsg(e.getMessage());
					break;
				} else {
					try {
						logger.error("调用接口发生异常，手机号码：" + phones + "异常信息：" + e.getMessage(),e);
						Thread.sleep(CherryBatchConstants.RETRY_TIMES);
					} catch (InterruptedException e1) {
						logger.error("线程sleep发生异常：" + e1.getMessage(),e1);
					}
				}
			}
		} while (retryTimes >= 0);
		return result;
	}

}
