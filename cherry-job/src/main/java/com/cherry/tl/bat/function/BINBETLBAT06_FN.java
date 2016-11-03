/*
 * @(#)BINBETLBAT06_FN.java     1.0 2013/08/20
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
package com.cherry.tl.bat.function;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.tl.bat.bl.BINBETLBAT06_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

/**
 * 
 * 清理MongoDB相关表处理FN
 * 
 * @author WangCT
 * @version 1.0 2013/08/20
 */
public class BINBETLBAT06_FN implements FunctionProvider {
	
	private static Logger logger = LoggerFactory.getLogger(BINBETLBAT06_FN.class.getName());
	
	/** 清理MongoDB相关表处理BL */
	@Resource
	private BINBETLBAT06_BL binBETLBAT06_BL;

	@Override
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		try {
			logger.info("******************************清理MongoDB相关表处理开始***************************");
			int flag = binBETLBAT06_BL.clearMongoDB(transientVars);
			ps.setInt("result", flag);
		} catch (Exception e) {
			// 打印错误信息
			logger.error(e.getMessage(),e);
			throw new WorkflowException();
		} finally {
			logger.info("******************************清理MongoDB相关表处理结束***************************");
		}
	}

}
