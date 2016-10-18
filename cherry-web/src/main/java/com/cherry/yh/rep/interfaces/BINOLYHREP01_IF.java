package com.cherry.yh.rep.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

/**
 * 按订单详细报表Interface
 * 
 * @author menghao
 * 
 */
public interface BINOLYHREP01_IF extends ICherryInterface {
	/**
	 * 取得订单汇总信息
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getSumInfo(Map<String, Object> map);
	
	/**
	 * 取得订单数据count
	 * @param map
	 * @return
	 */
	public int getSaleOrderDetailCount(Map<String, Object> map);
	
	/**
	 * 取得订单数据count
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getSaleOrderDetailList(Map<String, Object> map);
}
