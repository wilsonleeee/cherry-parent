/*	
 * @(#)BINOTBAT120_FN.java     1.0 @2015-8-13		
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

import com.cherry.ot.yin.bl.BINBAT120_BL;
import com.cherry.ot.yin.bl.BINOTYIN08_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

/**
 *
 * 颖通接口：发货单退库单(订货/退货废除)导入FN
 *
 * @author jijw
 *
 * @version  2015-8-13
 */
public class BINBAT120_FN implements FunctionProvider{
	private static Logger logger = LoggerFactory.getLogger(BINBAT120_FN.class.getName());
	
	/** 发货单退库单(订货/退货废除)导入BATCH处理BL */
	@Resource(name="binBAT120_BL")
	private BINBAT120_BL binBAT120_BL;
	
	@Override
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		try {
			logger.info("******************************发货单退库单(订货/退货废除)导入BATCH处理开始***************************");
			transientVars.put("RunType", "AT");
			int flag = binBAT120_BL.tran_batchExec(transientVars);
			ps.setInt("result", flag);
		} catch (Exception e) {
			// 打印错误信息
			logger.error(e.getMessage(),e);
			throw new WorkflowException();
		} finally {
			logger.info("******************************发货单退库单(订货/退货废除)导入BATCH处理结束***************************");
		}
		
	}

}
