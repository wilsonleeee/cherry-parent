/*	
 * @(#)BINBAT110_FN.java     1.0 @2015-6-2		
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
package com.cherry.webserviceout.kingdee.productOrder.function;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.webserviceout.kingdee.productOrder.bl.BINBAT110_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

/**
 *
 * Kingdee接口：推送产品订单FN
 *
 * @author jijw
 *
 * @version  2015-6-02
 */
public class BINBAT110_FN implements FunctionProvider{
	private static Logger logger = LoggerFactory.getLogger(BINBAT110_FN.class.getName());
	
	/** Kingdee产品订单推送BATCH处理BL */
	@Resource(name="binbat110_BL")
	private BINBAT110_BL binbat110_BL;
	
	@Override
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		try {
			logger.info("******************************Kingdee推送产品订单atch处理开始***************************");
			transientVars.put("RunType", "AT");
			int flag = binbat110_BL.tran_batchBat110(transientVars);
			ps.setInt("result", flag);
		} catch (Exception e) {
			// 打印错误信息
			logger.error(e.getMessage(),e);
			throw new WorkflowException();
		} finally {
			logger.info("******************************Kingdee推送产品订单Batch处理结束***************************");
		}
		
	}

}
