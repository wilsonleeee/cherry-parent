/*
 * @(#)BINOLCM38_BL.java     1.0 2013/06/14
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.SyncBasicHttpParams;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbussiness.service.BINOLCM38_Service;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.DESPlus;
import com.cherry.cm.util.ConvertUtil;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.client.apache4.ApacheHttpClient4;
import com.sun.jersey.client.apache4.config.DefaultApacheHttpClient4Config;
import com.sun.jersey.core.util.MultivaluedMapImpl;

/**
 * 沟通模块第三方接口共通
 * 
 * @author ZhangGS
 * @version 1.0 2013.06.14
 */
public class BINOLCM38_BL {
	
	@Resource
	private BINOLCM38_Service binOLCM38_Service;
	
	private static Logger logger = LoggerFactory.getLogger(BINOLCM38_BL.class.getName());
	
	private static Map<String, Object> webResourceMap = new HashMap<String, Object>();
	
	private static Client client;
	
	public BINOLCM38_BL() {
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
     * 访问第三方短信接口
     * 
     * @param
     * 		webServiceUrl 访问接口的URL
     * 		map 访问接口的参数
     * @return 接口的返回内容
     * @throws Exception 
     */
	public String sendMsgWebService(String webServiceUrl, Map<String, Object> map) throws Exception {
		
		try {
			if(null != webServiceUrl && !"".equals(webServiceUrl)){
				WebResource webResource = getWebResource(webServiceUrl);
				// 访问WebService的参数设定
				MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
				queryParams.add("userid", ConvertUtil.getString(map.get("userid")));
				queryParams.add("password", ConvertUtil.getString(map.get("password")));
				queryParams.add("destnumbers", ConvertUtil.getString(map.get("destnumbers")));
				queryParams.add("msg", ConvertUtil.getString(map.get("msg")));
				queryParams.add("sendtime", ConvertUtil.getString(map.get("sendtime")));
				// 访问WebService
				String result = webResource.queryParams(queryParams).get(String.class);
				
				List<Map<String, Object>> resultMap = xmlElements(result);
				
				// 取得WebService返回状态
//				String state = resultMap.get("return");
//				// WebService返回状态为error的场合
//				if("ERROR".equals(state)) {
//					String errorCode = (String)resultMap.get("ErrorCode");
//					String errorMes = (String)resultMap.get("Data");
//					if(errorMes != null && !"".equals(errorMes)) {
//						if(errorCode != null && !"".equals(errorCode)) {
//							errorMes = errorCode + ":" + errorMes;
//						}
//					} else {
//						if(errorCode != null && !"".equals(errorCode)) {
//							errorMes = errorCode;
//						}
//					}
//					logger.error(errorMes);
//				}
				return result;
			}else{
				return null;
			}
		} catch (Exception e) {
			logger.error("SendMsgWebservice ERROR",e);
			throw new CherryException("ECM00065", e);
		} catch (Throwable t) {
			logger.error("SendMsgWebservice ERROR",t);
		}
		return null;
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
	
    @SuppressWarnings("rawtypes")
	public List<Map<String, Object>> xmlElements(String xmlDoc) throws Exception {
    	List<Map<String, Object>> mapList = new ArrayList<Map<String,Object>>();
    	Document doc = DocumentHelper.parseText(xmlDoc);
		XPath xpathSelector = DocumentHelper.createXPath("root");
		List nodes = xpathSelector.selectNodes(doc);
		for (Object obj : nodes) {
			Element nts = (Element) obj;
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("returnValue", nts.attribute("return").getText());
			map.put("info", nts.attribute("info").getText());
			map.put("msgid", nts.attribute("msgid").getText());
			map.put("numbers", nts.attribute("numbers").getText());
			map.put("messages", nts.attribute("messages").getText());
			mapList.add(map);
		}
		return mapList;
    }
    
    // 获取接口配置信息
    public List<Map<String, Object>> getIFConfigInfoList(Map<String, Object> map) throws Exception{
		// 获取接口配置信息列表
		List<Map<String, Object>> configList = binOLCM38_Service.getIFConfigInfo(map);
		return configList;
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
