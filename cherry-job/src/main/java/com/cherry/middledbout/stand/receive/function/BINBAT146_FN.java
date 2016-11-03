/*	
 * @(#)BINBAT133_FN.java     1.0 @2015-12-22		
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

package com.cherry.middledbout.stand.receive.function;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.middledbout.stand.receive.bl.BINBAT146_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

/**
 * 收货单据导出(标准接口)FN
 * 
 * @author lzs
 * 
 * @version 2016-06-14
 * 
 */
public class BINBAT146_FN implements FunctionProvider {

	private static Logger logger = LoggerFactory.getLogger(BINBAT146_FN.class.getName());
	
	/**收货单据详细数据导出到标准接口表BL */
	@Resource(name = "binBAT146_BL")
	private BINBAT146_BL binBAT146_BL;

	@Override
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {

		try {
			logger.info("******************************收货单据导出(标准接口)处理开始***************************");
			// Job运行履历表的运行方式
			transientVars.put("RunType", "AT");
			int flag = binBAT146_BL.tran_binBAT146(transientVars);
			ps.setInt("result", flag);
		} catch (Exception e) {
			// 打印错误信息
			logger.error(e.getMessage(),e);
			throw new WorkflowException();
		} finally {
			logger.info("******************************收货单据导出(标准接口)处理结束***************************");
		}

	}

}
