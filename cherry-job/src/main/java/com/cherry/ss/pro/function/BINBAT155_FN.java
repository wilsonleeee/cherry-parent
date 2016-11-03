/*	
 * @(#)BINBAT155_FN.java     1.0 @2016-8-2	
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


import com.cherry.ss.pro.bl.BINBAT155_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

/**
 * 退库确认单成本导出(浓妆淡抹)FN
 * 
 * @author zw
 * 
 * @version 2016-06-27
 * 
 */
public class BINBAT155_FN implements FunctionProvider {

	private static Logger logger = LoggerFactory.getLogger(BINBAT155_FN.class.getName());
	
	/** 产品退库确认单详细数据导出到标准接口表BL */
	@Resource(name = "binbat155_BL")
	private BINBAT155_BL binbat155_BL;

	@Override
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {

		try {
			logger.info("******************************退库确认单成本导出(浓妆淡抹)处理开始***************************");
			// Job运行履历表的运行方式
			transientVars.put("RunType", "AT");
			int flag = binbat155_BL.tran_binbat155(transientVars);
			ps.setInt("result", flag);
		} catch (Exception e) {
			// 打印错误信息
			logger.error(e.getMessage(),e);
			throw new WorkflowException();
		} finally {
			logger.info("******************************退库确认单成本导出(浓妆淡抹)处理结束***************************");
		}

	}

}
