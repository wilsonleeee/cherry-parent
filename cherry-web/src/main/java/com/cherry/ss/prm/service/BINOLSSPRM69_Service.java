/*
 * @(#)BINOLSSPRM68_Service.java     1.0 2013/10/17
 * 
 * Copyright (c) 2010 SHANGHAI BINGKUN DIGITAL TECHNOLOGY CO.,LTD
 * All rights reserved
 * 
 * This software is the confidential and proprietary information of 
 * SHANGHAI BINGKUN.("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with SHANGHAI BINGKUN.
 */
package com.cherry.ss.prm.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.BaseServiceImpl;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 智能促销Service
 * 
 * @author lipc
 * @version 1.0 2013.10.17
 */
public class BINOLSSPRM69_Service extends BaseService {
	
	@Resource
	private BaseServiceImpl baseServeceImpl;
	
	/**
	 * 取得当前有效的促销规则
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPrmRuleList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM69.getPrmRuleList");
		return baseServeceImpl.getList(map);
	}
	
	/**
	 * 取得当前有效的促销规则总数
	 * @param map
	 * @return
	 */
	public int getPrmRuleCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM69.getPrmRuleCount");
		return baseServeceImpl.getSum(map);
	}
	
	/**
	 * 分页取得当前有效的促销规则
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPrmRulePageList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM69.getPrmRulePageList");
		return baseServeceImpl.getList(map);
	}

	/**
	 * 批量更新促销规则
	 * @param list
	 */
	public void updatePrmRule(List<Map<String, Object>> list) {
		baseServeceImpl.updateAll(list, "BINOLSSPRM69.updatePrmRule");
	}
	
	/**
	 * 取得促销规则排他关系
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPrmRuleRelationList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM69.getPrmRuleRelationList");
		return baseServeceImpl.getList(map);
	}
	
	/**
	 * 取得排他关系分组
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPrmRuleRelationGroupList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM69.getPrmRuleRelationGroupList");
		return baseServeceImpl.getList(map);
	}
	
	/**
	 * 根据ID获取排他关系分组
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getPrmRuleRelationGroup(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM69.getPrmRuleRelationGroup");
		return (Map<String, Object>) baseServeceImpl.get(map);
	}
	
	/**
	 * 添加排他关系分组
	 * @param map
	 * @return
	 */
	public int insertPrmRuleRelationGroup(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM69.insertPrmRuleRelationGroup");
		return baseServeceImpl.saveBackId(map);
	}
	
	/**
	 * 添加排他关系
	 * @param map
	 * 
	 */
	public void insertPrmRuleRelation(List<Map<String, Object>> list) {
		baseServeceImpl.saveAll(list, "BINOLSSPRM69.insertPrmRuleRelation");
	}
	
	/**
	 * 更新排他关系分组
	 * @param map
	 * @return
	 */
	public int updatePrmRuleRelationGroup(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM69.updatePrmRuleRelationGroup");
		return baseServeceImpl.update(map);
	}
	
	/**
	 * 停用或启用排他关系分组
	 * @param map
	 * @return
	 */
	public int disOrEnablePrmRuleRelationGroup(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM69.disOrEnablePrmRuleRelationGroup");
		return baseServeceImpl.update(map);
	}
	
	/**
	 * 更新排他关系
	 * @param map
	 * @return
	 */
	public int delPrmRuleRelation(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM69.delPrmRuleRelation");
		return baseServeceImpl.remove(map);
	}
	
}