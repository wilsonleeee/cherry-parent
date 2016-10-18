
package com.cherry.mb.cct.interfaces;

import java.util.List;
import java.util.Map;

public interface BINOLMBCCT01_IF {
	/**
	 * 根据来电号码获取匹配的会员数量
	 * 
	 * @param map
	 * @return 匹配会员数量
	 */
	public int getMemberCountByPhone(Map<String, Object> map);
	
	/**
	 * 根据来电号码获取匹配的会员列表
	 * 
	 * @param map
	 * @return 匹配会员列表
	 */
	@SuppressWarnings("rawtypes")
	public List getMemberListByPhone(Map<String, Object> map);
	
	/**
	 * 根据来电号码获取唯一匹配的会员的ID
	 * 
	 * @param map
	 * @return 匹配会员的ID
	 */
	public String getMemberIdByPhone(Map<String, Object> map);
	
	/**
	 * 根据来电号码获取匹配的非会员数量
	 * 
	 * @param map
	 * @return 匹配的非会员数量
	 */
	public int getCustomerCountByPhone(Map<String, Object> map);
	
	/**
	 * 根据来电号码获取唯一匹配的非会员的ID
	 * 
	 * @param map
	 * @return 匹配的非会员ID
	 */
	public String getCustomerIdByPhone(Map<String, Object> map);
	
	/**
	 * 记录来电日志
	 * 
	 * @param Map,String
	 * 			来电日志的参数集合
	 * @return 无
	 * 			
	 * @throws Exception 
	 */
	public void saveCallLog(Map<String, Object> map, String type) throws Exception;
}