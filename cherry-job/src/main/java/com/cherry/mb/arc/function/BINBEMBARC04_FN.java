/*	
 * @(#)BINBEMBARC04_FN.java     1.0 2013/11/07		
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

import com.cherry.mb.arc.bl.BINBEMBARC04_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

/**
 * 老后台会员资料导入新后台处理BL
 * 
 * @author WangCT
 * @version 1.0 2013/11/07
 */
public class BINBEMBARC04_FN implements FunctionProvider {
	
	private static Logger logger = LoggerFactory.getLogger(BINBEMBARC04_FN.class.getName());
	
	/** 老后台会员资料导入新后台处理BL */
	@Resource
	private BINBEMBARC04_BL binBEMBARC04_BL;

	@Override
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		try {
			logger.info("******************************老后台会员资料导入新后台处理开始***************************");
			int flag = binBEMBARC04_BL.memberInfoHandle(transientVars);
			ps.setInt("result", flag);
		} catch (Exception e) {
			// 打印错误信息
			logger.error(e.getMessage(), e);
			throw new WorkflowException();
		} finally {
			logger.info("******************************老后台会员资料导入新后台处理结束***************************");
		}
	}

}
