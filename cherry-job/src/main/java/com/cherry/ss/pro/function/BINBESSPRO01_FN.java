/*
 * @(#)BINBESSPRO01_FN.java     1.0 2010/12/09
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

import com.cherry.ss.pro.bl.BINBESSPRO01_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

/**
 * 
 * 产品月度库存统计FN
 * 
 * 
 * @author ZhangJie
 * @version 1.0 2010.12.09
 */
public class BINBESSPRO01_FN implements FunctionProvider {
	
	private static Logger logger = LoggerFactory.getLogger(BINBESSPRO01_FN.class.getName());
	
	/** 产品月度库存统计BL */
	@Resource
	private BINBESSPRO01_BL binbesspro01bl;

	@Override
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		try {
			logger.info("******************************产品月度库存统计BATCH处理开始***************************");
			int flag = binbesspro01bl.tran_batchStockHistory(transientVars);
			ps.setInt("result", flag);
		} catch (Exception e) {
			// 打印错误信息
			logger.error(e.getMessage(),e);
			throw new WorkflowException();
		} finally {
			logger.info("******************************产品月度库存统计BATCH处理结束***************************");
		}
	}

}
