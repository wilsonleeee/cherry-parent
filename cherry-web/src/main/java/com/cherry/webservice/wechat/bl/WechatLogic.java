package com.cherry.webservice.wechat.bl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.DateUtil;
import com.cherry.webservice.wechat.interfaces.Wechat_IF;
import com.cherry.webservice.wechat.service.WechatService;

/**
 * 微信接口BL
 * 
 * @author Hujh
 * @version 2015-11-11 
 */
public class WechatLogic implements Wechat_IF {

	@Resource(name = "wechatService")
	private WechatService wechatService;
	
	/**
	 * 保存扫码关注信息
	 * 
	 */
	@Override
	public Map<String, Object> tran_saveSubscribeInfo(Map<String, Object> map) {
		
		Map<String, Object> retMap = new HashMap<String, Object>();
		String organizationInfoId =  String.valueOf(map.get("BIN_OrganizationInfoID"));
		String brandInfoId = String.valueOf(map.get("BIN_BrandInfoID"));
		String openID = String.valueOf(map.get("OpenID"));
		String subscribeEventKey = String.valueOf(map.get("SubscribeEventKey"));
		String subscribeTime = String.valueOf(map.get("SubscribeTime"));
		String firstFlag = String.valueOf(map.get("FirstFlag"));
		
		if("null".equals(openID) || "".equals(openID)) {
			retMap.put("ERRORCODE", "WSE9993");
			retMap.put("ERRORMSG", "参数OpenID必填。");
			return retMap;
		}
		
		if("null".equals(subscribeTime) || "".equals(subscribeTime)) {
			subscribeTime = DateUtil.date2String(new Date(), DateUtil.DATETIME_PATTERN);
		}
		
		if("null".equals(firstFlag) || "".equals(firstFlag)) {
			retMap.put("ERRORCODE", "WSE9993");
			retMap.put("ERRORMSG", "参数FirstFlag必填。");
			return retMap;
		}
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("BIN_OrganizationInfoID", organizationInfoId);
		params.put("BIN_BrandInfoID", brandInfoId);
		params.put("OpenID", openID);
		if(!"null".equals(subscribeEventKey) && !"".equals(subscribeEventKey)) {
			params.put("SubscribeEventKey", subscribeEventKey);
		}
		params.put("SubscribeTime", subscribeTime);
		params.put("FirstFlag", firstFlag);
		setCommParam(params);
		wechatService.saveSubscribeInfo(params);
		return retMap;
	}
	
	/**
	 * 设置共通参数
	 * @param map
	 * @return
	 */
	private Map<String, Object> setCommParam(Map<String, Object> map) {
		map.put(CherryConstants.CREATEDBY, "cherryws");
		map.put(CherryConstants.CREATEPGM, "WechatLogic");
		map.put(CherryConstants.UPDATEDBY, "cherryws");
		map.put(CherryConstants.UPDATEPGM, "WechatLogic");
		return map;
	}
	
	
}
