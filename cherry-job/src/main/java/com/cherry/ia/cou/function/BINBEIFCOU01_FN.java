/*
 * @(#)BINBEIFCOU01_FN.java     1.0 2010/11/12
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

package com.cherry.ia.cou.function;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.ia.cou.bl.BINBEIFCOU01_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

/**
 * 
 * 柜台列表导入FN
 * 
 * 
 * @author ZhangJie
 * @version 1.0 2010.11.12
 */
public class BINBEIFCOU01_FN implements FunctionProvider {
	
	private static Logger logger = LoggerFactory.getLogger(BINBEIFCOU01_FN.class.getName());
	
	/** 柜台列表导入BL */
	@Resource
	private BINBEIFCOU01_BL binBEIFCOU01_BL;

	@Override
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		try {
			logger.info("******************************柜台列表导入BATCH处理开始***************************");
			transientVars.put("RunType", "AT");
			int flag = binBEIFCOU01_BL.tran_batchCounters(transientVars);
			ps.setInt("result", flag);
		} catch (Exception e) {
			// 打印错误信息
			logger.error(e.getMessage(),e);
			throw new WorkflowException();
		} finally {
			logger.info("******************************柜台列表导入BATCH处理结束***************************");
		}
	}

}
