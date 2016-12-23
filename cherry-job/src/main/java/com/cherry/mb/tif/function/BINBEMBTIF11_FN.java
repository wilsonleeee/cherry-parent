/*	
 * @(#)BINBEMBTIF02_FN.java     1.0 2015/06/24
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
package com.cherry.mb.tif.function;

import com.cherry.mb.tif.bl.BINBEMBTIF09_BL;
import com.cherry.mb.tif.bl.BINBEMBTIF11_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 会员信息合并处理FN
 * 
 * @author fxb
 * @version 1.0 2016/12/14
 */
public class BINBEMBTIF11_FN implements FunctionProvider{
	
	private static Logger logger = LoggerFactory.getLogger(BINBEMBTIF11_FN.class.getName());
	
	/** 会员信息合并处理BL */
	@Resource
	private BINBEMBTIF11_BL binBEMBTIF11_BL;
	
	@Override
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		try {
			logger.info("******************************会员信息合并处理开始***************************");
			int flag = binBEMBTIF11_BL.tran_MemberMerge(transientVars);
			ps.setInt("result", flag);
		} catch (Exception e) {
			// 打印错误信息
			logger.error(e.getMessage(),e);
			throw new WorkflowException();
		} finally {
			logger.info("******************************会员信息合并处理结束***************************");
		}
	}

}
