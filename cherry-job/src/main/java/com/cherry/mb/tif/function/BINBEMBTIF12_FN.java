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

import com.cherry.mb.tif.bl.BINBEMBTIF12_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 添加会员天猫加密手机号（巧迪会员通）处理FN
 * 
 * @author fxb
 * @version 1.0 2016/12/14
 */
public class BINBEMBTIF12_FN implements FunctionProvider{
	
	private static Logger logger = LoggerFactory.getLogger(BINBEMBTIF12_FN.class.getName());
	
	/** 添加会员天猫加密手机号（巧迪会员通）处理BL */
	@Resource
	private BINBEMBTIF12_BL binBEMBTIF12_BL;
	
	@Override
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		try {
			logger.info("******************************添加会员天猫加密手机号（巧迪会员通）处理开始***************************");
			int flag = binBEMBTIF12_BL.tran_addMixMobile(transientVars);
			ps.setInt("result", flag);
		} catch (Exception e) {
			// 打印错误信息
			logger.error(e.getMessage(),e);
			throw new WorkflowException();
		} finally {
			logger.info("******************************添加会员天猫加密手机号（巧迪会员通）处理结束***************************");
		}
	}

}
