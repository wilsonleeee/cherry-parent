/*
 * @(#)BINBEDRHAN10_FN.java     1.0 2012/05/28
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
package com.cherry.dr.handler.function;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.dr.handler.bl.BINBEDRHAN10_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

/**
 * 
 * 处理会员的规则履历记录FN
 * 
 * 
 * @author WangCT
 * @version 1.0 2012/05/28
 */
public class BINBEDRHAN10_FN implements FunctionProvider {
	
	private static Logger logger = LoggerFactory.getLogger(BINBEDRHAN10_FN.class.getName());
	
	/** 处理会员的规则履历记录BL */
	@Resource
	private BINBEDRHAN10_BL binBEDRHAN10_BL;

	@SuppressWarnings("unchecked")
	@Override
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		try {
			logger.info("******************************会员规则履历记录处理开始***************************");
			int flag = binBEDRHAN10_BL.tran_memRuleRecordHandle(transientVars);
			ps.setInt("result", flag);
		} catch (Exception e) {
			// 打印错误信息
			logger.error(e.getMessage(),e);
			throw new WorkflowException();
		} finally {
			logger.info("******************************会员规则履历记录处理结束***************************");
		}
	}

}
