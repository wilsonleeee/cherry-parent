/*
 * @(#)BINBEIFDPL01_FN.java     1.0 2010/07/04
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

package com.cherry.ia.dpl.function;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.ia.dpl.bl.BINBEIFDPL01_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

/**
 * 
 * 员工管辖部门数据导入FN
 * 
 * 
 * @author WangCT
 * @version 1.0 2011.07.04
 */
public class BINBEIFDPL01_FN implements FunctionProvider {
	
	private static Logger logger = LoggerFactory.getLogger(BINBEIFDPL01_FN.class.getName());
	
	/** 员工管辖部门数据导入BL */
	@Resource
	private BINBEIFDPL01_BL binBEIFDPL01_BL;

	@Override
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		try {
			logger.info("******************************员工管辖部门数据导入BATCH处理开始***************************");
			int flag = binBEIFDPL01_BL.tran_batchEmpDepart(transientVars);
			ps.setInt("result", flag);
		} catch (Exception e) {
			// 打印错误信息
			logger.error(e.getMessage(),e);
			throw new WorkflowException();
		} finally {
			logger.info("******************************员工管辖部门数据导入BATCH处理结束***************************");
		}
	}

}
