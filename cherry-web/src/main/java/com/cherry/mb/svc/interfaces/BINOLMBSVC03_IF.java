package com.cherry.mb.svc.interfaces;

import java.util.List;
import java.util.Map;

public interface BINOLMBSVC03_IF {
	/**
	 * 获取储值卡信息
	 * @param map
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> getCard(Map<String, Object> map) throws Exception;
	/**
	 * 获取促销规则列表
	 * @param map
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> getRechargeDiscountList(Map<String, Object> map) throws Exception;
	/**
	 * 充值接口
	 * @param map
	 * @return
	 * @throws Exception
	 */
	Map<String,Object> savingsCardRecharge(Map<String,Object> map) throws Exception;
}
