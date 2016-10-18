/*		
 * @(#)BINOLCM98_BL.java     1.0 2013/12/06          	
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
package com.cherry.cm.cmbussiness.bl;

import javax.annotation.Resource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.annota.TimeLog;

/**
 * 共通LOG输出
 * 
 * @author hub
 */
@Aspect
public class BINOLCM98_BL {
	
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	
	private static Logger logger = LoggerFactory
			.getLogger(BINOLCM98_BL.class.getName());
	
	private boolean isLog = false;
	
	/**
	 * 查询是否需要打印日志
	 * 
	 * @param orgIdStr
	 *            组织ID
	 * @param brandIdStr
	 *            品牌ID
	 */
	public void logConfig(String orgIdStr, String brandIdStr) {
		if ("1".equals(binOLCM14_BL.getConfigValue("1115", orgIdStr, brandIdStr))) {
			isLog = true;
		} else if(isLog) {
			isLog = false;
		}
	}
	
	@Around("(within(com.cherry.mq..*) || within(com.cherry.cm.cmbussiness..*)) && @annotation(rl)")    
    public Object addLogSuccess(ProceedingJoinPoint jp, TimeLog rl) throws Throwable{
		Object result = null;
		if (!isLog) {
			result = jp.proceed();
		} else {
			long startTime = System.currentTimeMillis();
			result = jp.proceed();
			// 运行时间
			long time = System.currentTimeMillis() - startTime;
			//获取类名    
			String className = jp.getTarget().getClass().toString();
			className = className.substring(className.indexOf("com"));  
			//获取方法名  
			String signature = jp.getSignature().toString();
			String methodName = signature.substring(signature.lastIndexOf(".")+1, signature.indexOf("("));
			StringBuilder bd = new StringBuilder();
			bd.append("ClassName: ").append(className).append("****** Method: ").append(methodName)
			.append("****** Method invocation time: ").append(time).append("ms");
			// 打印日志
			logger.info(bd.toString());
		}
		return result;
	}
	
	/**
	 * 输出日志(通过配置项控制是否需要写入日志文件)
	 * 
	 * @param orgIdStr
	 *            组织ID
	 * @param brandIdStr
	 *            品牌ID
	 */
	public void logOut(String msg) {
		if (isLog) {
			// 打印日志
			logger.info(msg);
		}
	}
}
