package com.cherry.cp.point.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class BINOLCPPOI01_Service extends BaseService{

	/**
	 * 取得区域信息
	 * @param map
	 * @return
	 */
	public List getRegionInfoList (Map<String, Object> map){
		map.put("userId", map.get("userID"));
		map.put("businessType", "1");
		map.put("operationType", "1");
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPPOI01.getRegionInfoList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得渠道信息
	 * @param map
	 * @return
	 */
	public List getChannelInfoList (Map<String,Object> map){
		map.put("userId", map.get("userID"));
		map.put("businessType", "1");
		map.put("operationType", "1");
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPPOI01.getChannelInfoList");
		return baseServiceImpl.getList(map);
	}
	
	public List getOrganizationInfoList(Map<String,Object> map){
		map.put("userId", map.get("userID"));
		map.put("businessType", "1");
		map.put("operationType", "1");
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPPOI01.getOrganizationInfoList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得渠道信息
	 * @param map
	 * @return
	 */
	public List getCntInfoList (Map<String,Object> map){
		map.put("userId", map.get("userID"));
		map.put("businessType", "1");
		map.put("operationType", "1");
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPPOI01.getCntInfoList");
		return baseServiceImpl.getList(map);
	}
	
	
	/**
	 * 取得柜台信息
	 * @param map
	 * @return
	 */
	public List getCounterInfoList(Map<String, Object> map){
		map.put("userId", map.get("userID"));
		map.put("businessType", "1");
		map.put("operationType", "1");
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPPOI01.getCounterInfoList");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得会员等级有效期
	 * 
	 * @param map 
	 * 				查询条件
	 * @return List
	 * 				会员等级有效期
	 */
	@SuppressWarnings("unchecked")
	public List getLevelDateList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPPOI01.getLevelDateList");
		return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 取得会员等级有效期
	 * 
	 * @param map 
	 * 				查询条件
	 * @return List
	 * 				会员等级有效期
	 */
	public int getCounterCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPPOI01.getCounterCount");
		return baseServiceImpl.getSum(parameterMap);
	}
	
	/**
	 * 取得品牌信息
	 * 
	 * @param map 
	 * 				查询条件
	 * @return map
	 * 				品牌信息
	 */
	public Map<String, Object> getBrandInfo(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPPOI01.getPOIBrandInfo");
		return (Map<String, Object>) baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 取得柜台名
	 * 
	 * @param map 
	 * 				查询条件
	 * @return String
	 * 				柜台名
	 */
	public String getCounterName(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPPOI01.getCounterName");
		return (String) baseServiceImpl.get(parameterMap);
	}
	
	/**
	 * 取得城市数量数
	 * 
	 * @param map 
	 * 				查询条件
	 * @return List
	 * 				城市数量数
	 */
	public int getCityCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("userId", map.get("userID"));
		parameterMap.put("businessType", "1");
		parameterMap.put("operationType", "1");
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPPOI01.getCityCount");
		return baseServiceImpl.getSum(parameterMap);
	}
	
	/**
	 * 取得渠道数量数
	 * 
	 * @param map 
	 * 				查询条件
	 * @return List
	 * 				渠道数量数
	 */
	public int getChannelCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("userId", map.get("userID"));
		parameterMap.put("businessType", "1");
		parameterMap.put("operationType", "1");
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPPOI01.getChannelCount");
		return baseServiceImpl.getSum(parameterMap);
	}
	 /**
     * 取得活动对象信息
     * 
     * @param map
     * @return list
     */
    public List<Map<String, Object>> getCustomerList(Map<String, Object> map) {
    	Map<String, Object> param = new HashMap<String, Object>(map);
    	param.put(CherryConstants.IBATIS_SQL_ID, "BINOLCPCOM02.getCustomerList");
    	return baseServiceImpl.getList(param);
    }
}
