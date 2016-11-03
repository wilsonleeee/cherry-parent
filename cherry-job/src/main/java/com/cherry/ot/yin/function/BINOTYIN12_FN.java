/*	
 * @(#)BINOTYIN11_FN.java     1.0 @2013-01-26		
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

import com.cherry.ot.yin.bl.BINOTYIN11_BL;
import com.cherry.ot.yin.bl.BINOTYIN12_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

/**
 *
 * 颖通接口：颖通BA信息导出FN
 *
 * @author jijw
 *
 * @version  2013-01-26
 */
public class BINOTYIN12_FN implements FunctionProvider{
	private static Logger logger = LoggerFactory.getLogger(BINOTYIN12_FN.class.getName());
	
	/** 颖通BA信息导出BATCH处理BL */
	@Resource(name="binOTYIN12_BL")
	private BINOTYIN12_BL binOTYIN12_BL;
	
	@Override
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		try {
			logger.info("******************************颖通BA信息导出BATCH处理开始***************************");
			int flag = binOTYIN12_BL.tran_batchOTYIN12(transientVars);
			ps.setInt("result", flag);
		} catch (Exception e) {
			// 打印错误信息
			logger.error(e.getMessage(),e);
			throw new WorkflowException();
		} finally {
			logger.info("******************************颖通BA信息导出BATCH处理结束***************************");
		}
		
	}

}
