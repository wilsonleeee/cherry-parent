/*
 * @(#)BINBEPLDPL08_FN.java     1.0 2012.10.15
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

import com.cherry.pl.dpl.bl.BINBEPLDPL04_BL;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;

/**
 * 重建部门从属关系表和索引
 * 
 * @author WangCT
 * @version 1.0 2012.10.15
 */
public class BINBEPLDPL08_FN implements FunctionProvider {

private static Logger logger = LoggerFactory.getLogger(BINBEPLDPL08_FN.class.getName());
	
	/** 实时刷新数据权限BL */
	@Resource
	private BINBEPLDPL04_BL binBEPLDPL04_BL;

	@Override
	public void execute(Map transientVars, Map args, PropertySet ps)
			throws WorkflowException {
		try {
			logger.info("******************************重建部门从属关系表和索引BATCH处理开始***************************");
			int flag = binBEPLDPL04_BL.createDepartRelationTable(transientVars);
			ps.setInt("result", flag);
		} catch (Exception e) {
			// 打印错误信息
			logger.error(e.getMessage(), e);
			throw new WorkflowException();
		} catch (Throwable t) {
			logger.error(t.getMessage(), t);
			throw new WorkflowException();
		} finally {
			logger.info("******************************重建部门从属关系表和索引BATCH处理结束***************************");
		}
	}
}
