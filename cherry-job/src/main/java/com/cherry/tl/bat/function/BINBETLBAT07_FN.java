/*
 * @(#)BINBETLBAT07_FN.java     1.0 2013/10/16
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

import com.cherry.tl.bat.bl.BINBETLBAT07_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

/**
 * 
 * 清除临时文件处理FN
 * 
 * @author WangCT
 * @version 1.0 2013/10/16
 */
public class BINBETLBAT07_FN implements FunctionProvider {
	
	private static Logger logger = LoggerFactory.getLogger(BINBETLBAT07_FN.class.getName());
	
	/** 清除临时文件处理BL */
	@Resource
	private BINBETLBAT07_BL binBETLBAT07_BL;

	@Override
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		try {
			logger.info("******************************清除临时文件处理开始***************************");
			int flag = binBETLBAT07_BL.clearTempFiles(transientVars);
			ps.setInt("result", flag);
		} catch (Exception e) {
			// 打印错误信息
			logger.error(e.getMessage(), e);
			throw new WorkflowException();
		} finally {
			logger.info("******************************清除临时文件处理结束***************************");
		}
	}

}
