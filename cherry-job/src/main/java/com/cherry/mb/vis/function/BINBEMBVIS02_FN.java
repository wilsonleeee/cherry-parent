/*	
 * @(#)BINBEMBVIS02_FN.java     1.0 @2012-12-17		
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
package com.cherry.mb.vis.function;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.mb.vis.bl.BINBEMBVIS02_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

/**
 *
 * 会员回访任务生成FN
 *
 * @author jijw
 *
 * @version  2012-12-17
 */
public class BINBEMBVIS02_FN  implements FunctionProvider {
	
	private static Logger logger = LoggerFactory.getLogger(BINBEMBVIS02_FN.class.getName());
	
	/** 会员回访任务生成batch处理BL */
	@Resource(name="binBEMBVIS02_BL")
	private BINBEMBVIS02_BL binBEMBVIS02_BL;
	
	@SuppressWarnings("unchecked")
	@Override
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		try {
			logger.info("******************************会员回访任务生成BATCH处理开始***************************");
			int flag = binBEMBVIS02_BL.tran_batchMemVistTask(transientVars);
			ps.setInt("result", flag);
		} catch (Exception e) {
			// 打印错误信息
			logger.error(e.getMessage(),e);
			throw new WorkflowException();
		} finally {
			logger.info("******************************会员回访任务生成BATCH处理结束***************************");
		}
		
	}

}
