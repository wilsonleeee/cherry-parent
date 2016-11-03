/*
 * @(#)BINBEIFEMP03_FN.java     1.0 2013/10/29
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

package com.cherry.ia.emp.function;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.ia.emp.bl.BINBEIFEMP03_BL;
import com.cherry.ia.emp.bl.BINBEIFEMP05_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

/**
 * 刷新老后台U盘绑定柜台数据FN
 * 
 * @author JiJW
 * @version 1.0 2013-10-29
 */
public class BINBEIFEMP05_FN implements FunctionProvider {
	
	private static Logger logger = LoggerFactory.getLogger(BINBEIFEMP05_FN.class.getName());

	@Resource(name="binBEIFEMP05_BL")
	private BINBEIFEMP05_BL binBEIFEMP05_BL;
	
	@Override
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		try {
			logger.info("******************************刷新老后台U盘绑定柜台数据 BATCH处理开始***************************");
			int flag = binBEIFEMP05_BL.tran_batchUDiskCounter(transientVars);
			ps.setInt("result", flag);
		} catch (Exception e) {
			// 打印错误信息
			logger.error(e.getMessage(),e);
			throw new WorkflowException();
		} finally {
			logger.info("******************************刷新老后台U盘绑定柜台数据 BATCH处理结束***************************");
		}
	}

}
