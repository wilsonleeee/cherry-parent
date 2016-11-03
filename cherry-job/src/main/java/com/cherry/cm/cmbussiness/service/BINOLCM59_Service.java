package com.cherry.cm.cmbussiness.service;

import java.util.List;
import java.util.Map;
import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.service.BaseService;

/**
 * 促销规则下发service
 * @author zhangle
 * @version 1.0 2014.01.14
 */
public class BINOLCM59_Service extends BaseService{
	
	/**
	 * 删除促销规则表数据（接口表）
	 * @param brandCode 品牌Code
	 */
	public void delProRuleSCS(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINOLCM59.delProRuleSCS");
		ifServiceImpl.remove(map);
	}
	
	/**
	 * 删除促销规则类型表数据（接口表）
	 * @param brandCode 品牌Code
	 */
	public void delProRuleCateSCS(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINOLCM59.delProRuleCateSCS");
		ifServiceImpl.remove(map);
	}
	
	/**
	 * 删除促销规则排他关系表数据（接口表）
	 * @param brandCode 品牌Code
	 */
	public void delProRuleRelationSCS(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINOLCM59.delProRuleRelationSCS");
		ifServiceImpl.remove(map);
	}
	
	/**
	 * 清除促销分类属性表（接口表）
	 * @param brandCode 品牌Code
	 */
	public void delProRuleCateBaseSCS(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINOLCM59.delProRuleCateBaseSCS");
		ifServiceImpl.remove(map);
	}
	
	/**
	 * 获取需要下发的促销规则信息
	 * @param brandInfoId 品牌ID
	 * @param organizationInfoId 组织ID
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getProRule(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINOLCM59.getProRule");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 获取需要下发的促销规则信息
	 * @param brandInfoId 品牌ID
	 * @param organizationInfoId 组织ID
	 * @return
	 */
	public List<Map<String, Object>> getProRule2(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINOLCM59.getProRule2");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 获取需要下发的促销分类信息
	 * @param brandInfoId 品牌ID
	 * @param organizationInfoId 组织ID
	 * @return
	 */
	public List<Map<String, Object>> getProRuleCate(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINOLCM59.getProRuleCate");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 获取促销规则排他关系信息
	 * @param brandInfoId 品牌ID
	 * @param organizationInfoId 组织ID
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getProRuleRelation(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINOLCM59.getProRuleRelation");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 获取促销分类属性信息
	 * @param brandInfoId 品牌ID
	 * @param organizationInfoId 组织ID
	 * @return
	 */
	public List<Map<String, Object>> getProRuleCateBase(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINOLCM59.getProRuleCateBase");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 添加促销规则记录（接口表）
	 * @param map
	 */
	public void insertProRuleSCS(List<Map<String, Object>> list) {
		ifServiceImpl.saveAll(list, "BINOLCM59.insertProRuleSCS");
	}
	
	/**
	 * 添加促销规则分类记录（接口表）
	 * @param map
	 */
	public void insertProRuleCateSCS(List<Map<String, Object>> list) {
		ifServiceImpl.saveAll(list, "BINOLCM59.insertProRuleCateSCS");
	}
	
	/**
	 * 添加促销规则排他关系记录（接口表）
	 * @param map
	 */
	public void insertProRuleRelationSCS(List<Map<String, Object>> list) {
		ifServiceImpl.saveAll(list, "BINOLCM59.insertProRuleRelationSCS");
	}
	
	/**
	 * 添加促销规则记录（接口表）
	 * @param map
	 */
	public void insertProRuleSCS2(List<Map<String, Object>> list) {
		ifServiceImpl.saveAll(list, "BINOLCM59.insertProRuleSCS2");
	}
	
	/**
	 * 添加促销规则分类记录（接口表）
	 * @param map
	 */
	public void insertProRuleCateSCS2(List<Map<String, Object>> list) {
		ifServiceImpl.saveAll(list, "BINOLCM59.insertProRuleCateSCS2");
	}
	
	/**
	 * 添加促销规则分类记录（接口表）
	 * @param map
	 */
	public void insertProRuleCateBaseSCS(List<Map<String, Object>> list) {
		ifServiceImpl.saveAll(list, "BINOLCM59.insertProRuleCateBaseSCS");
	}
}
