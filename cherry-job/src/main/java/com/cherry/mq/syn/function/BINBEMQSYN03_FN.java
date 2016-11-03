/*
 * @(#)BINBEMQSYN03_FN.java     1.0 2010/11/04
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

package com.cherry.mq.syn.function;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.mq.syn.bl.BINBEMQSYN01_BL;
import com.cherry.mq.syn.bl.BINBEMQSYN02_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

/**
 * 重新发送接收失败的MQ消息
 * 
 * @author WangCT
 * @version 1.0 2010.11.04
 */
public class BINBEMQSYN03_FN implements FunctionProvider {
	
	private static Logger logger = LoggerFactory.getLogger(BINBEMQSYN03_FN.class.getName());
	
	/** MQ同步batch处理BL */
	@Resource
	private BINBEMQSYN01_BL binBEMQSYN01_BL;
	
	/** MQ同步batch处理BL */
	@Resource
	private BINBEMQSYN02_BL binBEMQSYN02_BL;

	@Override
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		try {
			logger.info("******************************重新发送Witpos接收失败的MQ消息BATCH处理开始***************************");
			int flag = binBEMQSYN02_BL.sendFailMQLogCherry(transientVars);
			ps.setInt("result", flag);
		} catch (Exception e) {
			// 打印错误信息
			logger.error(e.getMessage(),e);
			throw new WorkflowException();
		} finally {
			logger.info("******************************重新发送Witpos接收失败的MQ消息BATCH处理结束***************************");
		}
	}

}
