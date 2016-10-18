package com.cherry.mb.svc.service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class BINOLMBSVC05_Service extends BaseService{
	/**
	 * 获取储值卡交易概览统计信息
	 * @param map
	 * @return
	 */
	public Map<String, Object> getTradeCountInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBSVC05.getTradeCountInfo");
		return (Map)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 查询储值卡交易概览信息List
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getTradeList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBSVC05.getTradeList");
		return baseServiceImpl.getList(paramMap);
	}
}
