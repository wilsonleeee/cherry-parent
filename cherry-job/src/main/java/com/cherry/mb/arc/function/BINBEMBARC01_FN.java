/*	
 * @(#)BINBEMBARC01_FN.java     1.0 2012/06/04		
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
package com.cherry.mb.arc.function;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.mb.arc.bl.BINBEMBARC01_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

/**
 * 官网会员资料数据导入处理FN
 * 
 * @author WangCT
 * @version 1.0 2012/06/04
 */
public class BINBEMBARC01_FN implements FunctionProvider {
	
	private static Logger logger = LoggerFactory.getLogger(BINBEMBARC01_FN.class.getName());
	
	/** 官网会员资料数据导入处理BL */
	@Resource
	private BINBEMBARC01_BL binBEMBARC01_BL;

	@Override
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		try {
			logger.info("******************************官网会员资料数据导入处理开始***************************");
			int flag = binBEMBARC01_BL.memberInfoHandle(transientVars);
			ps.setInt("result", flag);
		} catch (Exception e) {
			// 打印错误信息
			logger.error(e.getMessage(),e);
			throw new WorkflowException();
		} finally {
			logger.info("******************************官网会员资料数据导入处理结束***************************");
		}
	}

}
