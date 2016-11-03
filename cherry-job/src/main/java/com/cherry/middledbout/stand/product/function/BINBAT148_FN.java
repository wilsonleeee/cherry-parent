/*	
 * @(#)BINBAT148_FN.java     1.0 @2016-06-27		
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
package com.cherry.middledbout.stand.product.function;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.middledbout.stand.product.bl.BINBAT148_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

/**
 * 
 * 柜台特价产品导入(标准接口)(IF_Offers)FN
 * @author jijw
 *  * @version 1.0 2016.06.27
 *
 */
public class BINBAT148_FN implements FunctionProvider {
	private static Logger logger = LoggerFactory.getLogger(BINBAT148_FN.class.getName());
	
	/**标准接口柜台特价产品信息Batch数据导入处理BL **/
	@Resource(name = "binBAT148_BL")
	private BINBAT148_BL binBAT148_BL;

	@Override
	public void execute(Map transientVars, Map arg, PropertySet ps) throws WorkflowException {
		try {
			logger.info("**************************柜台特价产品导入(标准接口)开始 *******************************");
			//Job运行履历表的运行方式->自动运行标志
			transientVars.put("RunType", "AT");
			int flag=binBAT148_BL.tran_batchExec(transientVars);
			ps.setInt("result", flag);
		} catch (Exception e) {
			//打印错误日志信息
			logger.error(e.getMessage(),e);
			throw new WorkflowException();
		}
		finally{
			logger.info("***************************柜台特价产品导入(标准接口)结束******************************");
		}
	}

}
