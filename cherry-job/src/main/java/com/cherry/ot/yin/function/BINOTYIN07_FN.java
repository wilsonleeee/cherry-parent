/*	
 * @(#)BINOTYIN01_FN.java     1.0 @2013-3-21		
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
package com.cherry.ot.yin.function;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.ot.yin.bl.BINOTYIN07_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

/**
 *
 * 颖通接口：产品盘点单据导出
 *
 * @author jijw
 *
 * @version  2013-3-21
 */
public class BINOTYIN07_FN implements FunctionProvider{
	private static Logger logger = LoggerFactory.getLogger(BINOTYIN07_FN.class.getName());
	
	/**产品盘点单据导出BATCH处理BL */
	@Resource(name="binOTYIN07_BL")
	private BINOTYIN07_BL binOTYIN07_BL;
	
	@Override
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		try {
			logger.info("******************************产品盘点单据导出BATCH处理开始***************************");
			int flag = binOTYIN07_BL.tran_batchOTYIN07(transientVars);
			ps.setInt("result", flag);
		} catch (Exception e) {
			// 打印错误信息
			logger.error(e.getMessage(),e);
			throw new WorkflowException();
		} finally {
			logger.info("******************************产品盘点单据导出BATCH处理结束***************************");
		}
		
	}

}
