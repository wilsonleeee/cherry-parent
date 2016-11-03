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

package com.cherry.middledbout.stand.depot.function;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.middledbout.stand.depot.bl.BINBAT145_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

/**
 * 入库单据导出(标准接口)FN
 * 
 * @author lzs
 * 
 * @version 2016-06-08
 * 
 */
public class BINBAT145_FN implements FunctionProvider {

	private static Logger logger = LoggerFactory.getLogger(BINBAT145_FN.class.getName());
	
	/**入库单据详细数据导出到标准接口表BL */
	@Resource(name = "binBAT145_BL")
	private BINBAT145_BL binBAT145_BL;

	@Override
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {

		try {
			logger.info("******************************入库单据导出(标准接口)处理开始***************************");
			// Job运行履历表的运行方式
			transientVars.put("RunType", "AT");
			int flag = binBAT145_BL.tran_binBAT145(transientVars);
			ps.setInt("result", flag);
		} catch (Exception e) {
			// 打印错误信息
			logger.error(e.getMessage(),e);
			throw new WorkflowException();
		} finally {
			logger.info("******************************入库单据导出(标准接口)处理结束***************************");
		}

	}

}
