package com.cherry.webservice.unique.service;

import java.util.HashMap;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 唯一码查询 接口Service
 * 
 * @author zhouwei
 * @version 2016-06-12 1.0.0
 */
public class UniqueCodeSearchService extends BaseService {

	/**
	 * 查询唯一码信息
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getPrtUniqueInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID,  "UniqueCodeSearch.getPrtUniqueInfo");
		return (Map<String, Object>)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 查询积分唯一码是否存在
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getPrtUnique(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID,  "UniqueCodeSearch.getPrtUnique");
		return (Map<String, Object>)baseServiceImpl.get(paramMap);
	}
	
}
