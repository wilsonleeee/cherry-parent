/*  
 * @(#)CherryUrlFilter.java     1.0 2011/05/31      
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
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * CherryUrlFilter
 * 
 * 
 * 
 * @author hub
 * @version 1.0 2011.05.31
 */
public class CherryUrlFilter
    implements Filter
{

    public CherryUrlFilter()
    {
    }

    public void destroy()
    {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException
    {
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        HttpServletResponse httpResponse = (HttpServletResponse)response;
        // 获取HTTP头文件
        String requestAddressUrl = null;
        if (null != httpRequest.getRequestURL()) {
        	requestAddressUrl = httpRequest.getRequestURL().toString();
        }
        if(requestAddressUrl == null) {
            return;
        }
        if(urlRegex != null && !"".equals(urlRegex) && requestAddressUrl.replaceAll("[/]{1,}", "/").matches(urlRegex))
        {
            String requestHeaderUrl = httpRequest.getHeader("referer");
            if(urlAllow != null && !"".equals(urlAllow))
            {
                String requestUrl = httpRequest.getRequestURI();
                // 请求地址开始字符串
                String urlStartStr = requestAddressUrl.substring(0, requestAddressUrl.indexOf(requestUrl));
                boolean isAllow = false;
                // 如果请求头文件 在允许请求范围内
                if(requestHeaderUrl != null && requestHeaderUrl.startsWith((new StringBuilder(String.valueOf(urlStartStr))).append(urlAllow.trim()).toString())) {
                    isAllow = true;
                }
                if(!isAllow)
                {
                    if(urlError == null || "".equals(urlError)) {
                        httpResponse.sendError(404);
                    } else {
                        httpResponse.sendRedirect((new StringBuilder(String.valueOf(urlStartStr))).append(urlError.trim()).toString());
                    }
                    return;
                }
            }
        }
        chain.doFilter(httpRequest, httpResponse);
    }

    public void init(FilterConfig fc)
        throws ServletException
    {
        if(initFlg) {
            return;
        }
        urlRegex = fc.getInitParameter("urlRegex");
        urlAllow = fc.getInitParameter("urlAllow");
        urlError = fc.getInitParameter("urlError");
        initFlg = true;
    }
    /** 允许访问地址 */
    protected static String urlAllow = null;
    /** 错误页面 */
    protected static String urlError = null;
    /** 加载区分 */
    protected static boolean initFlg = false;
    /** 地址过滤条件 */
    protected static String urlRegex = null;

}