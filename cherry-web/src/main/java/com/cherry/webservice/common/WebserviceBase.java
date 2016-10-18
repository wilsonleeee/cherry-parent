package com.cherry.webservice.common;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.core.CherryAESCoder;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.util.CherryUtil;

public class WebserviceBase {

//	private static Logger logger = LoggerFactory.getLogger(WebserviceBase.class.getName());
	@Resource(name = "webserviceDataSource")
	private WebserviceDataSource webserviceDataSource;

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
		if(!webserviceDataSource.setBrandDataSource(brandCode)){
			paramMap.put("ERRORCODE", "E0002");
			paramMap.put("ERRORMSG", "参数brandCode错误");
			return false;
		}
		
		//查询AES密钥
		String AESKEY = webserviceDataSource.getAESKey(brandCode);
		if(CherryChecker.isNullOrEmpty(AESKEY)){
			paramMap.put("ERRORCODE", "E0005");
			paramMap.put("ERRORMSG", "未能取得品牌"+brandCode+"的AES密钥");
			return false;
		}
		
		//查询到组织ID品牌ID
		Map<String,Object> brandMap = new HashMap<String,Object>();
		brandMap.put("brandCode", brandCode);		
		webserviceDataSource.getBrandInfo(brandMap);
		
		//AES解密,将解密后的JSON字符串转换成Map		
		paramData = CherryAESCoder.decrypt(paramData, AESKEY);		
		paramMap.putAll(CherryUtil.json2Map(paramData));
		int orgID = Integer.parseInt(brandMap.get("organizationInfoID").toString());
		int brandID = Integer.parseInt(brandMap.get("brandInfoID").toString());
		paramMap.put("BIN_OrganizationInfoID", orgID);
		paramMap.put("BIN_BrandInfoID", brandID);
		return true;
	}
	
//	protected String getReturnString (Map<String, Object> retMap) throws Exception{
//		return CherryUtil.map2Json(retMap);
//	}
	
	protected String getEncryptReturnString (String brandCode,Map<String, Object> retMap) throws Exception{		
		String AESKey = webserviceDataSource.getAESKey(brandCode);
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
