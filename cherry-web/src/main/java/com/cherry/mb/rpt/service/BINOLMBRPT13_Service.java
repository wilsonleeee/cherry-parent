package com.cherry.mb.rpt.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class BINOLMBRPT13_Service extends BaseService {
	
	/**
	 * 取得活动购买金额、购买人数、新会员购买人数、新会员购买金额
	 * 
	 * @param map 检索条件
	 * @return 活动购买金额、购买人数、新会员购买人数、新会员购买金额
	 */
	public Map<String, Object> getCampSaleInfo(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBRPT13.getCampSaleInfo");
		return (Map<String, Object>)baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 取得活动预约人数
	 * 
	 * @param map 检索条件
	 * @return 活动预约人数
	 */
	public int getCampOrderCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBRPT13.getCampOrderCount");
		return baseServiceImpl.getSum(parameterMap);
	}
	
	/**
	 * 取得活动预期到柜人数
	 * 
	 * @param map 检索条件
	 * @return 活动预期到柜人数
	 */
	public int getCampBookCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBRPT13.getCampBookCount");
		return baseServiceImpl.getSum(parameterMap);
	}
	
	/**
	 * 取得活动实际到柜人数（会员） 
	 * 
	 * @param map 检索条件
	 * @return 活动实际到柜人数（会员） 
	 */
	public int getCampMemArriveCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBRPT13.getCampMemArriveCount");
		return baseServiceImpl.getSum(parameterMap);
	}
	
	/**
	 * 取得活动实际到柜人数（非会员）
	 * 
	 * @param map 检索条件
	 * @return 活动实际到柜人数（非会员）
	 */
	public int getCampNomemArriveCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBRPT13.getCampNomemArriveCount");
		return baseServiceImpl.getSum(parameterMap);
	}
	
	/**
	 * 取得活动按柜台统计信息List
	 * 
	 * @param map 检索条件
	 * @return 活动按柜台统计信息List
	 */
	public List<Map<String, Object>> getCampCountInfoByCounterList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBRPT13.getCampCountInfoByCounterList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 取得活动按柜台统计信息件数
	 * 
	 * @param map 检索条件
	 * @return 活动按柜台统计信息件数
	 */
	public int getCampCountInfoByCounterCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBRPT13.getCampCountInfoByCounterCount");
		return baseServiceImpl.getSum(parameterMap);
	}
	
	/**
	 * 取得活动List
	 * 
	 * @param map 检索条件
	 * @return 活动List
	 */
	public List<Map<String, Object>> getCampaignList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBRPT13.getCampaignList");
		return baseServiceImpl.getList(parameterMap);
	}

}
