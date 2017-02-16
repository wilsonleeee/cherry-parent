/*
 * @(#)BINOLSSPRM88_Service.java     1.0 2013/10/17
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

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.cm.util.ConvertUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * 智能促销Service
 * 
 * @author lipc
 * @version 1.0 2013.10.17
 */
public class BINOLSSPRM88_Service extends BaseService {
	/**
	 * 删除促销活动规则
	 * @param map
	 * @return
	 */
	public int delPrmActivityRule (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM88.delPrmActivityRule");
		return baseServiceImpl.remove(map);
	}
	
	/**
	 * 删除促销活动条件
	 * @param map
	 * @return
	 */
	public int delPrmActCondition (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM88.delPrmActCondition");
		return baseServiceImpl.remove(map);
	}
	
	/**
	 * 删除促销活动结果 
	 * @param map
	 * @return
	 */
	public int delPrmActResult (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM88.delPrmActResult");
		return baseServiceImpl.remove(map);
	}
	
	/**
	 * 删除促销规则表 
	 * @param map
	 * @return
	 */
	public int delPrmActRule (Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM88.delPrmActRule");
		return baseServiceImpl.remove(map);
	}
	
	/**
	 * 删除促销规则表 
	 * @param map
	 * @return
	 */
	public int delPrmActRuleCate(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM88.delPrmActRuleCate");
		return baseServiceImpl.remove(map);
	}
	
	/**
	 * 取得活动信息
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getActRuleInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM88.getActRuleInfo");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
	
	/**
	 * 新增促销活动规则
	 * @param map
	 */
	public void addActRuleInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM88.addActRuleInfo");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 新增促销活动规则履历
	 * @param map
	 */
	public void addActRuleHisInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM88.addActRuleHisInfo");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 新增促销活动分类
	 * @param list
	 */
	public void addActRuleCate(List<Map<String, Object>> list){
		baseServiceImpl.saveAll(list, "BINOLSSPRM88.addActRuleCate");
	}
	
	/**
	 * 查询短键是否存在
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> isExistShortCode(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM88.isExistShortCode");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 
	 * @param map
	 * @return
	 */
	public int addCamp(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM88.addCamp");
		return (Integer)baseServiceImpl.get(map);
	}
	
	/**
	 * 
	 * @param map
	 * @return
	 */
	public int addCampRule(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM88.addCampRule");
		return (Integer)baseServiceImpl.get(map);
	}
	
	/**
	 * 
	 * @param map
	 * @return
	 */
	public int delCampain(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM88.delCampain");
		return ConvertUtil.getInt(baseServiceImpl.get(map));
	}
	
	/**
	 * 
	 * @param map
	 * @return
	 */
	public void delCampainRule(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM88.delCampainRule");
		baseServiceImpl.remove(map);
	}
	
	/**
	 * 
	 * @param map
	 * @return
	 */
	public void delCampRuleResCond(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM88.delCampRuleResCond");
		baseServiceImpl.remove(map);
	}
	
	/**
	 * 
	 * @param map
	 * @return
	 */
	public void addCampRuleResult(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM88.addCampRuleResult");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 
	 * @param map
	 * @return
	 */
	public void addCampRuleCondition(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM88.addCampRuleCondition");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 
	 * @param map
	 * @return
	 */
	public void addCampRuleConditionCust(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM88.addCampRuleConditionCust");
		baseServiceImpl.save(map);
	}

	/**
	 * 获取用户权限地点List Service
	 * @param map
	 * @return
     */
	public List<Map<String,Object>> getUserAuthorityPlaceList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM88.getUserAuthorityPlaceList");
		return baseServiceImpl.getList(map);
	}

	/**
	 * 获取促销活动地点List Service
	 * @param map
	 * @return
     */
	public List<Object> getProRulePlaceList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM88.getProRulePlaceList");
		return baseServiceImpl.getList(map);
	}

	/**
	 * 获取品牌Code
	 * @param map
	 * @return
	 */
	public String getBrandCode(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM88.getBrandCode");
		return (String) baseServiceImpl.get(map);
	}

	/**
	 * 取得部门ID
	 *
	 * @param map 部门代号
	 * @return Integer 部门ID
	 */
	public Integer getOrganizationId(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM88.getOrganizationId");
		return (Integer) baseServiceImpl.get(map);
	}

	/**
	 * 插入失败数据
	 * @param faillist
	 */
	public void insertFailDataList(List<Map<String,Object>> faillist){
		baseServiceImpl.saveAll(faillist,"BINOLSSPRM88.insertFailDataList");
	}

	/**
	 * 增量导入时插入原始数据
	 * @param map
     */
	public void insertOriginData(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM88.insertOriginData");
		baseServiceImpl.save(map);
	}

	/**
	 * SearchLog表增加插入记录
	 * @param map
     */
	public void addMemSearchLog(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM88.addMemSearchLog");
		baseServiceImpl.save(map);
	}

	/**
	 * 取得柜台信息
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getCounterInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM88.getCounterInfo");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}

	/**
	 * 插入到customInfo表中
	 * @param memberList
     */
	public void insertMemberList(List<Map<String,Object>> memberList){
		baseServiceImpl.saveAll(memberList,"BINOLSSPRM88.insertMemberList");
	}

	/**
	 * 失败会员插入到失败表
	 * @param failMemberList
     */
	public void insertFailMemberList(List<Map<String,Object>> failMemberList){
		baseServiceImpl.saveAll(failMemberList,"BINOLSSPRM88.insertFailMemberList");
	}

	public List<String> getFailUploadTotalList(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID,"BINOLSSPRM88.getFailUploadTotalList");
		return baseServiceImpl.getList(map);
	}

	/**
	 * 获取导入失败总数
	 * @param map
	 * @return
     */
	public int getFailUploadCount(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID,"BINOLSSPRM88.getFailUploadCount");
		return baseServiceImpl.getSum(map);
	}

	/**
	 * 获取导入失败list
	 * @param map
	 * @return
     */
	public List<String> getFailUploadList(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID,"BINOLSSPRM88.getFailUploadList");
		return baseServiceImpl.getList(map);
	}

	public int getOriginMobileCount(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID,"BINOLSSPRM88.getOriginMobileCount");
		return baseServiceImpl.getSum(map);
	}

	public List<String> getOriginMobileList(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID,"BINOLSSPRM88.getOriginMobileList");
		return baseServiceImpl.getList(map);
	}

	public int getMemberListCount(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID,"BINOLSSPRM88.getMemberListCount");
		return baseServiceImpl.getSum(map);
	}

	public List<Map<String, Object>> getMemberList(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID,"BINOLSSPRM88.getMemberList");
		return baseServiceImpl.getList(map);
	}

	public Map<String,Object> getProductInfo(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID,"BINOLSSPRM88.getProductInfo");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}

	/**
	 * 添加柜台黑名单
	 * @param placeList
     */
	public void addCounterListBlack(List<Map<String,Object>> placeList){
		baseServiceImpl.saveAll(placeList,"BINOLSSPRM88.addCounterListBlack");
	}

	/**
	 * 删除柜台黑名单表数据
	 * @param map
	 * @return
     */
	public int delCounterListBlack(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM88.delCounterListBlack");
		return baseServiceImpl.remove(map);
	}

	/**
	 * 获得促销活动柜台黑名单List
	 * @param map
	 * @return
     */
	public List<Map<String,Object>> getPlaceBlackList(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM88.getPlaceBlackList");
		return baseServiceImpl.getList(map);
	}

	/**
	 * 删除柜台黑名单表数据
	 * @param map
	 * @return
	 */
	public int deleteOldFailInfo(Map<String,Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM88.deleteOldFailInfo");
		return baseServiceImpl.remove(map);
	}

	/**
	 * 获取execl导入白名单数量 Service
	 * @param searchCode
	 * @return
     */
	public int getExeclMemberCount(String searchCode){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("searchCode",searchCode);
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM88.getExeclMemberCount");
		return baseServiceImpl.getSum(map);
	}
}