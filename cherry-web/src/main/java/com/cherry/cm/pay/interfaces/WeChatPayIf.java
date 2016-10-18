package com.cherry.cm.pay.interfaces;

import java.util.List;
import java.util.Map;

public interface WeChatPayIf {
	
	/** 微信接口(收单查询接口)*/
	List<Map<String, Object>> getOrderQuery(Map map);

	/** 微信接口(提交被扫接口)*/
	List<Map<String,Object>> getMicropay(Map map);
	
	/**微信接口(退款申请接口)*/
	List<Map<String,Object>> getWEpayRefund(Map map);
	
	/**微信接口(退款查询接口)*/
	List<Map<String,Object>> getWEpayRefundQuery(Map map);
	
	/** 微信接口(撤销接口)*/
	List<Map<String,Object>> getWEpayReverse(Map map);
	
	/**微信接口(对账单接口)*/
	List<Map<String,Object>> getWEpayDownloadbill(Map map);
	
	boolean getWeChatConfig(Map map);
	
//	boolean getWepayKey(String counterCode);
}
