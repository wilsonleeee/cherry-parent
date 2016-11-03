/*
 * @(#)BINBESSRPS01_FN.java     1.0 2012/11/08
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
package com.cherry.ss.rps.function;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.ss.rps.bl.BINBESSRPS01_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

/**
 * 销售月度统计FN
 * 
 * @author WangCT
 * @version 1.0 2012/11/08
 */
public class BINBESSRPS01_FN implements FunctionProvider {
	
	private static Logger logger = LoggerFactory.getLogger(BINBESSRPS01_FN.class.getName());
	
	/** 销售月度统计BL **/
	@Resource
	private BINBESSRPS01_BL binBESSRPS01_BL;

	@Override
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		try {
			logger.info("******************************销售月度统计处理开始***************************");
			int flag = binBESSRPS01_BL.tran_saleCountBatch(transientVars);
			ps.setInt("result", flag);
		} catch (Exception e) {
			// 打印错误信息
			logger.error(e.getMessage(),e);
			throw new WorkflowException();
		} finally {
			logger.info("******************************销售月度统计处理结束***************************");
		}
	}

}
