/*	
 * @(#)BINBAT153_FN.java     1.0 @2016-7-10	
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
package com.cherry.ss.pro.function;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.ss.pro.bl.BINBAT153_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

/**
 *
 * 标准接口 ：产品入出库批次成本导出 FN
 *
 * @author zw
 *
 * @version  2016-7-10
 */
public class BINBAT153_FN implements FunctionProvider{
	private static Logger logger = LoggerFactory.getLogger(BINBAT153_FN.class.getName());
	
	/** 产品入出库批次成本导出（标准接口）BATCH处理BL */
	@Resource(name="binBAT153_BL")
	private BINBAT153_BL binBAT153_BL;
	
	@Override
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		try {
			logger.info("******************************产品入出库批次成本导出（标准接口）BATCH处理开始***************************");
			int flag = binBAT153_BL.tran_batchbat153(transientVars);
			ps.setInt("result", flag);
		} catch (Exception e) {
			// 打印错误信息
			logger.error(e.getMessage(),e);
			throw new WorkflowException();
		} finally {
			logger.info("******************************产品入出库批次成本导出（标准接口）BATCH处理结束***************************");
		}
		
	}

}
