package com.cherry.webservice.common;

import com.cherry.cm.core.*;
import com.cherry.cm.util.CherryUtil;

import java.util.HashMap;
import java.util.Map;

public class WebserviceBase {

//	private static Logger logger = LoggerFactory.getLogger(WebserviceBase.class.getName());

	protected boolean parseParam(String brandCode, String paramData,Map<String, Object> paramMap) throws Exception {
		
		if(CherryChecker.isNullOrEmpty(brandCode)){
			paramMap.put("ERRORCODE", "E0001");
			paramMap.put("ERRORMSG", "参数brandCode是必须的");
			return false;
		}
		
		if(CherryChecker.isNullOrEmpty(paramData)){
			paramMap.put("ERRORCODE", "E0001");
			paramMap.put("ERRORMSG", "参数paramData是必须的");
			return false;
		}
		
		
		// 设置数据源
		SystemConfigDTO systemConfigDTO = SystemConfigManager.getSystemConfig(brandCode);
		if(null==systemConfigDTO){
			paramMap.put("ERRORCODE", "E0002");
			paramMap.put("ERRORMSG", "参数brandCode错误");
			return false;
		}
		CustomerContextHolder.setCustomerDataSourceType(systemConfigDTO.getDataSourceName());
		//查询AES密钥
		String AESKEY = systemConfigDTO.getAesKey();
		if(CherryChecker.isNullOrEmpty(AESKEY)){
			paramMap.put("ERRORCODE", "E0005");
			paramMap.put("ERRORMSG", "未能取得品牌"+brandCode+"的AES密钥");
			return false;
		}

		//AES解密,将解密后的JSON字符串转换成Map		
		paramData = CherryAESCoder.decrypt(paramData, AESKEY);		
		paramMap.putAll(CherryUtil.json2Map(paramData));
		paramMap.put("BIN_OrganizationInfoID", systemConfigDTO.getOrganizationInfoID());
		paramMap.put("BIN_BrandInfoID", systemConfigDTO.getBrandInfoID());
		return true;
	}
	
//	protected String getReturnString (Map<String, Object> retMap) throws Exception{
//		return CherryUtil.map2Json(retMap);
//	}
	
	protected String getEncryptReturnString (String brandCode,Map<String, Object> retMap) throws Exception{
		SystemConfigDTO systemConfigDTO = SystemConfigManager.getSystemConfig(brandCode);
		String AESKey = systemConfigDTO.getAesKey();
		if(retMap.containsKey("ERRORCODE")){
			// 业务中报错，直接返回报错信息
			return CherryUtil.map2Json(retMap);
		}
		
		HashMap<String,Object>	returnMap = new HashMap<String,Object>();
		returnMap.put("ERRORCODE", "0");
		returnMap.put("ERRORMSG", "执行成功");
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
		return CherryUtil.map2Json(returnMap);
		
	}
}
