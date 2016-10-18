package com.cherry.shindig.gadgets.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 预约任务报表Service
 * 
 * @author WangCT
 * @version 1.0 2014/12/17
 */
public class OrderTaskService extends BaseService {
	
	/**
	 * 查询预约任务总件数
	 * 
	 * @param map 查询条件
	 * @return 预约任务总件数
	 */
	public int getOrderTaskCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "OrderTask.getOrderTaskCount");
		return baseServiceImpl.getSum(parameterMap);
	}
	
	/**
	 * 查询预约任务List
	 * 
	 * @param map 查询条件
	 * @return 预约任务List
	 */
	public List<Map<String, Object>> getOrderTaskList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "OrderTask.getOrderTaskList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 统计预约任务在不同状态下的数量
	 * 
	 * @param map 查询条件
	 * @return 预约任务在不同状态下的数量
	 */
	public List<Map<String, Object>> getOrderTaskCountList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "OrderTask.getOrderTaskCountList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 查询预约完成List
	 * 
	 * @param map 查询条件
	 * @return 预约完成List
	 */
	public List<Map<String, Object>> getGiftDrawList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "OrderTask.getGiftDrawList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 查询活动List
	 * 
	 * @param map 查询条件
	 * @return 活动List
	 */
	public List<Map<String, Object>> getCampaignList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "OrderTask.getCampaignList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 查询活动库存数量
	 * 
	 * @param map 查询条件
	 * @return 活动库存数量
	 */
	public Map<String, Object> getCampaignStockCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "OrderTask.getCampaignStockCount");
		return (Map)baseServiceImpl.get(parameterMap);
	}

}
