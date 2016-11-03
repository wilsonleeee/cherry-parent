/*	
 * @(#)BINOTLS01_FN.java     1.0 @2014-11-18		
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
package com.cherry.ot.ls.function;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.ot.ls.bl.BINOTLS01_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

/**
 *
 * LotionSpa接口：产品导入FN
 *
 * @author jijw
 *
 * @version  2014-11-18
 */
public class BINOTLS01_FN implements FunctionProvider{
	private static Logger logger = LoggerFactory.getLogger(BINOTLS01_FN.class.getName());
	
	@Resource(name="binOTLS01_BL")
	private BINOTLS01_BL binOTLS01_BL;
	
	@Override
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		try {
			logger.info("****************************** LotionSpa产品导入BATCH处理开始  ***************************");
			int flag = binOTLS01_BL.tran_batchOTLS01(transientVars);
			ps.setInt("result", flag);
		} catch (Exception e) {
			// 打印错误信息
			logger.error(e.getMessage(),e);
			throw new WorkflowException();
		} finally {
			logger.info("****************************** LotionSpa产品导入BATCH处理结束 ***************************");
		}
		
	}

}
