/*
 * @(#)BINBEMQSYN01_FN.java     1.0 2010/11/04
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
 * MQ接收同步BATCH处理
 * 
 * @author WangCT
 * @version 1.0 2010.11.04
 */
public class BINBEMQSYN01_FN implements FunctionProvider {
	
	private static Logger logger = LoggerFactory.getLogger(BINBEMQSYN01_FN.class.getName());
	
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
			logger.info("******************************MQ接收同步BATCH处理开始***************************");
			int flag;
			if("1".equals(String.valueOf(transientVars.get("freshFlag")))){
				logger.info("刷新工作流文件失败，不进行同步操作！");
				flag = 1;
			}else{
				flag = binBEMQSYN02_BL.tran_receFailMQLogHandle(transientVars);
			}
			ps.setInt("result", flag);
		} catch (Exception e) {
			// 打印错误信息
			logger.error(e.getMessage(),e);
			throw new WorkflowException();
		} finally {
			logger.info("******************************MQ接收同步BATCH处理结束***************************");
		}
	}

}
