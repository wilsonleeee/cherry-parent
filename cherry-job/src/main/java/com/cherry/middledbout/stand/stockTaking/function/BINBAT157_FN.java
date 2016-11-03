/*	
 * @(#)BINBAT157_FN.java     1.0 @2015-12-16		
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
package com.cherry.middledbout.stand.stockTaking.function;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.middledbout.stand.stockTaking.bl.BINBAT157_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

/**
 *
 * 柜台盘点单据导出(标准接口)FN
 *
 * @author jijw
 *
 * @version  2015-12-16
 */
public class BINBAT157_FN implements FunctionProvider{
	private static Logger logger = LoggerFactory.getLogger(BINBAT157_FN.class.getName());
	
	/**柜台盘点申请单据导出(标准接口)BL **/
	@Resource(name = "binBAT157_BL")
	private BINBAT157_BL binBAT157_BL;
	
	@Override
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		try {
			logger.info("******************************柜台盘点单据导出(标准接口)处理开始***************************");
			//Job运行履历表的运行方式->自动运行标志
			transientVars.put("RunType", "AT");
			int flag = binBAT157_BL.tran_batchExec(transientVars);
			ps.setInt("result", flag);
		} catch (Exception e) {
			// 打印错误信息
			logger.error(e.getMessage(),e);
			throw new WorkflowException();
		} finally {
			logger.info("******************************柜台盘点单据导出(标准接口)处理结束***************************");
		}
		
	}

}
