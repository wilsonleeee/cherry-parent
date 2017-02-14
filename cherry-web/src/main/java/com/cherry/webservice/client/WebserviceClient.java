package com.cherry.webservice.client;

import com.cherry.cm.core.*;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.client.apache4.ApacheHttpClient4;
import com.sun.jersey.client.apache4.config.DefaultApacheHttpClient4Config;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.SyncBasicHttpParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MultivaluedMap;
import java.util.HashMap;
import java.util.Map;

public class WebserviceClient {
	
	private static Logger logger = LoggerFactory.getLogger(WebserviceClient.class.getName());
	
	private static Client client;
	
	private static Map<String, Object> webResourceMap = new HashMap<String, Object>();
	
	/**
     * 创建一个Client类的实例
     * 
     * @return 返回一个Client类的实例
     */
	public static Client createClient() {
		ClientConfig config = new DefaultApacheHttpClient4Config();
		// 使用HttpParams对象设定jersey客户端访问WebService的ConnectionTimeout和ReadTimeout
		HttpParams httpParams = new SyncBasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
		HttpConnectionParams.setSoTimeout(httpParams, 1000*60*10);
	   
		config.getProperties().put(DefaultApacheHttpClient4Config.PROPERTY_CHUNKED_ENCODING_SIZE, 0);
		config.getProperties().put(DefaultApacheHttpClient4Config.PROPERTY_THREADPOOL_SIZE, 10);
		config.getProperties().put(DefaultApacheHttpClient4Config.PROPERTY_CONNECT_TIMEOUT, 10000);
		config.getProperties().put(DefaultApacheHttpClient4Config.PROPERTY_READ_TIMEOUT, 1000*60*10);
		config.getProperties().put(DefaultApacheHttpClient4Config.PROPERTY_HTTP_PARAMS, httpParams);
		PoolingHttpClientConnectionManager  poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();
		poolingHttpClientConnectionManager.setMaxTotal(200);
		poolingHttpClientConnectionManager.setDefaultMaxPerRoute(100);		
		config.getProperties().put(DefaultApacheHttpClient4Config.PROPERTY_CONNECTION_MANAGER, poolingHttpClientConnectionManager);
		
		return ApacheHttpClient4.create(config);
	}
	
	/**
     * 使用Client对象创建一个WebResource类的实例，可根据不同的URL创建不同的WebResponse对象
     * 
     * @param webServiceUrl 访问WebService的URL，为空的场合使用系统设定的WebServiceURL
     * @return 返回WebResource对象
     */
	public static WebResource getWebResource(String webServiceUrl) {
		if(webResourceMap.containsKey(webServiceUrl)) {
			return (WebResource)webResourceMap.get(webServiceUrl);
		} else {
			synchronized (webResourceMap) {
				if (webResourceMap.containsKey(webServiceUrl)) {
					return (WebResource) webResourceMap.get(webServiceUrl);
				} else {
					if (client == null) {
						client = createClient();
					}
					WebResource webResource = client.resource(webServiceUrl);
					webResourceMap.put(webServiceUrl, webResource);
					return webResource;
				}
			}
		}
	}
	
	/**
     * 访问WebService(访问Batch的WebService)
     * 
     * @param param 访问WebService的参数
     * @return WebService的返回内容
     * @throws Exception 
     */
    public static Map<String, Object> accessBatchWebService(Map<String, Object> param) throws Exception{
    	try {
    		// 品牌代码
    		String brandCode = (String)param.get("brandCode");
    		if (CherryChecker.isNullOrEmpty(brandCode)) {
    			Map<String, Object> retMap = new HashMap<String, Object>();
    			retMap.put("ERRORCODE", "WSE9998");
    			retMap.put("ERRORMSG", "参数brandCode错误。brandCode=" + brandCode);
    			return retMap;
    		}
			BrandInfoDTO brandInfoDTO = SystemConfigManager.getBrandInfo(brandCode);
    		// 查询AES密钥
    		String AESKEY = brandInfoDTO.getAesKey();
    		if (CherryChecker.isNullOrEmpty(AESKEY)) {
    			Map<String, Object> retMap = new HashMap<String, Object>();
    			retMap.put("ERRORCODE", "WSE9996");
    			retMap.put("ERRORMSG", "品牌" + brandCode + "的密钥缺失");
    			return retMap;
    		}
    		WebResource webResource = getWebResource(PropertiesUtil.pps.getProperty("BatchWebServiceUrl"));
    		//对传递的参数进行加密
    		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
    		queryParams.add("brandCode", brandCode);
    		queryParams.add("paramData", CherryAESCoder.encrypt(CherryUtil.map2Json(param),AESKEY));
    		//TODO:临时措施，内部访问webservice时不使用动态密钥的形式。
    		queryParams.add("appID", "pekon");
    		String result = webResource.queryParams(queryParams).get(String.class);	
    		Map<String, Object> retMap = CherryUtil.json2Map(result);
    		if(retMap.get("ERRORCODE").toString().equals("0")){
    			//执行成功
    			if(retMap.containsKey("ResultContent")){
    				// 返回结果为多条数据的
    				String encryptResult = CherryAESCoder.decrypt(retMap.get("ResultContent").toString(),AESKEY);
    				retMap.put("ResultContent", CherryUtil.json2ArryList(encryptResult));
    			}else if(retMap.containsKey("ResultString")){
    				// 返回结果为字符串的 
    				String encryptResult = CherryAESCoder.decrypt(retMap.get("ResultString").toString(),AESKEY);
    				retMap.put("ResultString", encryptResult);
    			}else if(retMap.containsKey("ResultMap")){
    				// 返回结果为Map的
    				String encryptResult = CherryAESCoder.decrypt(retMap.get("ResultMap").toString(),AESKEY);
    				retMap.put("ResultMap", CherryUtil.json2Map(encryptResult));
    			}
    		}
    		return retMap;
    	} catch (Exception e) {
			logger.error("Webservice ERROR",e);
			Map<String, Object> retMap = new HashMap<String, Object>();
			retMap.put("ERRORCODE", "WSE9999");
			retMap.put("ERRORMSG", "处理过程中发生未知异常");
			return retMap;
		} catch (Throwable t) {
			logger.error("Webservice ERROR",t);
			Map<String, Object> retMap = new HashMap<String, Object>();
			retMap.put("ERRORCODE", "WSE9999");
			retMap.put("ERRORMSG", "处理过程中发生未知异常");
			return retMap;
		}
	}

	/**
	 * 访问WebService(访问Pekonws的WebService)
	 * @param  param  访问WebService的参数
	 * @param  webServiceUrl webservice访问地址
	 * @return WebService的返回内容
	 * @throws Exception
	 */
	public static Map<String, Object> accessPekonWebService(Map<String, Object> param,String webServiceUrl) throws Exception{
		try {
			// 品牌代码
			String brandCode = (String)param.get("brandCode");
			BrandInfoDTO brandInfoDTO = SystemConfigManager.getBrandInfo(brandCode);
			if (null== brandInfoDTO) {
				Map<String, Object> retMap = new HashMap<String, Object>();
				retMap.put("ERRORCODE", "WSE9998");
				retMap.put("ERRORMSG", "参数brandCode错误。brandCode=" + brandCode);
				return retMap;
			}
			// 查询AES密钥
			String AESKEY = brandInfoDTO.getAesKey();
			if (CherryChecker.isNullOrEmpty(AESKEY)) {
				Map<String, Object> retMap = new HashMap<String, Object>();
				retMap.put("ERRORCODE", "WSE9996");
				retMap.put("ERRORMSG", "品牌" + brandCode + "的密钥缺失");
				return retMap;
			}
//    		WebResource webResource = getWebResource(PropertiesUtil.pps.getProperty("PeknonWebServiceUrl"));
			WebResource webResource = getWebResource(webServiceUrl);
			//对传递的参数进行加密
			MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
			queryParams.add("brandCode", brandCode);
			queryParams.add("appID", ConvertUtil.getString(param.get("appID")));
			queryParams.add("paramData", CherryAESCoder.encrypt(CherryUtil.map2Json(param),AESKEY));
			String result = webResource.queryParams(queryParams).get(String.class);
			Map<String, Object> retMap = CherryUtil.json2Map(result);
			if(retMap.get("ERRORCODE").toString().equals("0")){
				//执行成功
				if(retMap.containsKey("ResultContent")){
					// 返回结果为多条数据的
					String encryptResult = CherryAESCoder.decrypt(retMap.get("ResultContent").toString(),AESKEY);
					retMap.put("ResultContent", CherryUtil.json2Map(encryptResult));
				}
			}
			return retMap;
		} catch (Exception e) {
			logger.error("Webservice ERROR",e);
			Map<String, Object> retMap = new HashMap<String, Object>();
			retMap.put("ERRORCODE", "WSE9999");
			retMap.put("ERRORMSG", "处理过程中发生未知异常");
			return retMap;
		} catch (Throwable t) {
			logger.error("Webservice ERROR",t);
			Map<String, Object> retMap = new HashMap<String, Object>();
			retMap.put("ERRORCODE", "WSE9999");
			retMap.put("ERRORMSG", "处理过程中发生未知异常");
			return retMap;
		}
	}
    
    
    /**
     * 访问WebService(访问微商申请的WebService)
     * @param param
     * @return
     * @throws Exception
     */
    public static Map<String, Object> accessWeshopWebService(Map<String, Object> param) throws Exception{
    	try {
    		//http://weixin2.digitalwebon.cn:7802/weshop/index.php
    		String AESKEY = PropertiesUtil.pps.getProperty("WeshopAESKEY");
    		WebResource webResource = getWebResource(PropertiesUtil.pps.getProperty("WeshopWebServiceUrl"));
    		//对传递的参数进行加密
    		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
    		queryParams.add("paramData", CherryAESCoder.encrypt(CherryUtil.map2Json(param),AESKEY));
    		//TODO:临时措施，内部访问webservice时不使用动态密钥的形式。
    		queryParams.add("a", "index");
    		String result = webResource.queryParams(queryParams).get(String.class);	
    		Map<String, Object> retMap = CherryUtil.json2Map(result);
    		if(retMap.get("ERRORCODE").toString().equals("0")){
    			//执行成功
    			if(retMap.containsKey("ResultContent")){
    				// 返回结果为多条数据的
    				String encryptResult = CherryAESCoder.decrypt(retMap.get("ResultContent").toString(),AESKEY);
    				retMap.put("ResultContent", CherryUtil.json2ArryList(encryptResult));
    			}else if(retMap.containsKey("ResultString")){
    				// 返回结果为字符串的 
    				String encryptResult = CherryAESCoder.decrypt(retMap.get("ResultString").toString(),AESKEY);
    				retMap.put("ResultString", encryptResult);
    			}else if(retMap.containsKey("ResultMap")){
    				// 返回结果为Map的
    				String encryptResult = CherryAESCoder.decrypt(retMap.get("ResultMap").toString(),AESKEY);
    				retMap.put("ResultMap", CherryUtil.json2Map(encryptResult));
    			}
    		}
    		return retMap;
    	} catch (Exception e) {
			logger.error("Webservice ERROR",e);
			Map<String, Object> retMap = new HashMap<String, Object>();
			retMap.put("ERRORCODE", "WSE9999");
			retMap.put("ERRORMSG", "处理过程中发生未知异常");
			return retMap;
		} catch (Throwable t) {
			logger.error("Webservice ERROR",t);
			Map<String, Object> retMap = new HashMap<String, Object>();
			retMap.put("ERRORCODE", "WSE9999");
			retMap.put("ERRORMSG", "处理过程中发生未知异常");
			return retMap;
		}
	}

	/**
	 * 微商城订单提货后更新微商城状态webService接口
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> accessWeshopWebService(Map<String, Object> param, String url, String aesKey) throws Exception{
		try {
			String AESKEY = PropertiesUtil.pps.getProperty("WeshopAESKEY");
			if(!CherryChecker.isNullOrEmpty(aesKey)) {
				AESKEY = aesKey;
			}

			String webResourceUrl = PropertiesUtil.pps.getProperty("WeshopWebServiceUrl");
			if(!CherryChecker.isNullOrEmpty(url)) {
				webResourceUrl = url;
			}

			WebResource webResource = getWebResource(webResourceUrl);
			//对传递的参数进行加密
			MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
			queryParams.add("paramData", CherryAESCoder.encrypt(CherryUtil.map2Json(param),AESKEY));
			//TODO:临时措施，内部访问webservice时不使用动态密钥的形式。
			queryParams.add("a", "index");
			String result = webResource.queryParams(queryParams).get(String.class);
			Map<String, Object> retMap = CherryUtil.json2Map(result);
			if(retMap.get("ERRORCODE").toString().equals("0")){
				//执行成功
				if(retMap.containsKey("ResultContent")){
					// 返回结果为多条数据的
					String encryptResult = CherryAESCoder.decrypt(retMap.get("ResultContent").toString(),AESKEY);
					retMap.put("ResultContent", CherryUtil.json2ArryList(encryptResult));
				}else if(retMap.containsKey("ResultString")){
					// 返回结果为字符串的
					String encryptResult = CherryAESCoder.decrypt(retMap.get("ResultString").toString(),AESKEY);
					retMap.put("ResultString", encryptResult);
				}else if(retMap.containsKey("ResultMap")){
					// 返回结果为Map的
					String encryptResult = CherryAESCoder.decrypt(retMap.get("ResultMap").toString(),AESKEY);
					retMap.put("ResultMap", CherryUtil.json2Map(encryptResult));
				}
			}
			return retMap;
		} catch (Exception e) {
			logger.error("Webservice ERROR",e);
			Map<String, Object> retMap = new HashMap<String, Object>();
			retMap.put("ERRORCODE", "WSE9999");
			retMap.put("ERRORMSG", "处理过程中发生未知异常");
			return retMap;
		} catch (Throwable t) {
			logger.error("Webservice ERROR",t);
			Map<String, Object> retMap = new HashMap<String, Object>();
			retMap.put("ERRORCODE", "WSE9999");
			retMap.put("ERRORMSG", "处理过程中发生未知异常");
			return retMap;
		}
	}
    
    /**
     * 访问WebService(访问微信接口的WebService)
     * @param param
     * @return
     * @throws Exception
     */
    public static Map<String, Object> accessWeChatWebService(String url, Map<String, String> param) throws Exception{
    	try {
    		WebResource webResource = getWebResource(url);
    		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
    		for(String key : param.keySet()) {
    			queryParams.add(key, param.get(key));
    		}
    		String result = webResource.queryParams(queryParams).get(String.class);	
    		return CherryUtil.json2Map(result);
    	} catch (Exception e) {
			logger.error("Webservice ERROR",e);
			Map<String, Object> retMap = new HashMap<String, Object>();
			retMap.put("errcode", "WSE9999");
			retMap.put("errmsg", "处理过程中发生未知异常");
			return retMap;
		} catch (Throwable t) {
			logger.error("Webservice ERROR",t);
			Map<String, Object> retMap = new HashMap<String, Object>();
			retMap.put("errcode", "WSE9999");
			retMap.put("errmsg", "处理过程中发生未知异常");
			return retMap;
		}
	}

	/**
	 * POST 访问WebService(访问微信接口的WebService)
	 * @param url
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> accessWeChatWebServicePost(String url, String params) throws Exception{
		try {
			WebResource webResource = getWebResource(url);
			String result = webResource.accept("application/json")
					.type("application/json").post(String.class, params);
			return CherryUtil.json2Map(result);
		} catch (Exception e) {
			logger.error("Webservice ERROR",e);
			Map<String, Object> retMap = new HashMap<String, Object>();
			retMap.put("errcode", "WSE9999");
			retMap.put("errmsg", "处理过程中发生未知异常");
			return retMap;
		} catch (Throwable t) {
			logger.error("Webservice ERROR",t);
			Map<String, Object> retMap = new HashMap<String, Object>();
			retMap.put("errcode", "WSE9999");
			retMap.put("errmsg", "处理过程中发生未知异常");
			return retMap;
		}
	}
}
