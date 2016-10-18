package com.cherry.mb.vis.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 会员回访类型管理Service
 * 
 * @author WangCT
 * @version 1.0 2014/12/11
 */
public class BINOLMBVIS03_Service extends BaseService {
	
	/**
	 * 取得会员回访类型总数
	 * 
	 * @param map 检索条件
	 * @return 会员回访类型总数
	 */
	public int getVisitCategoryCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBVIS03.getVisitCategoryCount");
		return baseServiceImpl.getSum(paramMap);
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
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBVIS03.getVisitCategoryList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 取得会员回访类型信息
	 * 
	 * @param map 检索条件
	 * @return 会员回访类型信息
	 */
	public Map<String, Object> getVisitCategoryInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBVIS03.getVisitCategoryInfo");
		return (Map<String, Object>)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 添加会员回访类型
	 * 
	 * @param map 添加内容
	 */
	public void addVisitCategory(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBVIS03.addVisitCategory");
		baseServiceImpl.save(paramMap);
	}
	
	/**
	 * 更新会员回访类型
	 * 
	 * @param map 更新内容和条件
	 * @return 更新件数
	 */
	public int updateVisitCategory(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBVIS03.updateVisitCategory");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 启用停用会员回访类型
	 * 
	 * @param map 更新内容和条件
	 * @return 更新件数
	 */
	public int updVisitCategoryValid(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBVIS03.updVisitCategoryValid");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 通过回访类型代码取得会员回访类型ID
	 * 
	 * @param map 查询条件
	 * @return 会员回访类型ID
	 */
	public String getVisitCategoryByCode(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBVIS03.getVisitCategoryByCode");
		return (String)baseServiceImpl.get(paramMap);
	}

}
