package com.cherry.webservice.auth.resource;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.core.CherryAESCoder;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CustomerContextHolder;
import com.cherry.cm.core.DESPlus;
import com.cherry.cm.core.JsclPBKDF2WithHMACSHA256;
import com.cherry.cm.util.CherryUtil;
import com.cherry.webservice.auth.service.UserAuthService;
import com.cherry.webservice.common.WebserviceDataSource;

@Path("/auth")
public class UserAuthResource {
	
	private static Logger logger = LoggerFactory.getLogger(UserAuthResource.class);
	
	@Resource
	private UserAuthService userAuthService;
	
	@Resource(name = "webserviceDataSource")
	private WebserviceDataSource webserviceDataSource;
	
	/**
	 * 查询会员基本信息目前珀莱雅已经在使用，不可擅动，其它的接口将以新的结构提供，请见WebserviceEntrance类
	 * @param brandCode
	 * @param paramData
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@GET
	public String getUserInfo(@QueryParam("loginName") String loginName, @QueryParam("paramData") String paramData,@Context HttpServletRequest request) throws Exception {
		Map<String, Object> retMap = new HashMap<String, Object>();
		try {
			String dbName = userAuthService.getDBByName(loginName);
			if(dbName == null || "".equals(dbName)) {
				retMap.put("ERRORCODE", "WSE0007");
				retMap.put("ERRORMSG", "用户名不存在");
				return CherryUtil.map2Json(retMap);
			}
			
			CustomerContextHolder.setCustomerDataSourceType(dbName);
			
			Map<String, Object> userInfoMap = userAuthService.getUserSecurityInfo(loginName);
			if(userInfoMap == null) {
				retMap.put("ERRORCODE", "WSE0007");
				retMap.put("ERRORMSG", "用户名不存在");
				return CherryUtil.map2Json(retMap);
			}
			String brandCode = (String)userInfoMap.get("brandCode");
			String AESKEY = webserviceDataSource.getAESKey(brandCode);
			if(AESKEY == null || "".equals(AESKEY)) {
				retMap.put("ERRORCODE", "WSE9996");
				retMap.put("ERRORMSG", "未能取得品牌"+brandCode+"的AES密钥");
				return CherryUtil.map2Json(retMap);
			}
			
			String passWord = CherryAESCoder.decrypt(paramData, AESKEY);
			String _passWord = (String)userInfoMap.get("passWord");
			// 解密处理
			DESPlus des = new DESPlus(CherryConstants.CUSTOMKEY);
			_passWord = des.decrypt(_passWord);
			
			String _loginName = null;
			try {
				_loginName = JsclPBKDF2WithHMACSHA256.DecrptPBKDF2WithHMACSHA256(_passWord, passWord);
			} catch (Exception e) {
				
			}
			
			if(_loginName != null && _loginName.equals(loginName)) {
				retMap.put("ERRORCODE", "0");
				retMap.put("ERRORMSG", "执行成功");
				Map<String, Object> resultMap = new HashMap<String, Object>();
				String userId = String.valueOf(userInfoMap.get("userId"));
				resultMap.put("userId", userId);
				resultMap.put("loginName", userInfoMap.get("loginName"));
				resultMap.put("employeeName", userInfoMap.get("employeeName"));
				resultMap.put("brandCode", userInfoMap.get("brandCode"));
				retMap.put("ResultMap", CherryAESCoder.encrypt(CherryUtil.obj2Json(resultMap),AESKEY));
			} else {
				retMap.put("ERRORCODE", "WSE0008");
				retMap.put("ERRORMSG", "密码不正确");
				return CherryUtil.map2Json(retMap);
			}
			
			
	
		} catch (Exception ex) {
			logger.error("WS ERROR:", ex);
			logger.error("WS ERROR loginName:"+ loginName);
			logger.error("WS ERROR paramData:"+ paramData);
			retMap.put("ERRORCODE", "WSE9999");
			retMap.put("ERRORMSG", "处理过程中发生未知异常。");
		} finally {
			CustomerContextHolder.clearCustomerDataSourceType();
		}
		return CherryUtil.map2Json(retMap);
	}

}
