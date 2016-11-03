/*	
 * @(#)BINBEMBTIF06_FN.java     1.0 2015/06/24
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
package com.cherry.mb.tif.function;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.mb.tif.bl.BINBEMBTIF06_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

/**
 * 天猫历史注册会员转假登录会员处理FN
 * 
 * @author hub
 * @version 1.0 2015/06/24
 */
public class BINBEMBTIF06_FN implements FunctionProvider{
	
	private static Logger logger = LoggerFactory.getLogger(BINBEMBTIF06_FN.class.getName());
	
	/** 天猫历史注册会员转假登录会员处理BL */
	@Resource
	private BINBEMBTIF06_BL binBEMBTIF06_BL;
	
	@Override
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		try {
			logger.info("******************************天猫历史注册会员转假登录会员处理开始***************************");
			int flag = binBEMBTIF06_BL.tran_HisRegister(transientVars);
			ps.setInt("result", flag);
		} catch (Exception e) {
			// 打印错误信息
			logger.error(e.getMessage(),e);
			throw new WorkflowException();
		} finally {
			logger.info("******************************天猫历史注册会员转假登录会员处理结束***************************");
		}
	}

}

