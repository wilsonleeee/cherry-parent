/*	
 * @(#)BINBEMBARC02_FN.java     1.0 2013/04/11
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

import com.cherry.mb.arc.bl.BINBEMBARC02_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

/**
 * 会员积分初始导入处理 FN
 * 
 * @author hub
 * @version 1.0 2013/04/11
 */
public class BINBEMBARC02_FN implements FunctionProvider{
private static Logger logger = LoggerFactory.getLogger(BINBEMBARC02_FN.class.getName());
	
	/** 会员积分初始导入处理BL */
	@Resource
	private BINBEMBARC02_BL binBEMBARC02_BL;


	@Override
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		try {
			logger.info("******************************会员积分初始导入处理开始***************************");
			if (args.containsKey("cgptFlag")) {
				transientVars.put("cgptFlag", "1");
			}
			int flag = binBEMBARC02_BL.tran_ImptMemPoint(transientVars);
			ps.setInt("result", flag);
		} catch (Exception e) {
			// 打印错误信息
			logger.error(e.getMessage(),e);
			throw new WorkflowException();
		} finally {
			logger.info("******************************会员积分初始导入处理结束***************************");
		}
	}
}
