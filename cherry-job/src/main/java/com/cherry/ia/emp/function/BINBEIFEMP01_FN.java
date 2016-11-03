/*
 * @(#)BINBEIFEMP01_FN.java     1.0 2010/11/12
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

package com.cherry.ia.emp.function;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.ia.emp.bl.BINBEIFEMP01_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

/**
 * 
 * 营业员列表导入Action
 * 
 * 
 * @author WangCT
 * @version 1.0 2011.05.26
 */
public class BINBEIFEMP01_FN implements FunctionProvider {
	
	private static Logger logger = LoggerFactory.getLogger(BINBEIFEMP01_FN.class.getName());

	/** 营业员列表导入BL */
	@Resource
	private BINBEIFEMP01_BL binBEIFEMP01_BL;
	
	@Override
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		try {
			logger.info("******************************营业员列表导入BATCH处理开始***************************");
			int flag = binBEIFEMP01_BL.tran_batchBaInfo(transientVars);
			ps.setInt("result", flag);
		} catch (Exception e) {
			// 打印错误信息
			logger.error(e.getMessage(),e);
			throw new WorkflowException();
		} finally {
			logger.info("******************************营业员列表导入BATCH处理结束***************************");
		}
	}

}
