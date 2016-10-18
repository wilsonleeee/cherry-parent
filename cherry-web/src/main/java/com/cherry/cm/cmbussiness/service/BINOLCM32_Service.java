/*
 * @(#)BINOLCM05_Service.java     1.0 2012/12/19
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
package com.cherry.cm.cmbussiness.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.CherryUtil;
/**
 * 沟通共通 Service
 * 
 * @author ZhangGS
 * @version 1.0 2012.12.19
 */
public class BINOLCM32_Service extends BaseService{
	/**
	 * 取活动名称列表
	 * @param map
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List getActivityList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM32.getActivityList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 取活动名称
	 * @param map
	 * @return map
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getActivityInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM32.getActivityInfo");
		return (Map<String, Object>)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 根据活动编号取得沟通计划编号
	 * @param map
	 * @return 沟通计划编号
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getPlanInfoByCampaign(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM32.getPlanInfoByCampaign");
		return (Map<String, Object>) baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 取搜索记录名称
	 * @param map
	 * @return 记录信息
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getObjRecordInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM32.getObjRecordInfo");
		return (Map<String, Object>)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 根据活动ID或Code获取活动的沟通对象搜索集合List
	 * @param map
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List getCommObjListByCampaign(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM32.getCommObjListByCampaign");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 取沟通模板数量
	 * @param map
	 * @return 沟通模板数量
	 */
	public int getMsgTemplateCount (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM32.getMsgTemplateCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 取沟通模板List
	 * @param map
	 * @return 沟通模板List
	 */
	@SuppressWarnings("rawtypes")
	public List getMsgTemplateList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM32.getMsgTemplateList");
		return baseServiceImpl.getList(map);
	}
	
	/**
     * 根据搜索记录编号查询非会员搜索结果
     * 
     * @param map
     * @return
     * 		搜索结果明细
     */
    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> getNoMemberSearchResultList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM32.getNoMemberSearchResultList");
        return baseServiceImpl.getList(paramMap);
    }
    
    /**
     * 根据搜索记录编号查询非会员搜索结果总数
     * 
     * @param map
     * @return
     * 		搜索结果总数
     */
    public int getNoMemberSearchResultCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM32.getNoMemberSearchResultCount");
		return CherryUtil.obj2int(baseServiceImpl.get(map));
	}
    
	/**
     * 根据搜索记录编号查询搜索结果
     * 
     * @param map
     * @return
     * 		搜索结果明细
     */
    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> getSearchResultList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM32.getSearchResultList");
        return baseServiceImpl.getList(paramMap);
    }
    
    /**
     * 根据搜索记录编号查询搜索结果总数
     * 
     * @param map
     * @return
     * 		搜索结果总数
     */
    public int getSearchResultCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM32.getSearchResultCount");
		return CherryUtil.obj2int(baseServiceImpl.get(map));
	}
    
	/**
	 * 获取沟通模版变量
	 * @param map
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getVariableList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM32.getVariable");
        return baseServiceImpl.getList(paramMap);
    }
	/**
	 * 获取沟通模版变量
	 * @param map
	 * @return Map
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getVariable(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM32.getVariable");
        return (Map<String, Object>) baseServiceImpl.get(paramMap);
    }
	
	/**
	 * 获取沟通模版内容非法字符List
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getIllegalCharList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM32.getIllegalCharList");
		return baseServiceImpl.getList(paramMap);
    }
	
	/**
     * 根据沟通阶段标识检查沟通是否存在
     * 
     * @param map
     * @return
     * 		沟通编号
     */
    public String getCommunicationByPhaseNum(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLCM32.getCommunicationByPhaseNum");
        return (String) baseServiceImpl.get(paramMap);
    }
	
}
