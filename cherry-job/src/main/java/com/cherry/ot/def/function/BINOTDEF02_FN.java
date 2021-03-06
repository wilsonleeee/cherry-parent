/*	
 * @(#)BINOTDEF02_FN.java     1.0 @2013-7-11		
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
package com.cherry.ot.def.function;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.ot.def.bl.BINOTDEF02_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

/**
 *
 * 标准订单数据导出FN
 *
 * @author jijw
 *
 * @version  2013-7-11
 */
public class BINOTDEF02_FN implements FunctionProvider{
	private static Logger logger = LoggerFactory.getLogger(BINOTDEF02_FN.class.getName());
	
	/** 标准订单数据导出BATCH处理BL */
	@Resource(name="binOTDEF02_BL")
	private BINOTDEF02_BL binOTDEF02_BL;
	
	@Override
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		try {
			logger.info("*****************************标准订单数据导出BATCH处理开始***************************");
			int flag = binOTDEF02_BL.tran_batchOTDEF02(transientVars);
			ps.setInt("result", flag);
		} catch (Exception e) {
			// 打印错误信息
			logger.error(e.getMessage(),e);
			throw new WorkflowException();
		} finally {
			logger.info("******************************标准订单数据导出BATCH处理结束***************************");
		}
		
	}

}
