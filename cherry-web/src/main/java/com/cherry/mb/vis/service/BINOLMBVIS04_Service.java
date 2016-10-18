package com.cherry.mb.vis.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class BINOLMBVIS04_Service extends BaseService {
	
	/**
	 * 取得回访信息总数
	 * 
	 * @param map 检索条件
	 * @return 回访信息总数
	 */
	public int getVisitTaskCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBVIS04.getVisitTaskCount");
		return baseServiceImpl.getSum(paramMap);
	}

	/**
	 * 取得回访信息List
	 * 
	 * @param map 检索条件
	 * @return 回访信息List
	 */
	public List<Map<String, Object>> getVisitTaskList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBVIS04.getVisitTaskList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 取消回访任务
	 * 
	 * @param list 条件
	 */
	public void updateVisitTaskState(List<Map<String, Object>> list) {
		baseServiceImpl.updateAll(list, "BINOLMBVIS04.updateVisitTaskState");
	}

}
