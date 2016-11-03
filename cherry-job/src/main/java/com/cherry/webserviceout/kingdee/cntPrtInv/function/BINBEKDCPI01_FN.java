/*	
 * @(#)BINBEKDCPI01_FN.java     1.0 @2015-5-13		
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
package com.cherry.webserviceout.kingdee.cntPrtInv.function;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.webserviceout.kingdee.cntPrtInv.bl.BINBEKDCPI01_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

/**
 *
 * Kingdee接口：柜台产品库存更新FN
 *
 * @author jijw
 *
 * @version  2015-5-13
 */
public class BINBEKDCPI01_FN implements FunctionProvider{
	private static Logger logger = LoggerFactory.getLogger(BINBEKDCPI01_FN.class.getName());
	
	/** Kingdee柜台产品库存更新BATCH处理BL */
	@Resource(name="binbekdcpi01_BL")
	private BINBEKDCPI01_BL binbekdcpi01_BL;
	
	@Override
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		try {
			logger.info("******************************Kingdee柜台产品库存更新Batch处理开始***************************");
			transientVars.put("RunType", "AT");
			int flag = binbekdcpi01_BL.tran_batchkdcpi01(transientVars);
			ps.setInt("result", flag);
		} catch (Exception e) {
			// 打印错误信息
			logger.error(e.getMessage(),e);
			throw new WorkflowException();
		} finally {
			logger.info("******************************Kingdee柜台产品库存更新Batch处理结束***************************");
		}
		
	}

}
