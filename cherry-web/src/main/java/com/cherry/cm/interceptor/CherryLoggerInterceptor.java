/*  
 * @(#)CherryLoggerInterceptor.java     1.0 2011/05/31      
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
package com.cherry.cm.interceptor;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryLogger;
import com.cherry.cm.util.CherryUtil;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

@SuppressWarnings("serial")
public class CherryLoggerInterceptor extends AbstractInterceptor{

	@Resource
	private CherryLogger cherryLogger;
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		// String resourceid=request.getParameter("resourceIdHidden");
		// String actionName = invocation.getInvocationContext().getName();
		String namespace = invocation.getProxy().getNamespace(); // 获取到namespace
		if ((namespace != null) && (namespace.trim().length() > 0)) {
			if ("/".equals(namespace.trim())) {
				// 说明是根路径，不需要再增加反斜杠了。
			} else {
				namespace += "/";
			}
		}
		String actionName = namespace + invocation.getProxy().getActionName();

		long startTime = System.currentTimeMillis();
		String ret = invocation.invoke();

		// TODO:debug
		long executionTime = System.currentTimeMillis() - startTime;

		String paStr = "";
		Map paMap = invocation.getInvocationContext().getParameters();
		if (null != paMap) {
			try {
				paStr = CherryUtil.map2Json(paMap);
			} catch (Exception ex) {

			}
		}

		cherryLogger.access(actionName, executionTime, paStr);
		return ret;

	}
}

