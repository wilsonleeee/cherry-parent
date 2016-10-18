package com.cherry.pt.rps.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class BINOLPTRPS31_Service extends BaseService {

	/**
	 * 取得代理商提成统计信息LIST的行数
	 * @param map
	 * @return
	 */
	public int getBaCommissionCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS31.getBaCommissionCount");
		return baseServiceImpl.getSum(paramMap);
	}
	
	/**
	 * 取得代理商提成统计信息LIST
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getBaCommissionList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS31.getBaCommissionList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 取得汇总信息
	 * 
	 * */
	public Map<String,Object> getSumInfo(Map<String,Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS31.getSumInfo");
		return (Map<String,Object>)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 根据代理商ID取得代理商名称
	 * @param map
	 * @return
	 */
	public Map<String, Object> getBaNameFromId(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS31.getBaNameFromId");
		return (Map<String, Object>)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 取得指定代理商的推荐会员购买信息的数量
	 * @param map
	 * @return
	 */
	public int getMemberBuyInfoCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS31.getMemberBuyInfoCount");
		return baseServiceImpl.getSum(paramMap);
	}
	
	/**
	 * 取得指定代理商的推荐会员购买信息LIST
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getMemberBuyInfoList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS31.getMemberBuyInfoList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 取得代理商推荐购买信息的数量
	 * @param map
	 * @return
	 */
	public int getBaSaleInfoCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS31.getBaSaleInfoCount");
		return baseServiceImpl.getSum(paramMap);
	}
	
	/**
	 * 取得代理商推荐购买信息LIST
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getBaSaleInfoList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS31.getBaSaleInfoList");
		return baseServiceImpl.getList(paramMap);
	}
}
