/*
 * @(#)BINBETLBAT01_FN.java     1.0 2010/12/24
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

import com.cherry.tl.bat.bl.BINBETLBAT01_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

/**
 * 
 * 清空采番表处理FN
 * 
 * 
 * @author ZHANGJIE
 * @version 1.0 2010.12.24
 */
public class BINBETLBAT01_FN implements FunctionProvider {
	
	private static Logger logger = LoggerFactory.getLogger(BINBETLBAT01_FN.class.getName());
	
	/** 清空各种表BL */
	@Resource
	private BINBETLBAT01_BL binbetlbat01bl;

	@Override
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		try {
			logger.info("******************************清空采番表BATCH处理开始***************************");
			int flag = binbetlbat01bl.clearTicketNumber(transientVars);
			ps.setInt("result", flag);
		} catch (Exception e) {
			// 打印错误信息
			logger.error(e.getMessage(),e);
			throw new WorkflowException();
		} finally {
			logger.info("******************************清空采番表BATCH处理结束***************************");
		}
	}

}
