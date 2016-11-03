/*	
 * @(#)BINOTYIN01_FN.java     1.0 @2015-4-29		
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
package com.cherry.webserviceout.kingdee.product.function;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.core.BatchLoggerDTO;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryBatchLogger;
import com.cherry.ia.pro.bl.BINBEIFPRO04_BL;
import com.cherry.webserviceout.kingdee.product.bl.BINBEKDPRO01_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

/**
 *
 * Kingdee接口：产品导入FN
 *
 * @author jijw
 *
 * @version  2015-4-29
 */
public class BINBEKDPRO01_FN implements FunctionProvider{
	private static Logger logger = LoggerFactory.getLogger(BINBEKDPRO01_FN.class.getName());
	
	private static CherryBatchLogger cblogger = new CherryBatchLogger(BINBEKDPRO01_FN.class);	
	
	/** Kingdee产品导入BATCH处理BL */
	@Resource(name="binbekdpro01_BL")
	private BINBEKDPRO01_BL binbekdpro01_BL;
	
	/** 产品实时下发 */
	@Resource(name="binbeifpro04_BL")
	private BINBEIFPRO04_BL binbeifpro04_BL;
	
	@Override
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		try {
			logger.info("******************************Kingdee产品导入BATCH处理开始***************************");
			
			// Job运行履历表的运行方式
			transientVars.put("RunType", "AT");
			transientVars.put("EmployeeId", "KS9999"); // 用于发送【产品下发】MQ时，写入操作者ID。否则终端程序会报错。
			int flag = binbekdpro01_BL.tran_batchkdpro01(transientVars);
			ps.setInt("result", flag);
			
			// 调用产品实时下发
			try{
				// 是否实时发送MQ 1:是、 0或空：否
				transientVars.put("IsSendMQ", "1");
				binbeifpro04_BL.tran_batchProducts(transientVars);
			}catch(Exception e){
				BatchLoggerDTO batchLoggerDTO = new BatchLoggerDTO();
				batchLoggerDTO.setCode("EKD00016");
				batchLoggerDTO.setLevel(CherryBatchConstants.LOGGER_ERROR);
				cblogger.BatchLogger(batchLoggerDTO, e);
			}
			
		} catch (Exception e) {
			// 打印错误信息
			logger.error(e.getMessage(),e);
			throw new WorkflowException();
		} finally {
			logger.info("******************************Kingdee产品导入BATCH处理结束***************************");
		}
		
	}

}
