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

/*
 * @(#)CherryUrlFilter.java     1.0 2010/12/13
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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * CherryUrlFilter
 * 
 * 
 * 
 * @author hub
 * @version 1.0 2010.12.13
 */
public class CherryUrlFilter implements Filter {

	/** 允许访问地址列表 */
	protected static List<String> urlAllow = new ArrayList<String>();

	/** 错误页面 */
	protected static String urlError = "";

	/** 加载区分 */
	protected static boolean initFlg = false;

	/** 地址过滤条件 */
	protected static String urlRegex = null;

	public void destroy() {

	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		// 获取HTTP头文件
		String requestAddressUrl = httpRequest.getRequestURL().toString();
		if (null == requestAddressUrl) {
			return;
		}
		if (null != urlRegex && !"".equals(urlRegex)) {
			if (requestAddressUrl.replaceAll("[/]{1,}", "/").matches(urlRegex)) {
				String requestHeaderUrl = httpRequest.getHeader("referer");
				// 请求地址开始字符串
				String startStr = httpRequest.getScheme() + "://"
						+ httpRequest.getServerName() + ":"
						+ httpRequest.getServerPort();
				String requestAddress = requestAddressUrl.replace(startStr, "")
						.replaceAll("[/]{1,}", "/");
				String requestHeader = null;
				if (null != requestHeaderUrl) {
					requestHeader = requestHeaderUrl.replace(startStr, "").replaceAll(
							"[/]{1,}", "/");
				}
				if (!requestAddress.startsWith(urlError.trim())) {
					boolean isAllow = false;
					if (null != requestHeader) {
						// 如果请求头文件 在允许请求范围内
						for (int j = 0; j < urlAllow.size(); j++) {
							if (requestHeader.startsWith(urlAllow.get(j).toString()
									.trim())) {
								isAllow = true;
							}
						}
					}
					if (!isAllow) {
						if (null == urlError || "".equals(urlError)) {
							httpResponse.sendError(404);
						} else {
							httpResponse.sendRedirect(startStr + urlError.trim());
						}
						return;
					}
				}
			}
		}
		chain.doFilter(httpRequest, httpResponse);
	}

	public void init(FilterConfig fc) throws ServletException {
		if (initFlg) {
			return;
		}
		try {
			urlRegex = fc.getInitParameter("urlRegex");
			// TOMCAT配置路径
			String tomcatHome = System.getenv("TOMCAT_HOME");
			String fp = tomcatHome + System.getProperty("file.separator")
					+ "webapps" + System.getProperty("file.separator") + "ROOT"
					+ System.getProperty("file.separator")
					+ "filter.properties";
			File file = new File(fp);
			Properties properties = new Properties();
			properties.load(new FileInputStream(file));
			// 根目录
			String urlroot = properties.getProperty("url.allow");
			if (urlroot != null) {
				String[] allow = urlroot.split(",");
				for (int i = 0; i < allow.length; i++) {
					urlAllow.add(((String) allow[i]).trim());
				}
			}
			// 文件错误地址
			urlError = properties.getProperty("url.error");
			initFlg = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
