/*
 * @(#)BINBETLBAT04_FN.java     1.0 2011/07/13
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

import com.cherry.tl.bat.bl.BINBETLBAT03_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

/**
 * 
 * 更新业务日期FN
 * 
 * 
 * @author WangCT
 * @version 1.0 2011/07/13
 */
public class BINBETLBAT04_FN implements FunctionProvider {
	
	private static Logger logger = LoggerFactory.getLogger(BINBETLBAT04_FN.class.getName());
	
	/** 更新业务日期BL */
	@Resource
	private BINBETLBAT03_BL binBETLBAT03_BL;

	@Override
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		try {
			logger.info("******************************更新业务日期BATCH处理开始***************************");
			int flag = binBETLBAT03_BL.updateBussinessDate(transientVars);
			ps.setInt("result", flag);
		} catch (Exception e) {
			// 打印错误信息
			logger.error(e.getMessage(),e);
			throw new WorkflowException();
		} finally {
			logger.info("******************************更新业务日期ATCH处理结束***************************");
		}
	}

}
