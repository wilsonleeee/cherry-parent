/*
 * @(#)BINBEPLDPL06_FN.java     1.0 2012.04.12
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
package com.cherry.pl.dpl.function;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.pl.dpl.bl.BINBEPLDPL03_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

/**
 * 给人员权限表创建索引
 * 
 * @author WangCT
 * @version 1.0 2012.04.12
 */
public class BINBEPLDPL06_FN implements FunctionProvider {
	
	private static Logger logger = LoggerFactory.getLogger(BINBEPLDPL06_FN.class.getName());
	
	/** 权限表维护共通BL */
	@Resource
	private BINBEPLDPL03_BL binBEPLDPL03_BL;

	@Override
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
//		try {
//			logger.info("******************************给人员权限表创建索引BATCH处理开始***************************");
//			int flag = binBEPLDPL03_BL.tran_createEmployeePrivilegeIndex(transientVars);
//			ps.setInt("result", flag);
//		} catch (Exception e) {
//			// 打印错误信息
//			logger.error(e.getMessage());
//			throw new WorkflowException();
//		} finally {
//			logger.info("******************************给人员权限表创建索引BATCH处理结束***************************");
//		}
		ps.setInt("result", CherryBatchConstants.BATCH_SUCCESS);
	}

}
