package com.cherry.pt.rps.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class BINOLPTRPS32_Service extends BaseService {
	
	/**
	 * 取得代理商优惠券使用情况信息LIST的行数
	 * @param map
	 * @return
	 */
	public int getBaCouponUsedInfoCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS32.getBaCouponUsedInfoCount");
		return baseServiceImpl.getSum(paramMap);
	}
	
	/**
	 * 取得代理商优惠券使用情况信息LIST
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getBaCouponUsedInfoList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS32.getBaCouponUsedInfoList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 取得汇总信息
	 * 
	 * */
	public Map<String,Object> getSumInfo(Map<String,Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS32.getSumInfo");
		return (Map<String,Object>)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 根据代理商ID取得代理商名称
	 * @param map
	 * @return
	 */
	public Map<String, Object> getResellerNameFromCode(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS32.getResellerNameFromCode");
		return (Map<String, Object>)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 取得指定代理商的优惠券的使用情况详细一览行数 
	 * @param map
	 * @return
	 */
	public int getCouponUsedDetailCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS32.getCouponUsedDetailCount");
		return baseServiceImpl.getSum(paramMap);
	}
	
	/**
	 * 取得指定代理商的优惠券的使用情况详细一览LIST
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getCouponUsedDetailList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTRPS32.getCouponUsedDetailList");
		return baseServiceImpl.getList(paramMap);
	}
}
