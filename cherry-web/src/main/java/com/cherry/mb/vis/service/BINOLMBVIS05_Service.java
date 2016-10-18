package com.cherry.mb.vis.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class BINOLMBVIS05_Service extends BaseService {
	
	/**
	 * 取得回访信息总数
	 * 
	 * @param map 检索条件
	 * @return 回访信息总数
	 */
	public int getVisitTaskCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBVIS05.getVisitTaskCount");
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
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBVIS05.getVisitTaskList");
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

	public List<Map<String, Object>> getSaleDetailByMemberCodeFL(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBVIS05.getSaleDetailByMemberCodeFL");
		return baseServiceImpl.getList(paramMap);
	}
	
	public void insertMemberVisit(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBVIS05.insertMemberVisit");
		baseServiceImpl.save(paramMap);
	}
	
	public void updateVisitTask(Map<String,Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBVIS05.updateVisitTask");
		baseServiceImpl.update(paramMap);
	}
	
	public Map<String,Object> getMemberInfo(Map<String,Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBVIS05.getMemberInfo");
		return (Map<String,Object>)baseServiceImpl.get(paramMap);
	}
}
