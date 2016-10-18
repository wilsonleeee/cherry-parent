/*
 * @(#)CodeTableServlet.java     1.0 2010/11/16
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
package com.cherry.cm.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
/**
 * 
 * CodeTableServlet
 * 
 * 
 * 
 * @author hub
 * @version 1.0 2010.11.16
 */
@SuppressWarnings("unchecked")
public class CodeTableServlet extends HttpServlet {

	private static final long serialVersionUID = 632111374600003234L;

	public CodeTableServlet() {
		super();
	}

	public void init() {
		ServletContext servletContext = this.getServletContext();
		ApplicationContext appContext = WebApplicationContextUtils
				.getWebApplicationContext(servletContext);
		BaseConfServiceImpl baseConfServiceImpl = (BaseConfServiceImpl) appContext
				.getBean("baseConfServiceImpl");
		CodeTable codeTable = (CodeTable) appContext.getBean("CodeTable");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCMINC99.getCodeList");
		// 取得CODE值一览
		List codeList = baseConfServiceImpl.getList(paramMap);
		codeTable.setCodesMap(codeList);
		servletContext.setAttribute("CodeTable", codeTable);
	}

	public void destory() {
		super.destroy();
	}
}
