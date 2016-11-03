/*	
 * @(#)BINBEDRHAN01_FN.java     1.0 2011/08/26	
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

import com.cherry.dr.handler.bl.BINBEDRHAN01_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

/**
 * 
 * 会员等级和化妆次数重算FN
 * 
 * 
 * @author WangCT
 * @version 1.0 2011/08/26	
 */
public class BINBEDRHAN01_FN implements FunctionProvider {
	
	private static Logger logger = LoggerFactory.getLogger(BINBEDRHAN01_FN.class.getName());
	
	/** 会员等级和化妆次数重算BL */
	@Resource
	private BINBEDRHAN01_BL binBEDRHAN01_BL;

	@SuppressWarnings("unchecked")
	@Override
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		try {
			logger.info("******************************会员等级和化妆次数重算BATCH处理开始***************************");
			int flag = binBEDRHAN01_BL.tran_reCalcMemLevel(transientVars);
			ps.setInt("result", flag);
		} catch (Exception e) {
			// 打印错误信息
			logger.error(e.getMessage(),e);
			throw new WorkflowException();
		} finally {
			logger.info("******************************会员等级和化妆次数重算BATCH处理结束***************************");
		}
	}

}
