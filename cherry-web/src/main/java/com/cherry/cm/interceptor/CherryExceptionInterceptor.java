/*  
 * @(#)CherryExceptionInterceptor.java     1.0 2011/05/31      
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
import java.lang.reflect.InvocationTargetException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.core.CherryException;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

@SuppressWarnings("serial")
public class CherryExceptionInterceptor extends AbstractInterceptor{
	private static Logger logger = LoggerFactory.getLogger(CherryExceptionInterceptor.class.getName());
	@Override
	public String intercept(ActionInvocation invocation) {
		try{		  
			return invocation.invoke();			 
		}catch(Exception ex){
			ex.printStackTrace();
			invocation.getInvocationContext().getSession().put("seriousException", ex);
			if(ex instanceof CherryException){
				CherryException temp = (CherryException)ex;	
				logger.error(temp.getErrMessage());	
			}else if(ex.getCause() instanceof CherryException){
				CherryException temp = (CherryException)(ex.getCause());
				logger.error(temp.getErrMessage());	
			}else if(ex instanceof InvocationTargetException){
				Throwable targetEx = ((InvocationTargetException) ex).getTargetException();
	            if (targetEx != null)
	            {
	            	logger.error(targetEx.getMessage(),targetEx);
	            }
			}else{
				logger.error(ex.getMessage(),ex);
			}
			return "globalError";
		}
	}
}
