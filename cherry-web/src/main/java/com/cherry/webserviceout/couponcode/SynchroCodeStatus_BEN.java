package com.cherry.webserviceout.couponcode;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.ws.rs.core.MultivaluedMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.core.CherryAESCoder;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.CherryUtil;
import com.cherry.webserviceout.common.JerseyWebserviceClient;
import com.cherry.webserviceout.common.JerseyWebserviceClientSSL;
import com.cherry.webserviceout.common.WebserviceOutConfig;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class SynchroCodeStatus_BEN extends BaseService implements ISynchroCodeStatus{
	private static Logger log = LoggerFactory.getLogger(SynchroCodeStatus_BEN.class);
	private static String wsURL=null;
	private static String aesKey=null;
	@Resource(name = "webserviceOutConfig")
	private WebserviceOutConfig webserviceOutConfig;
	/* 
	 * param 中包含的key有CouponCode，Status，BIN_OrganizationInfoID，BIN_BrandInfoID
	 */
	@Override
	public Map<String, Object> synchroCodeStatus(String brandCode, Map<String, Object> param) throws Exception {
		// 如果URL或aesKey为空，则从数据库中获取
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if (null == wsURL || null == aesKey) {
				map.put("BIN_OrganizationInfoID", param.get("BIN_OrganizationInfoID"));
				map.put("BIN_BrandInfoID", param.get("BIN_BrandInfoID"));
				map.put("TradeCode", "SCSTW");
				map = webserviceOutConfig.getWebserviceOutConfig(map);
				if (null != map) {
					wsURL = map.get("URL").toString();
					aesKey = map.get("AccountPWD").toString();
				} else {
					log.error("接口信息配置缺失:" + brandCode);
					throw new Exception("接口信息配置缺失");
				}
			}
			// 调用webservice
			map = accessWebService(param);
			if (null != map && "0".equals(String.valueOf(map.get("ERRORCODE")))) {
				// 调用成功
				map.clear();
				map.put("ERRORCODE", "0");
				map.put("ERRORMSG", "OK");
				return map;
			} else {
				// 同步失败
				throw new Exception();
			}
		} catch (Exception ex) {
			// 同步失败
			//TODO:
			param.put("BrandCode", brandCode);
			insertFailedData(param);
			map.clear();
			map.put("ERRORCODE", "1");
			map.put("ERRORMSG", "False");
			return map;
		}
	}

	private void insertFailedData(Map<String,Object>param){
		//TODO:完善参数，出入数据库
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 业务类型
		paramMap.put("TradeType", "SCSTW");
		// Coupon状态
		paramMap.put("CouponStatus", param.get("Status"));
		// 同步状态
		paramMap.put("SynchroStatus", "0");
		// 有效区分
		paramMap.put("ValidFlag", "1");
		// 创建时间
		paramMap.put(CherryConstants.CREATE_TIME, this.getSYSDate());
		// 更新时间
		paramMap.put(CherryConstants.UPDATE_TIME, this.getSYSDate());
		// 创建程序
		paramMap.put(CherryConstants.CREATEPGM, "SynchroCodeStatus_BEN");
		// 更新程序
		paramMap.put(CherryConstants.UPDATEPGM, "SynchroCodeStatus_BEN");
        paramMap.putAll(param);
        paramMap.put(CherryConstants.IBATIS_SQL_ID, "SynchroCouponCode.insertFailedData");
		baseServiceImpl.save(paramMap);
	}

	
	private static Client client;
	private static Map<String, Object> webResourceMap = new HashMap<String, Object>();
	
	public static WebResource getWebResource(String webServiceUrl) throws Exception {

		if (webResourceMap.containsKey(webServiceUrl)) {
			return (WebResource) webResourceMap.get(webServiceUrl);
		} else {
			synchronized (webResourceMap) {
				if (webResourceMap.containsKey(webServiceUrl)) {
					return (WebResource) webResourceMap.get(webServiceUrl);
				} else {
					if (client == null) {
						if(webServiceUrl.startsWith("https:")){
							client = JerseyWebserviceClientSSL.getClient("","");
						}else{
							client = JerseyWebserviceClient.getClient("","");
						}
					}
					WebResource webResource = client.resource(webServiceUrl);
					webResourceMap.put(webServiceUrl, webResource);
					return webResource;
				}
			}
		}
	}
	
	public static Map<String, Object> accessWebService(Map praMap) throws Exception{
		WebResource webResource = getWebResource(wsURL);
		//对传递的参数进行加密
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add("paramData", CherryAESCoder.encrypt(CherryUtil.map2Json(praMap),aesKey));
		String result = webResource.queryParams(queryParams).get(String.class);
		Map<String, Object> retMap = CherryUtil.json2Map(result);
		return retMap;
	}
	

}
