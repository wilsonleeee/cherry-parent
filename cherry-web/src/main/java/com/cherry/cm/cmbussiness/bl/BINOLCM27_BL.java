/*      
 * @(#)BINOLCM27_BL.java     1.0 2012/08/24        
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
package com.cherry.cm.cmbussiness.bl;

import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.DESPlus;
import com.cherry.cm.core.SystemConfigManager;
import com.cherry.cm.util.CherryUtil;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.client.apache4.ApacheHttpClient4;
import com.sun.jersey.client.apache4.config.DefaultApacheHttpClient4Config;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.SyncBasicHttpParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MultivaluedMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 访问WebService共通BL
 * 
 * @author WangCT
 * @version 1.0 2012/08/24
 */
public class BINOLCM27_BL {
	
	private static Logger logger = LoggerFactory.getLogger(BINOLCM27_BL.class.getName());
	
	private static Client client;
	
	private static Map<String, Object> webResourceMap = new HashMap<String, Object>();
	
	public BINOLCM27_BL() {
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
	
	/**
     * 使用Client对象创建一个WebResource类的实例，可根据不同的URL创建不同的WebResponse对象
     * 
     * @param webServiceUrl 访问WebService的URL，为空的场合使用系统设定的WebServiceURL
     * @return 返回WebResource对象
     */
	public WebResource getWebResource(String webServiceUrl) {
		Client c = getClient();
		if(webServiceUrl == null || "".equals(webServiceUrl)) {
			webServiceUrl = SystemConfigManager.getWebserviceConfigDTO("oldws").getWebserviceURL();
		}
		if(webResourceMap.containsKey(webServiceUrl)) {
			return (WebResource)webResourceMap.get(webServiceUrl);
		} else {
			WebResource webResource = c.resource(webServiceUrl);
			webResourceMap.put(webServiceUrl, webResource);
			return webResource;
		}
	}
	
	/**
     * 访问WebService（使用系统设定的WebServiceURL）
     * 
     * @param
     * 		map 访问WebService的参数
     * @return WebService的返回内容
     * @throws Exception 
     */
	public Map<String, Object> accessWebService(Map<String, Object> map) throws Exception {
		return this.accessWebService(null, map);
	}
	
	/**
     * 访问WebService
     * 
     * @param
     * 		webServiceUrl 访问WebService的URL
     * 		map 访问WebService的参数
     * @return WebService的返回内容
     * @throws Exception 
     */
	@SuppressWarnings("unchecked")
	public Map<String, Object> accessWebService(String webServiceUrl, Map<String, Object> map) throws Exception {
		
		try {
			WebResource webResource = getWebResource(webServiceUrl);
			// 访问WebService时参数加密的固定密钥
			String webServiceKey = SystemConfigManager.getWebserviceConfigDTO("oldws").getSecretKey();// PropertiesUtil.pps.getProperty("WebServiceKey");
			// 随机的8位数字
			String key = getRandomString(8);
			// 使用随机的8位数字 + 固定密钥作为真正的密钥对参数进行加密处理
			DESPlus des = new DESPlus(key + webServiceKey);
			String praData =  des.encrypt(CherryUtil.map2Json(map));
			// 访问WebService的参数设定
			MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
			queryParams.add("key", key);
			queryParams.add("praData", praData);
			logger.debug("key="+key+" praData="+praData);
			// 访问WebService
			String result = webResource.queryParams(queryParams).get(String.class);
			
			Map<String, Object> resultMap = CherryUtil.json2Map(result);
			// 取得WebService返回状态
			String state = (String)resultMap.get("State");
			// WebService返回状态为error的场合
			if("ERROR".equals(state)) {
				String errorCode = (String)resultMap.get("ErrorCode");
				String errorMes = (String)resultMap.get("Data");
				if(errorMes != null && !"".equals(errorMes)) {
					if(errorCode != null && !"".equals(errorCode)) {
						errorMes = errorCode + ":" + errorMes;
					}
				} else {
					if(errorCode != null && !"".equals(errorCode)) {
						errorMes = errorCode;
					}
				}
				logger.error(errorMes);
			}
			return resultMap;
		} catch (Exception e) {
			logger.error("Webservice ERROR",e);
			throw new CherryException("ECM00065", e);
		} catch (Throwable t) {
			logger.error("Webservice ERROR",t);
		}
		return null;
	}

	/** 
     * 产生一个随机的字符串
     *   
     * @param length 字符串长度
     * @return 随机的字符串 
     */ 
    public String getRandomString(int length) {
        String base = "0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

}
