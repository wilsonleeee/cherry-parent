/*	
 * @(#)BINOTYIN14_FN.java     1.0 @2015-5-26	
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

import com.cherry.ot.yin.bl.BINOTYIN14_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

/**
 *
 * 颖通接口：颖通大陆柜台导入FN
 *
 * @author jijw
 *
 * @version  2015-5-26
 */
public class BINOTYIN14_FN implements FunctionProvider{
	private static Logger logger = LoggerFactory.getLogger(BINOTYIN14_FN.class.getName());
	
	/** 颖通大陆柜台导入BATCH处理BL */
	@Resource(name="binOTYIN14_BL")
	private BINOTYIN14_BL binOTYIN14_BL;
	
	@Override
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		try {
			logger.info("******************************颖通大陆柜台导入BATCH处理开始***************************");
			// Job运行履历表的运行方式
			transientVars.put("RunType", "AT");
			int flag = binOTYIN14_BL.tran_batchOTYIN14(transientVars);
			ps.setInt("result", flag);
		} catch (Exception e) {
			// 打印错误信息
			logger.error(e.getMessage(),e);
			throw new WorkflowException();
		} finally {
			logger.info("******************************颖通大陆柜台导入BATCH处理结束***************************");
		}
		
	}

}
