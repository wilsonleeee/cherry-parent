/*	
 * @(#)BINBAT111_FN.java     1.0 @2015-6-16		
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
package com.cherry.webserviceout.kingdee.sale.function;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.webserviceout.kingdee.sale.bl.BINBAT111_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

/**
 *
 * Kingdee接口：推送销售单据FN
 *
 * @author jijw
 *
 * @version  2015-6-16
 */
public class BINBAT111_FN implements FunctionProvider{
	private static Logger logger = LoggerFactory.getLogger(BINBAT111_FN.class.getName());
	
	/** Kingdee销售单据推送BATCH处理BL */
	@Resource(name="binbat111_BL")
	private BINBAT111_BL binbat111_BL;
	
	@Override
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		try {
			logger.info("******************************Kingdee推送销售单据Batch处理开始***************************");
			transientVars.put("RunType", "AT");
			int flag = binbat111_BL.tran_batchBat111(transientVars);
			ps.setInt("result", flag);
		} catch (Exception e) {
			// 打印错误信息
			logger.error(e.getMessage(),e);
			throw new WorkflowException();
		} finally {
			logger.info("******************************Kingdee推送销售单据Batch处理结束***************************");
		}
		
	}

}
