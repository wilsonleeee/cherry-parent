/*	
 * @(#)BINBAT152_FN.java     1.0 @2015-12-22		
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
import com.cherry.ss.pro.bl.BINBAT152_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

/**
 * 补录产品入出库成本(标准接口)FN
 * 
 * @author chenkuan
 * 
 * @version 2016-07-09
 * 
 */
public class BINBAT152_FN implements FunctionProvider {

	private static Logger logger = LoggerFactory.getLogger(BINBAT152_FN.class.getName());
	
	/** 补录产品入出库成本（标准接口）BL */
	@Resource(name = "binbat152_BL")
	private BINBAT152_BL binbat152_BL;

	@Override
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {

		try {
			logger.info("******************************补录产品入出库成本(标准接口)处理开始***************************");
			// Job运行履历表的运行方式
			transientVars.put("RunType", "AT");
			int flag = binbat152_BL.tran_binbat152(transientVars);
			ps.setInt("result", flag);
		} catch (Exception e) {
			// 打印错误信息
			logger.error(e.getMessage(),e);
			throw new WorkflowException();
		} finally {
			logger.info("******************************补录产品入出库成本(标准接口)处理结束***************************");
		}

	}

}
