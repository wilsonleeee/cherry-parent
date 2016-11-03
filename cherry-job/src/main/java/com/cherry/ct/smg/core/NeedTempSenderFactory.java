/*	
 * @(#)NeedTempSenderFactory.java     1.0 2016/04/28
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

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.ct.smg.interfaces.SmsNeedTempCodeSender;

/**
 * 短信发送者工厂类(需要指定第三方短信模板)
 * 
 * @author hub
 * @version 1.0 2016.04.28
 */
public class NeedTempSenderFactory {
	
	private static final Logger logger = LoggerFactory
			.getLogger(NeedTempSenderFactory.class);
	
	/** 保存使用的短信供应商 */
	private static Map<String, SmsNeedTempCodeSender> senderContainer = new HashMap<String, SmsNeedTempCodeSender>();
	
	/** 注册的短信供应商 */
	public static enum REGISTSENDERS {
		/** 阿里大鱼 */
		ALIDAYU(CherryBatchConstants.ALIDAYU_SYSTEMCODE, AliDayuSender.class);
		
		private static boolean isRegister(String key) {
			REGISTSENDERS[] opers = REGISTSENDERS.values();
			for(REGISTSENDERS oper : opers) {
				if(key.equals(oper.getKey())) {
					return true;
				}
			}
			return false;
		}
		private static Class<?> getSender(String key) {
			REGISTSENDERS[] opers = REGISTSENDERS.values();
			for(REGISTSENDERS oper : opers) {
				if(key.equals(oper.getKey())) {
					return oper.getClz();
				}
			}
			return null;
		}
		REGISTSENDERS(String key, Class<?> content) {
			this.key = key;
			this.clz = content;
		}
		private String getKey() {
			return key;
		}
		private Class<?> getClz() {
			return clz;
		}
		private String key;
		private Class<?> clz;
	}
	
	/**
	 * 
	 * 验证短信供应商是否注册
	 * 
	 * @param senderCode 需要验证的短信供应商代码
	 * 
	 * @return boolean true:已经注册 false:未注册
	 * 
	 */
	public static boolean isRegister(String senderCode) {
		return REGISTSENDERS.isRegister(senderCode);
	}
	
	/**
     * 创建一个短信发送者对象
     * 
     * @param senderCode 短信供应商代码
     * @return SmsNeedTempCodeSender 短信发送者
	 * @throws Exception 
     */
	public static SmsNeedTempCodeSender createSender(String senderCode) throws Exception {
		// 从容器中获取
		SmsNeedTempCodeSender sender = senderContainer.get(senderCode);
		if (null == sender) {
			// 非该工厂注册的供应商
			if (!isRegister(senderCode)) {
				return null;
			}
			synchronized (senderContainer) {
				sender = senderContainer.get(senderCode);
				if (null == sender) {
					try {
						// 创建对象
						sender = (SmsNeedTempCodeSender) REGISTSENDERS.getSender(senderCode).newInstance();
						// 将对象加入容器
						senderContainer.put(senderCode, sender);
					} catch (Exception e) {
						logger.error("实列化短信发送者对象时发生异常：" + e.getMessage(),e);
						throw e;
					}
				}
			}
		}
		return sender;
	}
}
