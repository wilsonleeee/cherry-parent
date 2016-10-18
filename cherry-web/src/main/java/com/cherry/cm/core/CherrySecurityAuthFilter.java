package com.cherry.cm.core;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CherrySecurityAuthFilter implements Filter {
	
	public CherrySecurityAuthFilter()
    {
    }

	@Override
	public void destroy() {
		this.forwardPath = null;
		
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		//BINOLSSPRM74 是提供给NECPOS调用的画面，不需要验证用户是否登录
		if(request.getSession().getAttribute(CherryConstants.SESSION_USERINFO) != null ||request.getRequestURI().contains("BINOLSSPRM74")) {
			chain.doFilter(request, response);
		} else {
			throw new ServletException("session timeout");
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		String _forwardPath = filterConfig.getInitParameter("forwardPath");
		if(_forwardPath != null) {
			this.forwardPath = _forwardPath;
		}
	}
	
	private String forwardPath = "index.html";

}
