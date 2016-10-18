package com.cherry.webservice.wechat.interfaces;

import java.util.Map;

/**
 * 微信接口
 * 
 * @author Hujh
 * @version 2015-11-11
 */
public interface Wechat_IF {
	
	/**
	 * 保存扫码关注信息
	 * @param map
	 * @return
	 */
	Map<String, Object> tran_saveSubscribeInfo(Map<String, Object> map);

}