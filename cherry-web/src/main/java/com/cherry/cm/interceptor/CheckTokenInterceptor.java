/*  
 * @(#)CheckTokenInterceptor.java     1.0 2011/05/31      
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

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.core.CherryException;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;

public class CheckTokenInterceptor extends MethodFilterInterceptor {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2404681786090505113L;

	private static Logger logger = LoggerFactory.getLogger(CheckTokenInterceptor.class.getName());

	@Override
	protected String doIntercept(ActionInvocation invocation) throws Exception {
		try {
			String tokenSession = String.valueOf(invocation.getInvocationContext().getSession().get("hiscsrftoken"));
			HttpServletRequest request = (HttpServletRequest) invocation.getInvocationContext().get("com.opensymphony.xwork2.dispatcher.HttpServletRequest");
			String tokenForm = request.getParameter("csrftoken");
			String code = request.getParameter("code");
			String codeSession = String.valueOf(invocation.getInvocationContext().getSession().get("code"));
			String ret = "error";

			String namespace = invocation.getProxy().getNamespace(); // 获取到namespace
			if ((namespace != null) && (namespace.trim().length() > 0)) {
				if ("/".equals(namespace.trim())) {
					// 说明是根路径，不需要再增加反斜杠了。
				} else {
					namespace += "/";
				}
			}
			String URL = namespace + invocation.getProxy().getActionName();

			URL += ".action";

			if ((tokenSession == null || "null".equals(tokenSession) || "".equals(tokenSession)) && "/login.action".equals(URL)) {
				return invocation.invoke();
			}
			
			if(code!=null &&!"".equals(code) && code.equals(codeSession)){
				return invocation.invoke();
			}
			
			if (tokenForm == null || tokenForm.length() < 32 || tokenSession.indexOf(tokenForm) < 0) {
				log.error("Token ERROR:URL = "+URL);
				log.error("Token ERROR:tokenSession = "+tokenSession);
				log.error("Token ERROR:tokenForm = "+tokenForm);
				throw new CherryException("ECM00001");
			} else {
				// 提交的token在session中保存的5个之内，但不是最新的一个，就要比较时间
				if (!tokenSession.endsWith(tokenForm+",")) {
					String lastTimeStr = String.valueOf(invocation.getInvocationContext().getSession().get("lastrequesttime"));
					long lastTime = Long.parseLong(lastTimeStr);
					long nowTime = System.currentTimeMillis();
					if(nowTime - lastTime >30000){
						log.error("Token ERROR:URL = "+URL);
						log.error("Token ERROR:tokenSession = "+tokenSession);
						log.error("Token ERROR:tokenForm = "+tokenForm);
						log.error("Token ERROR:period= "+(nowTime - lastTime));
						throw new CherryException("ECM00001");
					}
				}
				// System.out.println(tokenForm);
				// System.out.println(tokenSession);
				// System.out.println(tokenSession.indexOf(","+tokenForm+","));
				ret = invocation.invoke();
				invocation.getInvocationContext().getSession().put("lastrequesttime", System.currentTimeMillis());
			}
			return ret;
		} catch (Exception e) {
			log.error("ERROR:", e);
			throw  e;
		}
	}
}
