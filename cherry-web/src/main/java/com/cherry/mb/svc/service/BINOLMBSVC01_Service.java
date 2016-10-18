package com.cherry.mb.svc.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * @ClassName: BINOLMBSVC01_Service 
 * @Description: TODO(储值规则管理Service) 
 * @author menghao
 * @version v1.0.0 2016-6-28 
 *
 */
public class BINOLMBSVC01_Service extends BaseService{
	
	/**
	 * 插入主规则信息表
	 * @param params
	 */
	public int addMainRule(Map<String,Object> params){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(params);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBSVC01.addMainRule");
		return baseServiceImpl.saveBackId(paramMap);
	}
	
	/**
	 * 插入规则明细信息表
	 * @param params
	 */
	public void addRule(Map<String,Object> params){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(params);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBSVC01.addRule");
		baseServiceImpl.save(paramMap);
	}
	
	/**
	 * 插入储值卡优惠规则使用地点表
	 * @param list
	 */
	public void addRechargeRulePlace(List<Map<String, Object>> list) {
		baseServiceImpl.saveAll(list, "BINOLMBSVC01.addRechargeRulePlace");
	}
	
	/**
	 * 删除旧的储值卡优惠规则使用地点表
	 * @param map
	 */
	public void delRechargeRulePlace(String discountId) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("discountId",discountId);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBSVC01.delRechargeRulePlace");
		baseServiceImpl.remove(paramMap);
	}
	
	/**
	 * 获取规则详细信息
	 * @return
	 */
	public Map<String,Object> getRuleDetail(){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBSVC01.getRuleDetail");
		return (Map<String,Object>)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 获取规则概览统计信息
	 * @param map
	 * @return
	 */
	public Map<String, Object> getRuleCountInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBSVC01.getRuleCountInfo");
		return (Map)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 查询规则概览信息List
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getRuleList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBSVC01.getRuleList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 逻辑删除子规则
	 * @param map
	 * @return
	 */
	public int updateRuleVaild(Map<String,Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBSVC01.updateRuleVaild");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 逻辑删除主规则
	 * @param map
	 * @return
	 */
	public int updateMainRuleValid(Map<String,Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBSVC01.updateMainRuleValid");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 更新主规则
	 * @param map
	 * @return
	 */
	public int updateMainRule(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBSVC01.updateMainRule");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 更新子规则
	 * @param map
	 * @return
	 */
	public int updateRule(Map<String,Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBSVC01.updateRule");
		return baseServiceImpl.update(paramMap);
	}
	
	public Map<String, Object> getRuleDetail(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBSVC01.getRuleDetail");
		return (Map<String, Object>)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 校验规则明细
	 * @param map
	 * @return
	 */
	public Map<String,Object> checkRule(Map<String,Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBSVC01.checkRule");
		return (Map)baseServiceImpl.get(paramMap);
	}

	/**
	 * 取得储值卡优惠规则的使用柜台组织结构ID信息
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getCounterOrganiztionId(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBSVC01.getCounterOrganiztionId");
		return baseServiceImpl.getList(paramMap);
	}

	 /**
     * 取得所有区域柜台树，有带权限
     * 
     * @param map
     * @return 所有区域柜台树List
     */
	public List<Map<String, Object>> getAllCounter(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBSVC01.getAllCounter");
        return baseServiceImpl.getList(parameterMap);
	}

	/**
	 * 取得按组织结构柜台树
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getDepartCntList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBSVC01.getDepartCntList");
        return baseServiceImpl.getList(parameterMap);
	}

	/**
	 * 取大区信息
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getRegionList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBSVC01.getRegionList");
        return baseServiceImpl.getList(parameterMap);
	}

	/**
	 * 取得渠道柜台树
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getChannelCntList(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBSVC01.getChannelCntList");
        return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 统计 规则的限定的柜台数----规则使用地点表
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getApplyCntCount(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBSVC01.getApplyCntCount");
        return baseServiceImpl.getList(parameterMap);
	}
	
	/**
	 * 统计 规则的发送储值业务柜台数及参与储值人数----业务交易流水表
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getUsedCntCountAndInvolveNumber(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMBSVC01.getUsedCntCountAndInvolveNumber");
        return baseServiceImpl.getList(parameterMap);
	}
 }
