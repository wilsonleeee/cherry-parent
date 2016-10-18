package com.cherry.yh.rep.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 按订单详细报表Service
 * 
 * @author menghao
 * 
 */
public class BINOLYHREP01_Service extends BaseService {
	
	/**
	 * 取得使用优惠券订单的汇总信息
	 * 
	 * */
	public Map<String,Object> getSumInfo(Map<String,Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLYHREP01.getSumInfo");
		return (Map<String,Object>)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 取得使用优惠券订单的汇总信息
	 * 
	 * */
	public Map<String,Object> getSumNoUsedInfo(Map<String,Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLYHREP01.getSumNoUsedInfo");
		return (Map<String,Object>)baseServiceImpl.get(paramMap);
	}

	/**
	 * 取得订单总数
	 * 
	 * @param map
	 * @return
	 */
	public int getSaleOrderDetailCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLYHREP01.getSaleOrderDetailCount");
		return baseServiceImpl.getSum(paramMap);
	}

	/**
	 * 取得订单List
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getSaleOrderDetailList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLYHREP01.getSaleOrderDetailList");
		return baseServiceImpl.getList(paramMap);
	}
}
