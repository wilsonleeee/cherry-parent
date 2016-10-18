package com.cherry.webserviceout.common;

import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.SyncBasicHttpParams;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.client.apache4.ApacheHttpClient4;
import com.sun.jersey.client.apache4.config.DefaultApacheHttpClient4Config;

/**
 * http访问可以用此类创建client
 * @author dingyongchang
 *
 */
public class JerseyWebserviceClient{
	
	public static Client getClient(final String wsUsername,final String wsPassword){
		ClientConfig config = new DefaultApacheHttpClient4Config();
		// 使用HttpParams对象设定jersey客户端访问WebService的ConnectionTimeout和ReadTimeout
		HttpParams httpParams = new SyncBasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
		HttpConnectionParams.setSoTimeout(httpParams, 10000);
		config.getProperties().put(DefaultApacheHttpClient4Config.PROPERTY_CHUNKED_ENCODING_SIZE, 0);
		config.getProperties().put(DefaultApacheHttpClient4Config.PROPERTY_THREADPOOL_SIZE, 10);
		config.getProperties().put(DefaultApacheHttpClient4Config.PROPERTY_CONNECT_TIMEOUT, 10000);
		config.getProperties().put(DefaultApacheHttpClient4Config.PROPERTY_READ_TIMEOUT, 10000);
		config.getProperties().put(DefaultApacheHttpClient4Config.PROPERTY_HTTP_PARAMS, httpParams);
		ThreadSafeClientConnManager threadSafeClientConnManager = new ThreadSafeClientConnManager();
		threadSafeClientConnManager.setMaxTotal(200);
		threadSafeClientConnManager.setDefaultMaxPerRoute(100);
		config.getProperties().put(DefaultApacheHttpClient4Config.PROPERTY_CONNECTION_MANAGER, threadSafeClientConnManager);
//		if("true".equalsIgnoreCase(MulberryConfig.getSystemConfig("WechatUseProxy"))){		
//			String host = MulberryConfig.getSystemConfig( "ProxyURI");
//			String port = MulberryConfig.getSystemConfig( "ProxyPort");			
//			String domain = MulberryConfig.getSystemConfig("ProxyDomain");
//			String userName=MulberrySecret.decryptDES(MulberryConfig.getSystemConfig("ProxyUserName"));
//			String password=MulberrySecret.decryptDES(MulberryConfig.getSystemConfig("ProxyPassword"));
//			if(domain!=null&&!"".equals(domain)){
//				userName=domain+"\\"+userName;
//			}
//			
//			config.getProperties().put(DefaultApacheHttpClient4Config.PROPERTY_PROXY_URI, host+":"+port);
//			config.getProperties().put(DefaultApacheHttpClient4Config.PROPERTY_PROXY_USERNAME, userName);
//			config.getProperties().put(DefaultApacheHttpClient4Config.PROPERTY_PROXY_PASSWORD, password);			
//		}
		Client client = ApacheHttpClient4.create(config);
		//client.addFilter(new HTTPBasicAuthFilter(wsUsername, wsPassword));
		return client;		
	}
	
}
