/*
 * @(#)BINOTCOU02_FN.java     1.0 2014/11/13
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
package com.cherry.ot.cou.function;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.ot.cou.interfaces.BINOTCOU02_IF;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

/**
 * BATCH薇诺娜优惠劵获取FN
 * 
 * @author menghao
 * @version 2014.11.13
 */
public class BINOTCOU02_FN implements FunctionProvider {

	private static Logger logger = LoggerFactory.getLogger(BINOTCOU02_FN.class
			.getName());

	@Resource(name = "binOTCOU02_BL")
	private BINOTCOU02_IF binOTCOU02_BL;

	@Override
	public void execute(Map paramMap, Map arg1, PropertySet ps)
			throws WorkflowException {
		try {
			logger.info("*******************************薇诺娜优惠劵获取BATCH处理开始***************************");
			int flag = binOTCOU02_BL.tran_getCouponCode(paramMap);
			ps.setInt("result", flag);
		} catch (Exception e) {
			// 打印错误信息
			logger.error(">>>ERROR",e);
			ps.setInt("result", 1);
		} finally {
			logger.info("*******************************薇诺娜优惠劵获取BATCH处理结束***************************");
		}

	}

}
