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

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.ConvertUtil;

/**
 * 
 * 智能促销Service
 * 
 * @author lipc
 * @version 1.0 2013.10.17
 */
public class BINOLSSPRM68_Service extends BaseService {
	/**
	 * 删除促销活动规则
	 * @param map
	 * @return
	 */
	public int delPrmActivityRule (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM68.delPrmActivityRule");
		return baseServiceImpl.remove(map);
	}
	
	/**
	 * 删除促销活动条件
	 * @param map
	 * @return
	 */
	public int delPrmActCondition (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM68.delPrmActCondition");
		return baseServiceImpl.remove(map);
	}
	
	/**
	 * 删除促销活动结果 
	 * @param map
	 * @return
	 */
	public int delPrmActResult (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM68.delPrmActResult");
		return baseServiceImpl.remove(map);
	}
	
	/**
	 * 删除促销规则表 
	 * @param map
	 * @return
	 */
	public int delPrmActRule (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM68.delPrmActRule");
		return baseServiceImpl.remove(map);
	}
	
	/**
	 * 删除促销规则表 
	 * @param map
	 * @return
	 */
	public int delPrmActRuleCate(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM68.delPrmActRuleCate");
		return baseServiceImpl.remove(map);
	}
	
	/**
	 * 取得活动信息
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getActRuleInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM68.getActRuleInfo");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
	
	/**
	 * 新增促销活动规则
	 * @param map
	 */
	public void addActRuleInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM68.addActRuleInfo");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 新增促销活动规则履历
	 * @param map
	 */
	public void addActRuleHisInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM68.addActRuleHisInfo");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 新增促销活动分类
	 * @param list
	 */
	public void addActRuleCate(List<Map<String, Object>> list){
		baseServiceImpl.saveAll(list, "BINOLSSPRM68.addActRuleCate");
	}
	
	/**
	 * 查询短键是否存在
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> isExistShortCode(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM68.isExistShortCode");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 
	 * @param map
	 * @return
	 */
	public int addCamp(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM68.addCamp");
		return (Integer)baseServiceImpl.get(map);
	}
	
	/**
	 * 
	 * @param map
	 * @return
	 */
	public int addCampRule(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM68.addCampRule");
		return (Integer)baseServiceImpl.get(map);
	}
	
	/**
	 * 
	 * @param map
	 * @return
	 */
	public int delCampain(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM68.delCampain");
		return ConvertUtil.getInt(baseServiceImpl.get(map));
	}
	
	/**
	 * 
	 * @param map
	 * @return
	 */
	public void delCampainRule(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM68.delCampainRule");
		baseServiceImpl.remove(map);
	}
	
	/**
	 * 
	 * @param map
	 * @return
	 */
	public void delCampRuleResCond(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM68.delCampRuleResCond");
		baseServiceImpl.remove(map);
	}
	
	/**
	 * 
	 * @param map
	 * @return
	 */
	public void addCampRuleResult(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM68.addCampRuleResult");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 
	 * @param map
	 * @return
	 */
	public void addCampRuleCondition(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM68.addCampRuleCondition");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 
	 * @param map
	 * @return
	 */
	public void addCampRuleConditionCust(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM68.addCampRuleConditionCust");
		baseServiceImpl.save(map);
	}

	/**
	 * 获取用户权限地点List Service
	 * @param map
	 * @return
     */
	public List<Map<String,Object>> getUserAuthorityPlaceList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM68.getUserAuthorityPlaceList");
		return baseServiceImpl.getList(map);
	}

	/**
	 * 获取促销活动地点List Service
	 * @param map
	 * @return
     */
	public List<String> getProRulePlaceList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM68.getProRulePlaceList");
		return baseServiceImpl.getList(map);
	}
}