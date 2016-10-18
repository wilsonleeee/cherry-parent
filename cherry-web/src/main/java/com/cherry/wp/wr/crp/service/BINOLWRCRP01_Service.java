package com.cherry.wp.wr.crp.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 客户预约登记查询Service
 * 
 * @author menghao
 * @version 1.0 2014/12/24
 */
public class BINOLWRCRP01_Service extends BaseService {
	
	/**
	 *  获取客户预约登记记录数量
	 * 
	 * @param map 查询条件
	 * @return 
	 */
	public int getCampaignOrderCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWRCRP01.getCampaignOrderCount");
		return baseServiceImpl.getSum(paramMap);
	}
	
	/**
	 *  获取客户预约登记记录List
	 * 
	 * @param map 查询条件
	 * @return 
	 */
	public List<Map<String, Object>> getCampaignOrderList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWRCRP01.getCampaignOrderList");
		return baseServiceImpl.getList(paramMap);
	}
	
}
