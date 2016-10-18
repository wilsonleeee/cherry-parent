/*
 * @(#)CustomerSessionFilter.java     1.0 2011/05/30
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

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.core.CherryConstants;

public class CustomerSessionFilter implements Filter {
    //~ Static fields/initializers =====================================================================================

    protected static final Logger logger = LoggerFactory.getLogger(CustomerSessionFilter.class);
    static final String FILTER_APPLIED = "__customer_session_filter_applied";

    //~ Instance fields ================================================================================================

    public CustomerSessionFilter() throws ServletException {
        
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
 ServletException {
    	
    	HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		try {

			// Assert.isInstanceOf(HttpServletRequest.class, req,
			// "ServletRequest must be an instance of HttpServletRequest");
			// Assert.isInstanceOf(HttpServletResponse.class, res,
			// "ServletResponse must be an instance of HttpServletResponse");


				logger.debug("开始当前线程：" + Thread.currentThread().getId()
						+ "URL:" + ((HttpServletRequest) req).getRequestURL() + "数据源："
						+ CustomerContextHolder.getCustomerDataSourceType());
			

			if (request.getAttribute(FILTER_APPLIED) != null) {
				// ensure that filter is only applied once per request
				chain.doFilter(request, response);

				return;
			}

			HttpSession httpSession = null;

			try {
				httpSession = request.getSession();
			} catch (IllegalStateException ignored) {
			}

			// boolean httpSessionExistedAtStartOfRequest = httpSession != null;

			String contextBeforeChainExecution = readCustomerContextFromSession(httpSession);

			// Make the HttpSession null, as we don't want to keep a reference
			// to it lying
			// around in case chain.doFilter() invalidates it.
			httpSession = null;

			if (contextBeforeChainExecution == null) {
				// 确保清空线程本地变量，预防有些时候从线程池中拿到的线程缓存了值
				CustomerContextHolder.clearCustomerDataSourceType();

				// 没有从Session中取到数据源值，说明用户未登录，直接进入过滤链的下一步
				chain.doFilter(request, response);

				return;
			}

			request.setAttribute(FILTER_APPLIED, Boolean.TRUE);

			// Proceed with chain

			// This is the only place in this class where
			// CustomerContextHolder.setContext() is called
			CustomerContextHolder
					.setCustomerDataSourceType(contextBeforeChainExecution);

			chain.doFilter(request, response);
		} finally {
			// Crucial removal of CustomerContextHolder contents - do this
			// before anything else.
			CustomerContextHolder.clearCustomerDataSourceType();

			request.removeAttribute(FILTER_APPLIED);

				logger.debug("结束当前线程：" + Thread.currentThread().getId()
						+ " 数据源："
						+ CustomerContextHolder.getCustomerDataSourceType());
				logger.debug("本次请求结束，CustomerContextHolder被清除 ");
		}
	}

    /**
     * Gets the security context from the session (if available) and returns it.
     * <p/>
     * If the session is null, the context object is null or the context object stored in the session
     * is not an instance of SecurityContext it will return null.
     * <p/>
     * If <tt>cloneFromHttpSession</tt> is set to true, it will attempt to clone the context object
     * and return the cloned instance.
     *
     * @param httpSession the session obtained from the request.
     */
    private String readCustomerContextFromSession(HttpSession httpSession) {
        if (httpSession == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("No HttpSession currently exists");
            }
            return null;
        }

        // Session exists, so try to obtain a context from it.
        Object contextFromSessionObject = httpSession.getAttribute(CherryConstants.CHERRY_SECURITY_CONTEXT_KEY);

        if (contextFromSessionObject == null) {

                logger.debug("HttpSession returned null object for CHERRY_SECURITY_CONTEXT_KEY");
 
            return null;
        }

        // We now have the security context object from the session.
        // Everything OK. The only non-null return from this method.
        return (String) contextFromSessionObject;
    }

    /**
     * Does nothing. We use IoC container lifecycle services instead.
     *
     * @param filterConfig ignored
     * @throws ServletException ignored
     */
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    /**
     * Does nothing. We use IoC container lifecycle services instead.
     */
    public void destroy() {
    }



}
