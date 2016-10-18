/*  
 * @(#)Cherryi18nInterceptor     1.0 2011/05/31      
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

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.core.CherryConstants;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

@SuppressWarnings("serial")
public class Cherryi18nInterceptor extends AbstractInterceptor{
	private static Logger logger = LoggerFactory.getLogger(Cherryi18nInterceptor.class.getName());
	@Override
	public String intercept(ActionInvocation invocation) throws Exception{
			
//		Map<String, Object> map = 	invocation.getInvocationContext().getParameters();
//		//TODO:暂时限定为中文，以后放开
//		if(!map.containsKey("request_locale")){
//				HttpServletRequest request = (HttpServletRequest)invocation.getInvocationContext().get("com.opensymphony.xwork2.dispatcher.HttpServletRequest");
//				Object ob = request.getSession().getAttribute(CherryConstants.SESSION_LANGUAGE);
//				if(ob!=null){
//					map.put("request_locale", String.valueOf(ob));
//				}
//		}
		
//		map.put("request_locale", "zh_CN");
		return invocation.invoke();	
	}	
}
