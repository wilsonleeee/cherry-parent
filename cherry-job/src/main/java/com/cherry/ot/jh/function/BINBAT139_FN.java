/*	
 * @(#)BINBAT139_FN.java     1.0 @2016-3-17		
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
package com.cherry.ot.jh.function;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.ot.jh.bl.BINBAT139_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

/**
 *
 * SAP接口(WSDL)：产品导入FN
 *
 * @author jijw
 *
 * @version  2016-3-17
 */
public class BINBAT139_FN implements FunctionProvider{
	private static Logger logger = LoggerFactory.getLogger(BINBAT139_FN.class.getName());
	
	private static CherryBatchLogger cblogger = new CherryBatchLogger(BINBAT139_FN.class);	
	
	@Resource(name="binBAT139_BL")
	private BINBAT139_BL binBAT139_BL;
	
	@Override
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		try {
			logger.info("******************************SAP(WSDL家化)产品导入BATCH处理开始***************************");
			
			// Job运行履历表的运行方式
			transientVars.put("RunType", "AT");
			int flag = binBAT139_BL.tran_batchExecute(transientVars);
			ps.setInt("result", flag);
			
		} catch (Exception e) {
			// 打印错误信息
			logger.error(e.getMessage(),e);
			throw new WorkflowException();
		} finally {
			logger.info("******************************SAP(WSDL家化)产品导入BATCH处理结束***************************");
		}
		
	}

}
