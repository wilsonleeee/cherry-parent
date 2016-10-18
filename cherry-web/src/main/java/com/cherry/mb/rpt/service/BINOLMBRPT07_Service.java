package com.cherry.mb.rpt.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 会员活动统计报表Service
 * 
 * @author WangCT
 * @version 1.0 2014/12/25
 */
public class BINOLMBRPT07_Service extends BaseService {
	
	/**
	 * 取得柜台预约统计信息件数
	 * 
	 * @param map
	 *            检索条件
	 * @return 柜台预约统计信息件数
	 */
	public int getCountByCounterCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLMBRPT07.getCountByCounterCount");
		return baseServiceImpl.getSum(parameterMap);
	}

	/**
	 * 取得柜台预约统计信息List
	 * 
	 * @param map
	 *            检索条件
	 * @return 柜台预约统计信息List
	 */
	public List<Map<String, Object>> getCountByCounterList(
			Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLMBRPT07.getCountByCounterList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 取得活动List
	 * 
	 * @param map
	 *            检索条件
	 * @return 活动List
	 */
	public List<Map<String, Object>> getCampaignList(
			Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLMBRPT07.getCampaignList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 取得活动信息件数
	 * 
	 * @param map
	 *            检索条件
	 * @return 活动信息件数
	 */
	public int getCampaignInfoCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLMBRPT07.getCampaignInfoCount");
		return baseServiceImpl.getSum(parameterMap);
	}

	/**
	 * 取得活动信息List
	 * 
	 * @param map
	 *            检索条件
	 * @return 活动信息List
	 */
	public List<Map<String, Object>> getCampaignInfoList(
			Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLMBRPT07.getCampaignInfoList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 取得活动预约数
	 * 
	 * @param map
	 *            检索条件
	 * @return 活动预约数
	 */
	public int getOrderCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLMBRPT07.getOrderCount");
		return baseServiceImpl.getSum(parameterMap);
	}
	
	/**
	 * 取得活动预约到柜数
	 * 
	 * @param map
	 *            检索条件
	 * @return 活动预约到柜数
	 */
	public int getBookCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLMBRPT07.getBookCount");
		return baseServiceImpl.getSum(parameterMap);
	}
	
	/**
	 * 取得活动购买人数和金额
	 * 
	 * @param map
	 *            检索条件
	 * @return 活动购买人数和金额
	 */
	public Map<String, Object> getSaleCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLMBRPT07.getSaleCount");
		return (Map)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 获取销售明细统计信息
	 * 
	 * @param map
	 *            检索条件
	 * @return 销售明细统计信息
	 */
	public Map<String, Object> getSaleDetailCountInfo(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLMBRPT07.getSaleDetailCountInfo");
		return (Map)baseServiceImpl.get(parameterMap);
	}

	/**
	 * 获取销售明细记录list
	 * 
	 * @param map
	 *            检索条件
	 * @return 销售明细记录list
	 */
	public List<Map<String, Object>> getSaleDetailList(
			Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID,
				"BINOLMBRPT07.getSaleDetailList");
		return baseServiceImpl.getList(parameterMap);
	}

}
