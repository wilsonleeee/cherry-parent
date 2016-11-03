package com.cherry.webservice.common;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


import com.cherry.cm.core.CherryAESCoder;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CustomerContextHolder;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.core.SpringBeanManager;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;

@Path("/cherryws")
public class WebserviceEntrance implements ApplicationContextAware{
	
	private static Logger logger = LoggerFactory.getLogger(WebserviceEntrance.class.getName());
	
	private static ApplicationContext applicationContext; 
	
	@Resource(name = "webserviceDataSource")
	private WebserviceDataSource webserviceDataSource;
	
	private static ConcurrentHashMap<String,TradeConfig> tradeConfigMap;	
	
	private static HashMap<String,ThirdPartyConfig> thirdPartyConfigMap = new HashMap<String, ThirdPartyConfig>();;

		
	/**
	 * 以GET方式访问webservice
	 * @param brandCode
	 * @param paramData
	 * @param appID
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@GET
	public String getWebservice(@QueryParam("brandCode") String brandCode, @QueryParam("paramData") String paramData, @QueryParam("appID") String appID, @Context HttpServletRequest request) throws Exception {
		return doWebservice(brandCode, paramData,  appID,  request);
	}
	
	/**
	 * 以POST方式访问webservice
	 * @param brandCode
	 * @param paramData
	 * @param appID
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@POST
	 public String postWebservice(@FormParam("brandCode") String brandCode, @FormParam("paramData") String paramData, @FormParam("appID") String appID, @Context HttpServletRequest request) throws Exception {
			return doWebservice(brandCode, paramData,  appID,  request);
	}
	
	private String doWebservice(String brandCode, String paramData,  String appID, HttpServletRequest request) throws Exception {
		Map<String, Object> retMap = new HashMap<String, Object>();
		String retString = "";
		try {
			if (CherryChecker.isNullOrEmpty(brandCode)) {
				retMap.put("ERRORCODE", "WSE9998");
				retMap.put("ERRORMSG", "参数brandCode错误。brandCode=" + brandCode);
				retString = CherryUtil.map2Json(retMap);
				logger.error(retString);
				return retString;
			}

			if (CherryChecker.isNullOrEmpty(paramData)) {
				retMap.put("ERRORCODE", "WSE9997");
				retMap.put("ERRORMSG", "参数paramData错误。paramData=" + paramData);
				retString = CherryUtil.map2Json(retMap);
				logger.error(retString);
				return retString;
			}

			// 设置数据源
			if (!webserviceDataSource.setBrandDataSource(brandCode)) {
				retMap.put("ERRORCODE", "WSE9998");
				retMap.put("ERRORMSG", "参数brandCode错误。brandCode=" + brandCode);
				retString = CherryUtil.map2Json(retMap);
				logger.error(retString);
				return retString;
			}

			// 查询AES密钥
			String AESKEY = "";
			if ("true".equals(PropertiesUtil.pps.getProperty("WS_SafeModel_Flag", "false"))) {
				// 如果开启了动态密钥模式
				if (CherryChecker.isNullOrEmpty(appID)) {
					retMap.put("ERRORCODE", "WSE9993");
					retMap.put("ERRORMSG", "参数appID错误。appID=" + paramData);
					retString = CherryUtil.map2Json(retMap);
					logger.error(retString);
					return retString;
				}
				// 检查appID是否存在，不存在则尝试刷新一次
				if (null == thirdPartyConfigMap || !thirdPartyConfigMap.containsKey(appID)) {
					thirdPartyConfigMap = webserviceDataSource.getThirdPartyConfigList();
				}
				if (!thirdPartyConfigMap.containsKey(appID)) {
					retMap.put("ERRORCODE", "WSE9990");
					retMap.put("ERRORMSG", "无效的AppID");
					retString = CherryUtil.map2Json(retMap);
					logger.error(retString);
					return retString;
				}
				ThirdPartyConfig config = thirdPartyConfigMap.get(appID);
				String oldKey = config.getDynamicAESKey();
				String expireTime = config.getAesKeyExpireTime();
				Calendar calendar = Calendar.getInstance();
				SimpleDateFormat format = new SimpleDateFormat(DateUtil.DATETIME_PATTERN);
				String nowtime = format.format(calendar.getTime());
				if (expireTime.compareTo(nowtime) < 0) {
					// 密钥过期 重新从数据库中获取一次，刷新AES的动作是在Cherry中完成的，Batch这边只有主动获取
					thirdPartyConfigMap = webserviceDataSource.getThirdPartyConfigList();
					config = thirdPartyConfigMap.get(appID);
					if (oldKey.equals(config.getDynamicAESKey())) {
						// 如果重新取出的Key仍然和上一次的key相同，则说明系统没有刷新过，报错处理
						retMap.put("ERRORCODE", "WSE9995");
						retMap.put("ERRORMSG", "密钥无效或已过期");
						retString = CherryUtil.map2Json(retMap);
						logger.error(retString);
						return retString;
					}
				}
				AESKEY = config.getDynamicAESKey();
			} else {
				AESKEY = webserviceDataSource.getAESKey(brandCode);
			}
			if (CherryChecker.isNullOrEmpty(AESKEY)) {
				retMap.put("ERRORCODE", "WSE9996");
				retMap.put("ERRORMSG", "品牌" + brandCode + "的密钥缺失");
				retString = CherryUtil.map2Json(retMap);
				logger.error(retString);
				return retString;
			}

			// 查询到组织ID品牌ID
			Map<String, Object> brandMap = new HashMap<String, Object>();
			brandMap.put("brandCode", brandCode);
			webserviceDataSource.getBrandInfo(brandMap);

			// 密文参数
			String origianParamData = paramData;
			try {
				// AES解密,将解密后的JSON字符串转换成Map
				paramData = CherryAESCoder.decrypt(paramData, AESKEY);
			} catch (Exception ex) {
				retMap.put("ERRORCODE", "WSE9995");
				retMap.put("ERRORMSG", " 解密异常。 ");
				retString = CherryUtil.map2Json(retMap);
				logger.error(retString);
				return retString;
			}
			Map<String, Object> paramMap = new HashMap<String, Object>();
			try {
				paramMap.putAll(CherryUtil.json2Map(paramData));
			} catch (Exception ex) {
				retMap.put("ERRORCODE", "WSE9997");
				retMap.put("ERRORMSG", "参数paramData解析JSON失败。");
				retString = CherryUtil.map2Json(retMap);
				logger.error(retString);
				return retString;
			}
			// 检测 TradeType参数
			String tradeType = String.valueOf(paramMap.get("TradeType"));
			if (CherryChecker.isNullOrEmpty(tradeType) || "null".equalsIgnoreCase(tradeType)) {
				retMap.put("ERRORCODE", "WSE9994");
				retMap.put("ERRORMSG", "参数TradeType错误。TradeType=" + tradeType);
				retString = CherryUtil.map2Json(retMap);
				logger.error(retString);
				return retString;
			}

			int orgID = Integer.parseInt(brandMap.get("organizationInfoID").toString());
			int brandID = Integer.parseInt(brandMap.get("brandInfoID").toString());
			String orgCode = brandMap.get("orgCode").toString();
			paramMap.put("BIN_OrganizationInfoID", orgID);
			paramMap.put("OrganizationCode", orgCode);
			paramMap.put("BIN_BrandInfoID", brandID);
			paramMap.put("OrgCode", brandMap.get("orgCode"));
			paramMap.put("BrandCode", brandCode);
			paramMap.put("OriginParamData", origianParamData);

			// java 反射的方式，效率低，多品牌对应困难，要逐步废除，转由配置bean的方式
			TradeConfig tc = getTradeConfig(tradeType);
			if (null != tc) {
				long startTime = System.currentTimeMillis();
				Class<?> c = Class.forName(tc.getClassName());
				Method m = c.getMethod(tc.getMethodName(), Map.class);				
				retMap = (Map<String, Object>) m.invoke(applicationContext.getBean(tc.getBeanName()), paramMap);			
			} else {
				String beanName = PropertiesUtil.pps.getProperty("WS_BeanName_" + tradeType, "");
				Object ob = SpringBeanManager.getBean(beanName + "_" + brandCode.toUpperCase());
				if (null == ob) {
					ob = SpringBeanManager.getBean(beanName);
				}				
				retMap = ((IWebservice) ob).tran_execute(paramMap);
			}

			return getEncryptReturnString(AESKEY, retMap,appID,paramData);
		} catch (Exception ex) {
			logger.error("WS ERROR:", ex);
			retMap.put("ERRORCODE", "WSE9999");
			retMap.put("ERRORMSG", "处理过程中发生未知异常");
			retString = CherryUtil.map2Json(retMap);
			return retString;
		} finally {
			// 清除数据源ThreadLocal变量
			CustomerContextHolder.clearCustomerDataSourceType();
		}
	}
	
	private String getEncryptReturnString (String AESKey,Map<String, Object> retMap,String appID,String paramData) throws Exception{
		String strJson ="";
		try{
		if(retMap.containsKey("ERRORCODE")){
			// 业务中报错，直接返回报错信息
			strJson= CherryUtil.map2Json(retMap);
			logger.error(appID+strJson);
			logger.error(paramData);
			return strJson;
		}
		
		HashMap<String,Object>	returnMap = new HashMap<String,Object>();
		returnMap.put("ERRORCODE", "0");
		returnMap.put("ERRORMSG", "OK");
		// 能正常获取到品牌的AES Key
		if(retMap.containsKey("ResultContent")){
			// List形式的返回结果
			returnMap.put("ResultContent", CherryAESCoder.encrypt(CherryUtil.obj2Json(retMap.get("ResultContent")),AESKey));
		}else if(retMap.containsKey("ResultMap")){
			// Map形式的返回结果
			returnMap.put("ResultMap", CherryAESCoder.encrypt(CherryUtil.obj2Json(retMap.get("ResultMap")),AESKey));
		}else if(retMap.containsKey("ResultString")){
			// String形式的返回结果
			returnMap.put("ResultString", CherryAESCoder.encrypt(CherryUtil.obj2Json(retMap.get("ResultString")),AESKey));
		}
        // 返回结果的总条数，可供调用方作分页控制
		if(retMap.containsKey("ResultTotalCNT")){
		    returnMap.put("ResultTotalCNT", ConvertUtil.getString(retMap.get("ResultTotalCNT")));
		}
		strJson= CherryUtil.map2Json(returnMap);
		return strJson;
		}catch(Exception e){
			logger.error("WS ERROR:",e);
		}
		return null;
	}
	
	private TradeConfig getTradeConfig(String tradeType) throws Exception{
		try {
			if (tradeConfigMap == null) {
				tradeConfigMap = new ConcurrentHashMap<String, TradeConfig>();
				// 如果业务配置Map为空，则从配置文件中加载
				String jsonTradeConfig = PropertiesUtil.pps.getProperty("WS_TradeType_" + tradeType, "");
				if("".equals(jsonTradeConfig)){
					return null;
				}
				Map<String, String> configMap = CherryUtil.json2Map(jsonTradeConfig);
				TradeConfig tc = new TradeConfig();
				tc.setClassName(configMap.get("ClassName"));
				tc.setBeanName(configMap.get("BeanName"));
				tc.setMethodName(configMap.get("MethodName"));
				tradeConfigMap.put(tradeType, tc);
				return tc;
			} else if (tradeConfigMap.containsKey(tradeType)) {
				TradeConfig tc = tradeConfigMap.get(tradeType);
				return tc;
			} else {
				String jsonTradeConfig = PropertiesUtil.pps.getProperty("WS_TradeType_" + tradeType, "");
				if("".equals(jsonTradeConfig)){
					return null;
				}
				Map<String, String> configMap = CherryUtil.json2Map(jsonTradeConfig);
				TradeConfig tc = new TradeConfig();
				tc.setClassName(configMap.get("ClassName"));
				tc.setBeanName(configMap.get("BeanName"));
				tc.setMethodName(configMap.get("MethodName"));
				tradeConfigMap.put(tradeType, tc);
				return tc;
			}
		}catch(Exception e){
			logger.error("WS ERROR:",e);
			return null;
			//throw new Exception("业务"+tradeType+"没有配置或配置不正确。");
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		applicationContext = arg0;		
	}
	
	@Path("/hotline")
	@GET
	public String hotlineRequest(@QueryParam("brandcode") String brandCode, @QueryParam("mobilephone") String mobilephone, @QueryParam("buttonnumber") String buttonNumber, @QueryParam("sign") String sign) throws Exception {
		Map<String, Object> retMap = new HashMap<String, Object>();
		String retString ;
		
		//参数检查
		if (CherryChecker.isNullOrEmpty(brandCode)) {
			retMap.put("ERRORCODE", "WSE9997");
			retMap.put("ERRORMSG", "参数brandCode错误。brandCode=" + brandCode);
			retString = CherryUtil.map2Json(retMap);
			logger.error(retString);
			return retString;
		}
		if (CherryChecker.isNullOrEmpty(mobilephone)) {
			retMap.put("ERRORCODE", "WSE9997");
			retMap.put("ERRORMSG", "参数mobilephone错误。mobilephone=" + mobilephone);
			retString = CherryUtil.map2Json(retMap);
			logger.error(retString);
			return retString;
		}
		if (CherryChecker.isNullOrEmpty(buttonNumber)) {
			retMap.put("ERRORCODE", "WSE9997");
			retMap.put("ERRORMSG", "参数buttonNumber错误。buttonNumber=" + buttonNumber);
			retString = CherryUtil.map2Json(retMap);
			logger.error(retString);
			return retString;
		}
		if (CherryChecker.isNullOrEmpty(sign)) {
			retMap.put("ERRORCODE", "WSE9997");
			retMap.put("ERRORMSG", "参数sign错误。sign=" + sign);
			retString = CherryUtil.map2Json(retMap);
			logger.error(retString);
			return retString;
		}

		// 设置数据源
		if (!webserviceDataSource.setBrandDataSource(brandCode)) {
			retMap.put("ERRORCODE", "WSE9998");
			retMap.put("ERRORMSG", "参数brandCode错误。brandCode=" + brandCode);
			retString = CherryUtil.map2Json(retMap);
			logger.error(retString);
			return retString;
		}
		
		// 查询到组织ID品牌ID
		Map<String, Object> brandMap = new HashMap<String, Object>();
		brandMap.put("brandCode", brandCode);
		webserviceDataSource.getBrandInfo(brandMap);
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		int orgID = Integer.parseInt(brandMap.get("organizationInfoID").toString());
		int brandID = Integer.parseInt(brandMap.get("brandInfoID").toString());
		String orgCode = brandMap.get("orgCode").toString();
		paramMap.put("BIN_OrganizationInfoID", orgID);
		paramMap.put("OrganizationCode", orgCode);
		paramMap.put("BIN_BrandInfoID", brandID);
		paramMap.put("OrgCode", brandMap.get("orgCode"));
		paramMap.put("BrandCode", brandCode);
		paramMap.put("MobilePhone", mobilephone);
		paramMap.put("ButtonNumber", buttonNumber);
		
		Object ob = SpringBeanManager.getBean("wsHotlineRequest");
						
		retMap = ((IWebservice) ob).tran_execute(paramMap);
		
		retString = CherryUtil.map2Json(retMap);		
		return retString;
	}
}

class TradeConfig{
	String className;
	String beanName;
	String methodName;
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getBeanName() {
		return beanName;
	}
	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
}
