package com.cherry.cm.core;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.dispatcher.FilterDispatcher;

public class CherryFilterDispatcher extends FilterDispatcher {
	
	public void init(FilterConfig filterConfig) throws ServletException {
		super.init(filterConfig);
	}
	
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
    	throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)req;
		
		if(request.getRequestURI().contains("gadgets") 
				|| request.getRequestURI().contains("rpc")
				|| request.getRequestURI().contains("rest")
				|| request.getRequestURI().contains("xpc")
				|| request.getRequestURI().contains("pubsub")
				|| request.getRequestURI().contains("webservice")) {
			chain.doFilter(req, res);
		} else {
			super.doFilter(req, res, chain);
		}
    }

}
