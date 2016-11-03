/*	
 * @(#)BINOTYIN11_FN.java     1.0 @2013-5-02		
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
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

/**
 *
 * 颖通接口：颖通柜台导入FN
 *
 * @author jijw
 *
 * @version  2013-5-02
 */
public class BINOTYIN11_FN implements FunctionProvider{
	private static Logger logger = LoggerFactory.getLogger(BINOTYIN11_FN.class.getName());
	
	/** 颖通柜台导入BATCH处理BL */
	@Resource(name="binOTYIN11_BL")
	private BINOTYIN11_BL binOTYIN11_BL;
	
	@Override
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		try {
			logger.info("******************************颖通柜台导入BATCH处理开始***************************");
			int flag = binOTYIN11_BL.tran_batchOTYIN11(transientVars);
			ps.setInt("result", flag);
		} catch (Exception e) {
			// 打印错误信息
			logger.error(e.getMessage(),e);
			throw new WorkflowException();
		} finally {
			logger.info("******************************颖通柜台导入BATCH处理结束***************************");
		}
		
	}

}
