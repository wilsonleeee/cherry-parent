/*	
 * @(#)BINBEMBARC06_FN.java     1.0 2013/12/19		
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

import com.cherry.mb.arc.bl.BINBEMBARC06_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

/**
 * 汇美舍会员积分清零明细下发处理 FN
 * 
 * @author hub
 * @version 1.0 2013/12/19
 */
public class BINBEMBARC06_FN implements FunctionProvider{
	
	private static Logger logger = LoggerFactory.getLogger(BINBEMBARC06_FN.class.getName());
	
	/** 降级处理BL */
	@Resource
	private BINBEMBARC06_BL binBEMBARC06_BL;
	
	@Override
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		try {
			logger.info("******************************汇美舍会员积分清零明细下发处理开始***************************");
			int flag = binBEMBARC06_BL.tran_clearDetailSend(transientVars);
			ps.setInt("result", flag);
		} catch (Exception e) {
			// 打印错误信息
			logger.error(e.getMessage(),e);
			throw new WorkflowException();
		} finally {
			logger.info("******************************汇美舍会员积分清零明细下发处理结束***************************");
		}
		
	}
}
