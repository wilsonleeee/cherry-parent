/*	
 * @(#)BINOTYIN01_FN.java     1.0 @2015-1-28		
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
package com.cherry.ot.yin.function;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.ot.yin.bl.BINOTYIN13_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

/**
 *
 * 颖通接口：颖通产品导入FN
 *
 * @author jijw
 *
 * @version  2015-1-28
 */
public class BINOTYIN13_FN implements FunctionProvider{
	private static Logger logger = LoggerFactory.getLogger(BINOTYIN13_FN.class.getName());
	
	/** 颖通大陆产品导入BATCH处理BL */
	@Resource(name="binOTYIN13_BL")
	private BINOTYIN13_BL binOTYIN13_BL;
	
	@Override
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		try {
			logger.info("******************************颖通大陆产品导入BATCH处理开始***************************");
			transientVars.put("RunType", "AT");
			int flag = binOTYIN13_BL.tran_batchOTYIN13(transientVars);
			ps.setInt("result", flag);
		} catch (Exception e) {
			// 打印错误信息
			logger.error(e.getMessage(),e);
			throw new WorkflowException();
		} finally {
			logger.info("******************************颖通大陆产品导入BATCH处理结束***************************");
		}
		
	}

}
