/*	
 * @(#)BINBEDRHAN04_FN.java     1.0 2013/05/13
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
package com.cherry.dr.handler.function;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.dr.handler.bl.BINBEDRHAN04_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

/**
 * 积分清零处理FN
 * 
 * @author hub
 * @version 1.0 2013.05.13
 */
public class BINBEDRHAN04_FN implements FunctionProvider{
private static Logger logger = LoggerFactory.getLogger(BINBEDRHAN04_FN.class.getName());
	
	/** 降级处理BL */
	@Resource
	private BINBEDRHAN04_BL binBEDRHAN04_BL;
	
	@Override
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		try {
			logger.info("******************************会员积分清零处理BATCH处理开始***************************");
			int flag = binBEDRHAN04_BL.tran_pointClear(transientVars);
			ps.setInt("result", flag);
		} catch (Exception e) {
			// 打印错误信息
			logger.error(e.getMessage(),e);
			throw new WorkflowException();
		} finally {
			logger.info("******************************会员积分清零处理BATCH处理结束***************************");
		}
		
	}
}
