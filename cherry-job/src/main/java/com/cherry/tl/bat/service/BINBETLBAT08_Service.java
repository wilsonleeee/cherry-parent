package com.cherry.tl.bat.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.service.BaseService;

public class BINBETLBAT08_Service extends BaseService {

	/**
	 * 判断指定字段时否存在
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getTableColumn(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBETLBAT08.getTableColumn");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 取得待处理（加密）的数据
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPreHandleData(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID,
				"BINBETLBAT08.getPreHandleData");
		return baseServiceImpl.getList(paramMap);
	}
	
	/***
	 * 将加密后的数据写数据库中
	 * @param list
	 */
	public void updateHandleData(List<Map<String, Object>> list) {
		baseServiceImpl.updateAll(list, "BINBETLBAT08.updateHandleData");
	}
	
}
