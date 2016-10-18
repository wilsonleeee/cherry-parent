package com.cherry.mb.cct.interfaces;

import java.util.Map;

public interface BINOLMBCCT05_IF {
	/**
	 * 记录来电非会员资料
	 * 
	 * @param Map,String
	 * 			非会员资料参数集合
	 * @return 无
	 * 			
	 * @throws Exception 
	 */
	public void saveCustomer(Map<String, Object> map, String type) throws Exception;
	
	/**
	 * 根据非会员编号获取来电非会员资料
	 * 
	 * @param Map
	 * 			查询参数
	 * @return Map
	 * 			非会员资料
	 * @throws Exception 
	 */
	public Map<String, Object> getCustomerInfo(Map<String, Object> map) throws Exception;
}
