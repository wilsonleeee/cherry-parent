package com.cherry.ct.common;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.SyncBasicHttpParams;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.DESPlus;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.client.apache4.ApacheHttpClient4;
import com.sun.jersey.client.apache4.config.DefaultApacheHttpClient4Config;

public class BINBECTCOM02 {
	
	private static Map<String, Object> webResourceMap = new HashMap<String, Object>();
	
	private static Client client;
	
	public BINBECTCOM02() {
		getClient();
	}
	
	/**
     * 创建一个Client类的实例，该实例作为静态变量只初始化一次
     * 
     * @return 返回一个Client类的实例
     */
	public Client getClient() {
		if(client == null) {
			client = ApacheHttpClient4.create(getClientConfig());
		}
		return client;
	}
	
	/**
     * 使用Client对象创建一个WebResource类的实例，可根据不同的URL创建不同的WebResponse对象
     * 
     * @param webServiceUrl 访问WebService的URL，为空的场合使用系统设定的WebServiceURL
     * @return 返回WebResource对象
     */
	public WebResource getWebResource(String webServiceUrl) {
		Client c = getClient();
		if(webResourceMap.containsKey(webServiceUrl)) {
			return (WebResource)webResourceMap.get(webServiceUrl);
		} else {
			WebResource webResource = c.resource(webServiceUrl);
			webResourceMap.put(webServiceUrl, webResource);
			return webResource;
		}
	}
	
	/**
     * 创建Client对象的配置信息对象
     * 
     * @return 配置信息对象
     */
	private ClientConfig getClientConfig() {
		
	   ClientConfig config = new DefaultApacheHttpClient4Config();
	   // 使用HttpParams对象设定jersey客户端访问WebService的ConnectionTimeout和ReadTimeout
	   HttpParams httpParams = new SyncBasicHttpParams();
	   HttpConnectionParams.setConnectionTimeout(httpParams, 60000);
	   HttpConnectionParams.setSoTimeout(httpParams, 180000);
	   
	   config.getProperties().put(DefaultApacheHttpClient4Config.PROPERTY_CHUNKED_ENCODING_SIZE, 0);
	   config.getProperties().put(DefaultApacheHttpClient4Config.PROPERTY_THREADPOOL_SIZE, 10);
	   config.getProperties().put(DefaultApacheHttpClient4Config.PROPERTY_CONNECT_TIMEOUT, 60000);
	   config.getProperties().put(DefaultApacheHttpClient4Config.PROPERTY_READ_TIMEOUT, 180000);
	   config.getProperties().put(DefaultApacheHttpClient4Config.PROPERTY_HTTP_PARAMS, httpParams);
	   config.getProperties().put(DefaultApacheHttpClient4Config.PROPERTY_CONNECTION_MANAGER, new ThreadSafeClientConnManager());
	   return config;
	}
	
	public String toUtf8String(String s) throws Exception{  
		if (s == null || s.equals("")){  
			return "";
		}
		StringBuffer sb = new StringBuffer();
		try {
			char c;  
			for(int i = 0; i < s.length(); i++){
				c = s.charAt(i);
				if (c >= 0 && c <= 255){
					sb.append(c);
				}else{
					byte[] b;
					b = Character.toString(c).getBytes("utf-8");
					for (int j = 0; j < b.length; j++) {
						int k = b[j];
						if (k < 0)
							k += 256;
						sb.append("%" + Integer.toHexString(k).toUpperCase());
					}
				}
			}
		}catch (Exception e){
			throw e;
		}
		return sb.toString();
	}
	
	// 解密处理
    @SuppressWarnings("unused")
	private String decryptPsd(String psd) throws Exception {
		// 解密
		String ret ="";
		if (psd != null && !"".equals(psd)) {
			// 解密处理
			DESPlus des = new DESPlus(CherryConstants.CUSTOMKEY);
			ret = des.decrypt(psd);
		}
		return ret;
	}
    
    // 加密处理
    @SuppressWarnings("unused")
	private String encryptPsd(String psd) throws Exception {
		// 加密
		if (psd != null && !"".equals(psd)) {
			// 加密处理
			DESPlus des = new DESPlus(CherryConstants.CUSTOMKEY);
			psd = des.encrypt(psd);
		}
		return psd;
	}
}
