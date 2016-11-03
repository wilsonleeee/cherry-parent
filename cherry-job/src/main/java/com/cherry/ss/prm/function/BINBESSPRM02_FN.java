/*
 * @(#)BINBESSPRM02_FN.java     1.0 2010/12/20
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

package com.cherry.ss.prm.function;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.ss.prm.bl.BINBESSPRM02_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

/**
 * 
 * 促销产品月度库存重算FN
 * 
 * 
 * @author ZhangJie
 * @version 1.0 2010.12.20
 */
public class BINBESSPRM02_FN implements FunctionProvider {
	
	private static Logger logger = LoggerFactory.getLogger(BINBESSPRM02_FN.class.getName());
	
	/** 促销产品月度库存重算BL */
	@Resource
	private BINBESSPRM02_BL binBESSPRM02_BL;

	@Override
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		try {
			logger.info("******************************促销产品月度库存重算BATCH处理开始***************************");
			int flag = binBESSPRM02_BL.tran_batchHistory(transientVars);
			ps.setInt("result", flag);
		} catch (Exception e) {
			// 打印错误信息
			logger.error(e.getMessage(),e);
			throw new WorkflowException();
		} finally {
			logger.info("******************************促销产品月度库存重算BATCH处理结束***************************");
		}
	}

}
