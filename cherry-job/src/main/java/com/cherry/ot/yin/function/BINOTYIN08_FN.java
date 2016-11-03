/*	
 * @(#)BINOTYIN01_FN.java     1.0 @2013-3-11		
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

import com.cherry.ot.yin.bl.BINOTYIN08_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

/**
 *
 * 颖通接口：发货单退库单导入FN
 *
 * @author jijw
 *
 * @version  2013-3-18
 */
public class BINOTYIN08_FN implements FunctionProvider{
	private static Logger logger = LoggerFactory.getLogger(BINOTYIN08_FN.class.getName());
	
	/** 发货单退库单导入BATCH处理BL */
	@Resource(name="binOTYIN08_BL")
	private BINOTYIN08_BL binOTYIN08_BL;
	
	@Override
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		try {
			logger.info("******************************发货单退库单导入BATCH处理开始***************************");
			int flag = binOTYIN08_BL.tran_batchOTYIN08(transientVars);
			ps.setInt("result", flag);
		} catch (Exception e) {
			// 打印错误信息
			logger.error(e.getMessage(),e);
			throw new WorkflowException();
		} finally {
			logger.info("******************************发货单退库单导入BATCH处理结束***************************");
		}
		
	}

}
