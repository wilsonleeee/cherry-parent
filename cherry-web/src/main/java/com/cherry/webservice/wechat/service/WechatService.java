package com.cherry.webservice.wechat.service;

import java.util.Map;

import com.cherry.cm.service.BaseService;

/**
 * 微信接口Service
 * 
 * @author Hujh
 * @version 2015-11-11 1.0.0
 */
public class WechatService extends BaseService {
	

	/**
	 * 保存扫码关注信息
	 * @param map
	 */
	public void saveSubscribeInfo(Map<String, Object> map) {
		
		baseServiceImpl.save(map, "Wechat.saveSubscribeInfo");
	}
}
