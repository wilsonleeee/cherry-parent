package com.cherry.hmw.ft.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.service.BaseService;

/**
 * 海名微利润分摊Service
 * 
 * @author songka
 * @version 1.0 2015.09.08
 */
public class BINBEHMWFT01_Service extends BaseService{
	
	/**
	 * 查询未分摊销售信息
	 * @param paraMap 
	 * @param paramMap 查询条件
	 * @return 柜台ID
	 */
	public List<Map<String, Object>> getSalList(Map<String, Object> paraMap) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(paraMap);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEHMWFT01.getSalList");
		return (List<Map<String, Object>>) baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 查询返利信息
	 * @param paramMap 查询条件
	 * @return 员工等级,返利百分比
	 */
	public List<Map<String, Object>> getRebateDivideList() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEHMWFT01.getRebateDivideList");
		return (List<Map<String, Object>>) baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 插入返利信息
	 * @param salList 
	 * @return null
	 */
	public void InsertRebateDivideList(List<Map<String, Object>> salList) {
		baseServiceImpl.saveAll(salList, "BINBEHMWFT01.InsertRebateDivideList");
	}
	
	/**
	 * 查询上级员工信息
	 * @param parMap 
	 * @return Employee
	 */
	public Map<String, Object> getEmployee(Map<String, Object> parMap) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(parMap);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEHMWFT01.getEmployee");
		return (Map<String, Object>) baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 更新销售主表为已分摊利润状态
	 * @param list 
	 */
	public void updSaleRecord(List<Map<String, Object>> list) {
		baseServiceImpl.updateAll(list, "BINBEHMWFT01.updSaleRecord");
	}
	
	/**
	 * 查询员工所属等级
	 * @param  parMap
	 */
	public Integer getLevel(Map<String, Object> parMap) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(parMap);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEHMWFT01.getLevel");
		return (Integer)baseConfServiceImpl.get(paramMap);
	}

	public void updateProfitRebate(Map<String, Object> parMap) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(parMap);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEHMWFT01.updateProfitRebate");
		baseServiceImpl.update(paramMap);
	}

	public List<Map<String, Object>> getSalList2(Map<String, Object> paraMap) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(paraMap);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEHMWFT01.getSalList2");
		return (List<Map<String, Object>>) baseServiceImpl.getList(paramMap);
	}
}
