/*  
 * @(#)WebConfigListener.java     1.0 2011/05/31      
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

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;

public class WebConfigListener implements ServletContextListener{
	
	public static final String LOCATION_PREFIX_CLASSPATH = "classpath:";
	
	public static final String CONTEXT_PATH_ROOT_VAL = "ROOT";

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	

	@Override
	public void contextInitialized(ServletContextEvent arg0) {	
		PropertiesUtil.pps = new Properties();
		URL url = null;
		try{			
		 String location = arg0.getServletContext().getInitParameter("cherryconf");
		// String s2 = arg0.getServletContext().getRealPath("/");		 
		 // String p2 = (getClass().getClassLoader().getResource("").toURI()).getPath();   
		// FileInputStream fis = new FileInputStream(s2 + path); 
		// InputStreamReader read = new InputStreamReader (fis,"UTF-8");
		// PropertiesUtil.pps.load(read);
		 
		 url = Thread.currentThread()
					.getContextClassLoader()
					.getResource(location.substring(LOCATION_PREFIX_CLASSPATH.length()));
		 InputStream in = url.openStream();  
		 InputStreamReader read = new InputStreamReader(in, "UTF-8"); 
		 PropertiesUtil.pps.load(read);

		 
		 // 根目录
		 String cherryHome = PropertiesUtil.pps.getProperty(CherryConstants.CHERRY_HOME);
			Set<Map.Entry<Object, Object>> set = PropertiesUtil.pps.entrySet();
			Iterator<Map.Entry<Object, Object>> it = set.iterator();
			while(it.hasNext()) {
				Map.Entry<Object, Object> entry = it.next();
				String value = (String) entry.getValue();
				// 值中带有根目录参数
				if(value != null && value.indexOf("${" + CherryConstants.CHERRY_HOME + "}") >= 0) {
					String repVal = value.replace("${" + CherryConstants.CHERRY_HOME + "}", cherryHome);
					// 将具体的根目录路径设置到变量里
					PropertiesUtil.pps.setProperty((String) entry.getKey(), repVal);
				}
			}
			//读取DBSchemaversion文件
			location = arg0.getServletContext().getInitParameter("DBSchemaVersion");
		//	fis = new FileInputStream(s2 + path); 
		//	read = new InputStreamReader (fis,"UTF-8");
			url = Thread.currentThread()
					.getContextClassLoader()
					.getResource(location.substring(LOCATION_PREFIX_CLASSPATH.length()));
			in = url.openStream();  
			read = new InputStreamReader(in, "UTF-8"); 
			PropertiesUtil.pps.load(read);
		    read.close();
		    
		    ServletContext servletContext = arg0.getServletContext();
		    // 把项目名设置在CHERRY_CONTEXT_PATH常量中
		    servletContext.setAttribute("CHERRY_CONTEXT_PATH", getContextPath(servletContext));
		    
		    //启动线程，清除无效链接
		    HttpClientConnectionManager manager = new BasicHttpClientConnectionManager();  	          
	        new IdleConnectionMonitorThread(manager).start(); 
	        
		}catch (Exception e) {   
		 e.printStackTrace(); 
	}finally{
		// read.close();
	}		
}
	
	protected String getContextPath(ServletContext sc)
	{
		String cp = sc.getContextPath();

		if (cp.startsWith("/"))
			cp = cp.substring(1);

		if ("".equals(cp))
			cp = CONTEXT_PATH_ROOT_VAL;

		return cp;
	}
}
