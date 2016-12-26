package com.cherry.webservice.common;

import com.cherry.cm.core.CherryAESCoder;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CustomerContextHolder;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.DateUtil;
import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.Resource;
import javax.crypto.BadPaddingException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Path("/cherryws")
public class WebserviceEntrance implements ApplicationContextAware{
	
	private static Logger logger = LoggerFactory.getLogger(WebserviceEntrance.class.getName());
	
	private static ApplicationContext applicationContext; 
	
	@Resource(name = "webserviceDataSource")
	private WebserviceDataSource webserviceDataSource;
	
	private static ConcurrentHashMap<String,TradeConfig> tradeConfigMap;
	
	private static HashMap<String,ThirdPartyConfig> thirdPartyConfigMap = new HashMap<String, ThirdPartyConfig>();

	@GET
	@Path("/basis")
	public String basis(@QueryParam("appID") String appID, @QueryParam("paramData") String paramData, @Context HttpServletRequest request)
 throws Exception {
		Map<String, Object> retMap = new HashMap<String, Object>();
		String retString = "";
		// 参数不能为空
		if (CherryChecker.isNullOrEmpty(appID)) {
			retMap.put("ERRORCODE", "WSE9993");
			retMap.put("ERRORMSG", "参数appID错误。appID=" + appID);
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

		try {

			// 检查appID是否存在，不存在则尝试刷新一次
			if (null == thirdPartyConfigMap || !thirdPartyConfigMap.containsKey(appID)) {
				thirdPartyConfigMap = webserviceDataSource.getThirdPartyConfigList();
			}

			// 如果仍然不存在，则报错
			if (null == thirdPartyConfigMap || !thirdPartyConfigMap.containsKey(appID)) {
				retMap.put("ERRORCODE", "WSE9993");
				retMap.put("ERRORMSG", "参数appID错误。appID=" + appID);
				retString = CherryUtil.map2Json(retMap);
				logger.error(retString);
				return retString;
			}

			ThirdPartyConfig config = thirdPartyConfigMap.get(appID);
			String secret = config.getAppSecret();
			// 解密paramData
			paramData = CherryAESCoder.decrypt(paramData, secret);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.putAll(CherryUtil.json2Map(paramData));

			if (!"GetAESKey".equals(String.valueOf(paramMap.get("TradeType")))) {
				retMap.put("ERRORCODE", "WSE9993");
				retMap.put("ERRORMSG", "参数TradeType错误。TradeType=" + String.valueOf(paramMap.get("TradeType")));
				retString = CherryUtil.map2Json(retMap);
				logger.error(retString);
				return retString;
			}

			// 已正常解密，业务类型为刷新AESKey
			String newKey = CherryAESCoder.initkey();
			// 过期时间，系统当前时间+有效期时间
			int expirehour = Integer.parseInt(PropertiesUtil.pps.getProperty("WS_DynamicAESKey_Refresh"));
			String expireTime = DateUtil.addDateByHours(DateUtil.DATETIME_PATTERN, webserviceDataSource.getSYSDateTime(),	expirehour);
			paramMap.clear();
			paramMap.put("AppID", config.getAppID());
			paramMap.put("DynamicAESKey", newKey);
			paramMap.put("AESKeyExpireTime", expireTime);
			// 更新至DB和系统内存
			webserviceDataSource.refreshDynamicAESKey(paramMap);
			config.setAppSecret(newKey);
			config.setAesKeyExpireTime(expireTime);
			// 返回结果
			paramMap.clear();
			paramMap.put("AESKey", newKey);
			//过期时间返回的是还有多少秒过期，而不是截止到什么时候过期，这样可以应对两个系统的系统时间不一致的情况；
			paramMap.put("ExpireTime", expirehour*3600);
			retMap.put("ResultMap", paramMap);
			return getEncryptReturnString(secret, retMap);
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
	
		
	@GET
	public String getWebservice(@QueryParam("brandCode") String brandCode, @QueryParam("paramData") String paramData, @QueryParam("appID") String appID, @Context HttpServletRequest request)
 throws Exception {

		Map<String, Object> retMap = new HashMap<String, Object>();
		String retString ;

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
//			if ("true".equals(PropertiesUtil.pps.getProperty("WS_SafeModel_Flag", "false"))) {
//				// 如果开启了动态密钥模式
//				if (CherryChecker.isNullOrEmpty(appID)) {
//					retMap.put("ERRORCODE", "WSE9993");
//					retMap.put("ERRORMSG", "参数appID错误。appID=" + appID);
//					retString = CherryUtil.map2Json(retMap);
//					logger.error(retString);
//					return retString;
//				}
//				// 检查appID是否存在，不存在则尝试刷新一次
//				if (null == thirdPartyConfigMap || !thirdPartyConfigMap.containsKey(appID)) {
//					thirdPartyConfigMap = webserviceDataSource.getThirdPartyConfigList();
//				}
//				if (!thirdPartyConfigMap.containsKey(appID)) {
//					retMap.put("ERRORCODE", "WSE9990");
//					retMap.put("ERRORMSG", "无效的AppID");
//					retString = CherryUtil.map2Json(retMap);
//					logger.error(retString);
//					return retString;
//				}
//				ThirdPartyConfig config = thirdPartyConfigMap.get(appID);
//				String expireTime = config.getAesKeyExpireTime();
//				Calendar calendar = Calendar.getInstance();
//				SimpleDateFormat format = new SimpleDateFormat(DateUtil.DATETIME_PATTERN);
//				String nowtime = format.format(calendar.getTime());
//				if (expireTime.compareTo(nowtime) < 0) {
//					// 密钥过期
//					retMap.put("ERRORCODE", "WSE9995");
//					retMap.put("ERRORMSG", "密钥无效或已过期");
//					retString = CherryUtil.map2Json(retMap);
//					logger.error(retString);
//					return retString;
//				}
//				AESKEY = config.getDynamicAESKey();
//			} else {
				AESKEY = webserviceDataSource.getAESKey(brandCode);
//			}
			if (CherryChecker.isNullOrEmpty(AESKEY)) {
				webserviceDataSource.getThirdPartyConfigList();
				AESKEY = webserviceDataSource.getAESKey(brandCode);
				if(CherryChecker.isNullOrEmpty(AESKEY)) {
					retMap.put("ERRORCODE", "WSE9996");
					retMap.put("ERRORMSG", "品牌" + brandCode + "的密钥缺失");
					retString = CherryUtil.map2Json(retMap);
					logger.error(retString);
					return retString;
				}
			}

			// 查询到组织ID品牌ID
			Map<String, Object> brandMap = new HashMap<String, Object>();
			brandMap.put("brandCode", brandCode);
			webserviceDataSource.getBrandInfo(brandMap);

			// AES解密,将解密后的JSON字符串转换成Map
			Transaction transaction = Cat.newTransaction("WebService", "decrypt");
			try{
				paramData = CherryAESCoder.decrypt(paramData, AESKEY);
				transaction.setStatus(Transaction.SUCCESS);
			}catch (Exception t){
				transaction.setStatus(t);
				Cat.logError(t);
				throw t;
			}finally {
				transaction.complete();
			}
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.putAll(CherryUtil.json2Map(paramData));
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

			transaction = Cat.newTransaction("WebService", "invoke");
			try {
				TradeConfig tc = getTradeConfig(tradeType);
				Class<?> c = Class.forName(tc.getClassName());
				Method m = c.getMethod(tc.getMethodName(), Map.class);
				retMap = (Map<String, Object>) m.invoke(applicationContext.getBean(tc.getBeanName()), paramMap);
				transaction.setStatus(Transaction.SUCCESS);
			}catch (Exception t){
			transaction.setStatus(t);
			Cat.logError(t);
			throw t;
		}finally {
			transaction.complete();
		}
			return getEncryptReturnString(AESKEY, retMap);
		} catch (Exception ex) {
			if (ex instanceof BadPaddingException) {
				retMap.put("ERRORCODE", "WSE9995");
				retMap.put("ERRORMSG", "密钥无效或已过期");
				retString = CherryUtil.map2Json(retMap);
				return retString;
			}
			logger.error(paramData);
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
	
	private String getEncryptReturnString (String AESKey,Map<String, Object> retMap) throws Exception{
		String strJson ="";
		try{
		if(retMap.containsKey("ERRORCODE")){
			// 业务中报错，直接返回报错信息
			strJson= CherryUtil.map2Json(retMap);
			logger.error("WS return:"+strJson);
			return strJson;
		}
		
		HashMap<String,Object>	returnMap = new HashMap<String,Object>();
		returnMap.put("ERRORCODE", "0");
		returnMap.put("ERRORMSG", "OK");
		if(retMap.containsKey("WARNCODE")){
			returnMap.put("WARNCODE", retMap.get("WARNCODE"));
			returnMap.put("WARNMSG", retMap.get("WARNMSG"));
		}
		if(retMap.containsKey("ResultTotalCNT")){
			returnMap.put("ResultTotalCNT", retMap.get("ResultTotalCNT"));
		}
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
		if(retMap.containsKey("ResultTotalCNT")) {
			returnMap.put("ResultTotalCNT", retMap.get("ResultTotalCNT"));
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
					throw new Exception();
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
					throw new Exception();
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
			throw new Exception("业务"+tradeType+"没有配置或配置不正确。");
		}	
	}

	@Override
	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		applicationContext = arg0;		
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
