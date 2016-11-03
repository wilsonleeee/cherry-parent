package com.cherry.cm.interceptor;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

@SuppressWarnings("serial")
public class CherryExceptionInterceptor extends AbstractInterceptor{

	@Override
	public String intercept(ActionInvocation invocation) {
		try{		  
			return invocation.invoke();			 
		}catch(Exception ex){	
			invocation.getInvocationContext().getSession().put("seriousException", ex);
			return "error";
		}
	}
}
