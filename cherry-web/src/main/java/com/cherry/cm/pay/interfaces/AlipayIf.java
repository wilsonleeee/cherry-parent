package com.cherry.cm.pay.interfaces;

import java.util.List;
import java.util.Map;

public interface AlipayIf {

	/**支付宝接口(收单查询接口) */
	List<Map<String, Object>> getAlipayQuery(Map map);
	
	/**支付宝接口(收单撤销接口,撤销先调查询接口)*/
	List<Map<String, Object>> getAlipayQueryAndCancel(Map map);
	
	/**支付宝接口(收单退款接口，先调查询接口)*/
	List<Map<String, Object>> getAlipayQueryAndRefund(Map map);
	
	/**支付宝接口(单笔查询接口)*/
	List<Map<String, Object>> getAlipaySingleTradeQuery(Map map);
	
	/**支付宝接口(收单关闭接口)*/
	List<Map<String, Object>> getAlipayClose(Map map);
	
	/**支付宝接口(收单撤销接口)*/
	List<Map<String, Object>> getAlipayCancel(Map map);
	
	/**支付宝接口(收单退款接口)*/
	List<Map<String, Object>> getAlipayRefund(Map map);
	
	/**支付宝接口(统一下单并支付接口)*/
	List<Map<String, Object>> getAlipayCreateAndPay2(Map map);
	
	/**支付宝接口(统一下单并支付接口,下单前先调查询接口)*/
	List<Map<String, Object>> getAlipayQueryAndCreateAndPay2(Map map);
	
	/**支付宝接口(退款查询接口)*/
	List<Map<String, Object>> getAlipayRefundQuery(Map map);
	
	boolean getAliPayConfig(Map<String, Object> map);
	
	//##########################以下接口为当面付2.0版本接口##############################//
	/**支付宝接口(统一下单并支付接口,下单前先调查询接口)*/
	List<Map<String, Object>> getAlipayQueryAndCreateAndPayTwo(Map map);
	
	/**支付宝接口(收单查询接口) */
	List<Map<String, Object>> getAlipayQueryTwo(Map map);
	
	/**支付宝接口(统一下单并支付接口)*/
	List<Map<String, Object>> getAlipayCreateAndPayTwo(Map paramMap);
	
	/**支付宝接口(收单撤销接口)*/
	List<Map<String, Object>> getAlipayCancelTwo(Map paramMap);
	
	/**支付宝接口(收单退款接口)*/
	List<Map<String, Object>> getAlipayRefundTwo(Map paramMap);
	
	/**支付宝接口(收单退款接口,先调查询接口)*/
	List<Map<String, Object>> getAlipayQueryAndRefundTwo(Map paramMap);

	/**支付宝接口(收单撤销接口,先调查询接口)*/
	List<Map<String, Object>> getAlipayQueryAndCancelTwo(Map paramMap);
	
}
