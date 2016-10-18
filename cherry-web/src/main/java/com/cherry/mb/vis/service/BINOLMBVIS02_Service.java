package com.cherry.mb.vis.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 会员回访计划管理Service
 * 
 * @author WangCT
 * @version 1.0 2014/12/11
 */
public class BINOLMBVIS02_Service extends BaseService {
	
	/**
	 * 取得会员回访计划总数
	 * 
	 * @param map 检索条件
	 * @return 会员回访计划总数
	 */
	public int getVisitPlanCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBVIS02.getVisitPlanCount");
		return baseServiceImpl.getSum(paramMap);
	}
	
	/**
	 * 取得会员回访计划List
	 * 
	 * @param map 检索条件
	 * @return 会员回访计划List
	 */
	public List<Map<String, Object>> getVisitPlanList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBVIS02.getVisitPlanList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 取得会员回访计划信息
	 * 
	 * @param map 检索条件
	 * @return 会员回访计划信息
	 */
	public Map<String, Object> getVisitPlanInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBVIS02.getVisitPlanInfo");
		return (Map<String, Object>)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 添加会员回访计划
	 * 
	 * @param map 添加内容
	 */
	public void addVisitPlan(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBVIS02.addVisitPlan");
		baseServiceImpl.save(paramMap);
	}
	
	/**
	 * 更新会员回访计划
	 * 
	 * @param map 更新内容和条件
	 * @return 更新件数
	 */
	public int updateVisitPlan(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBVIS02.updateVisitPlan");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 启用停用会员回访计划
	 * 
	 * @param map 更新内容和条件
	 * @return 更新件数
	 */
	public int updVisitPlanValid(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBVIS02.updVisitPlanValid");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 取得会员回访类型List
	 * 
	 * @param map 检索条件
	 * @return 会员回访类型List
	 */
	public List<Map<String, Object>> getVisitCategoryList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBVIS02.getVisitCategoryList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 取得会员问卷List
	 * 
	 * @param map 检索条件
	 * @return 会员问卷List
	 */
	public List<Map<String, Object>> getPaperList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBVIS02.getPaperList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 批量添加会员回访对象
	 * 
	 * @param list 会员回访对象List
	 */
	public void addVisitObj(List<Map<String, Object>> list) {
		baseServiceImpl.saveAll(list, "BINOLMBVIS02.addVisitObj");
	}
	
	/**
	 * 删除会员回访对象
	 * 
	 * @param map 删除条件
	 */
	public void delVisitObj(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBVIS02.delVisitObj");
		baseServiceImpl.remove(paramMap);
	}
	
	/**
	 * 取得会员回访对象件数
	 * 
	 * @param map 检索条件
	 * @return 会员回访对象件数
	 */
	public int getVisitObjCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBVIS02.getVisitObjCount");
		return baseServiceImpl.getSum(paramMap);
	}
	
	/**
	 * 取得会员回访对象List
	 * 
	 * @param map 检索条件
	 * @return 会员回访对象List
	 */
	public List<Map<String, Object>> getVisitObjList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBVIS02.getVisitObjList");
		return baseServiceImpl.getList(paramMap);
	}

}
