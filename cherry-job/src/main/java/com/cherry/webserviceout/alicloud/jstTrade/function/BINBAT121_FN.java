/*	
 * @(#)BINBAT121_FN.java     1.0 @2015-9-16		
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
package com.cherry.webserviceout.alicloud.jstTrade.function;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.webserviceout.alicloud.jstTrade.bl.BINBAT121_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

/**
*
* 聚石塔接口：订单(销售)数据导入FN
* 
* 从聚石塔获取订单数据并存入新后台电商接口表及发送销售MQ
*
* @author jijw
*
* @version  2015-9-16
*/
public class BINBAT121_FN implements FunctionProvider{
	private static Logger logger = LoggerFactory.getLogger(BINBAT121_FN.class.getName());
	
	/** Kingdee销售单据推送BATCH处理BL */
	@Resource(name="binbat121_BL")
	private BINBAT121_BL binbat121_BL;
	
	@Override
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		try {
			logger.info("******************************聚石塔订单导入Batch处理开始***************************");
			transientVars.put("RunType", "AT");
			int flag = binbat121_BL.tran_batchBat121(transientVars);
			ps.setInt("result", flag);
		} catch (Exception e) {
			// 打印错误信息
			logger.error(e.getMessage(),e);
            throw new WorkflowException();
		} finally {
			logger.info("******************************聚石塔订单导入Batch处理结束***************************");
		}
		
	}

}
