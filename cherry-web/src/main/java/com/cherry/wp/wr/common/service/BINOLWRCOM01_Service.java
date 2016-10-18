package com.cherry.wp.wr.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 门店报表共通Service
 * 
 * @author WangCT
 * @version 1.0 2014/09/23
 */
public class BINOLWRCOM01_Service extends BaseService {
	
	/**
	 * 获取大类和小类信息List
	 * 
	 * @param map 查询条件
	 * @return 大类和小类信息List
	 */
	public List<Map<String, Object>> getCategoryList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWRCOM01.getCategoryList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 获取逻辑仓库List
	 * 
	 * @param map 查询条件
	 * @return 逻辑仓库List
	 */
	public List<Map<String, Object>> getLogicInventoryList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLWRCOM01.getLogicInventoryList");
		return baseServiceImpl.getList(paramMap);
	}

}
