package com.cherry.cm.core;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;

public class WebConfigListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		PropertiesUtil.pps = new Properties();
		URL url = null;
		try {
//			String path = null;
			
//				path = arg0.getServletContext().getRealPath("/") + 
//					arg0.getServletContext().getInitParameter("cherryconf");
				
				url = Thread.currentThread()
						.getContextClassLoader()
						.getResource(arg0.getServletContext().getInitParameter("cherryconf"));
			 /*else {
				path ="src/main/conf/properties/CherryBatch.properties";
			}*/
			InputStream fis =url.openStream();  
			InputStreamReader read = new InputStreamReader(fis, "UTF-8");
			PropertiesUtil.pps.load(read);
			// 根目录
			 String cherryHome = PropertiesUtil.pps.getProperty(CherryBatchConstants.CHERRY_HOME);
				Set<Map.Entry<Object, Object>> set = PropertiesUtil.pps.entrySet();
				Iterator<Map.Entry<Object, Object>> it = set.iterator();
				while(it.hasNext()) {
					Map.Entry<Object, Object> entry = it.next();
					String value = (String) entry.getValue();
					// 值中带有根目录参数
					if(value != null && value.indexOf("${" + CherryBatchConstants.CHERRY_HOME + "}") >= 0) {
						String repVal = value.replace("${" + CherryBatchConstants.CHERRY_HOME + "}", cherryHome);
						// 将具体的根目录路径设置到变量里
						PropertiesUtil.pps.setProperty((String) entry.getKey(), repVal);
					}
				}
			 read.close();
			 
		//启动线程，清除无效链接
		 HttpClientConnectionManager manager = new BasicHttpClientConnectionManager();  	          
		 new IdleConnectionMonitorThread(manager).start(); 
		        
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
