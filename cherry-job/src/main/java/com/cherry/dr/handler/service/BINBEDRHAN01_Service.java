/*	
 * @(#)BINBEDRHAN01_Service.java     1.0 2011/08/26	
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
package com.cherry.dr.handler.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.dr.cmbussiness.dto.core.CampBaseDTO;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 会员等级和化妆次数重算Service
 * 
 * @author WangCT
 * @version 1.0 2011/08/26	
 */
public class BINBEDRHAN01_Service extends BaseService {
	
	/**
	 * 
	 * 查询重算信息
	 * 
	 * @param map 查询参数
	 * @return 重算信息List
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getReCalcInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRHAN01.getReCalcInfo");
		return (List)baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 
	 * 查询需要重算的化妆次数积分的使用记录
	 * 
	 * @param map 查询参数
	 * @return 化妆次数积分的使用记录List
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getMemUsedCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRHAN01.getMemUsedCount");
		return (List)baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 
	 * 查询需要重算的预约单信息
	 * 
	 * @param map 查询参数
	 * @return 预约单信息List
	 * 
	 */
	public List<Map<String, Object>> getOrderTicketList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRHAN01.getOrderTicketList");
		return (List)baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 
	 * 查询预约单明细信息
	 * 
	 * @param campBaseDTO 查询条件
	 * @return 预约单明细信息
	 */
	public List<Map<String, Object>> getOrderDetailList(CampBaseDTO campBaseDTO){
		return (List) baseServiceImpl.getList(campBaseDTO, "BINBEDRHAN01.getOrderDetailList");
	}
	
	/**
	 * 
	 * 查询需要重算的销售记录
	 * 
	 * @param map 查询参数
	 * @return 销售记录List
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getSaleRecord(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRHAN01.getSaleRecord");
		return (List)baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 
	 * 查询需要重算的订单记录
	 * 
	 * @param map 查询参数
	 * @return 销售记录List
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getESOrderRecord(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRHAN01.getESOrderRecord");
		return (List)baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 
	 * 查询会员俱乐部信息
	 * 
	 * @param map 查询参数
	 * @return 会员俱乐部信息List
	 * 
	 */
	public List<Map<String, Object>> getMemberClubList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRHAN01.getMemberClubList");
		return (List)baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 
	 * 查询会员信息
	 * 
	 * @param map 查询参数
	 * @return 会员信息
	 * 
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getMemberInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRHAN01.getMemberInfo");
		return (Map)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 
	 * 取得最早重复的订单时间
	 * 
	 * @param map 查询参数
	 * @return 会员信息
	 * 
	 */
	public Map<String, Object> getMinMultiTime(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRHAN01.getMinMultiTime");
		return (Map)baseServiceImpl.get(map);
	}
	
	/**
	 * 
	 * 查询会员最新规则执行履历记录
	 * 
	 * @param map 查询参数
	 * @return 会员最新规则执行履历记录
	 * 
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getNewRuleExecRecord(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRHAN01.getNewRuleExecRecord");
		return (Map)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 
	 * 查询会员俱乐部等级信息
	 * 
	 * @param map 查询参数
	 * @return 会员俱乐部等级信息
	 * 
	 */
	public Map<String, Object> getMemClubLevelInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRHAN01.getMemClubLevelInfo");
		return (Map)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 
	 * 查询会员俱乐部信息
	 * 
	 * @param map 查询参数
	 * @return 会员俱乐部信息
	 * 
	 */
	public Map<String, Object> selMemClubInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRHAN01.selMemClubInfo");
		return (Map)baseServiceImpl.get(paramMap);
	}
	/**
	 * 
	 * 更新会员等级
	 * 
	 * @param map 更新条件和内容
	 * @return 更新件数
	 * 
	 */
	public int updateMemberLevel(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRHAN01.updateMemberLevel");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 
	 * 更新会员等级(会员俱乐部)
	 * 
	 * @param map 更新条件和内容
	 * @return 更新件数
	 * 
	 */
	public int updateMemberClubLevel(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRHAN01.updateMemberClubLevel");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 
	 * 更新会员积分信息表
	 * 
	 * @param map 更新条件和内容
	 * @return 更新件数
	 * 
	 */
	public int updateMemberPoint(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRHAN01.updateMemberPoint");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 
	 * 更新会员积分最后变化时间
	 * 
	 * @param map 更新条件和内容
	 * @return 更新件数
	 * 
	 */
	public int updateMemPointLcTime(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRHAN01.updateMemPointLcTime");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 
	 * 通过会员等级ID取得会员等级代码
	 * 
	 * @param map 查询条件
	 * @return 会员等级代码
	 */
	public String getLevelCode(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRHAN01.getLevelCode");
		return (String)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 
	 * 查询首单时间
	 * 
	 * @param map 查询条件
	 * @return 首单时间
	 */
	public String getFirstTicketTime(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRHAN01.getHANFirstTicketTime");
		return (String)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 
	 * 查询首单信息
	 * 
	 * @param map 查询条件
	 * @return 首单时间
	 */
	public Map<String, Object> getFirstTickInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRHAN01.getFirstTickInfo");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	
	/**
	 * 
	 * 删除重算信息
	 * 
	 * @param list 删除条件
	 * 
	 */
	public void delReCalcInfo(List<Map<String, Object>> list) {
		baseServiceImpl.deleteAll(list, "BINBEDRHAN01.delReCalcInfo");
	}
	
	/**
	 * 
	 * 取得所有的规则内容
	 * 
	 * @param map 查询条件
	 * @return 所有的规则内容
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getAllRuleContent(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRHAN01.getAllRuleContent");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 
	 * 查询基准点的累计金额, 等级, 化妆次数
	 * 
	 * @param campBaseDTO 查询条件
	 * @return 基准点的累计金额, 等级, 化妆次数
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getReferenceRecord(CampBaseDTO campBaseDTO){
		return (Map) baseServiceImpl.get(campBaseDTO, "BINBEDRHAN01.getReferenceRecord");
	}
	
	/**
	 * 
	 * 通过会员等级ID取得会员等级级别
	 * 
	 * @param map 查询条件
	 * @return 会员等级级别
	 */
	public int getMemLevelGrade(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRHAN01.getMemLevelGrade");
		return (Integer)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 
	 * 查询指定会员重算时间点以后的某种履历区分的无效履历记录
	 * 
	 * @param map 查询条件
	 * @return 重算时间点以后的某种履历区分的无效履历记录
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getDelRecordList(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRHAN01.getDelRecordList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 
	 * 查询指定会员重算时间点以后的某种履历区分的无效履历记录
	 * 
	 * @param map 查询条件
	 * @return 重算时间点以后的某种履历区分的无效履历记录
	 */
	public List<Map<String, Object>> getDelPointChangeList(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRHAN01.getDelPointChangeList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 
	 * 删除指定会员重算时间点以后的履历记录
	 * 
	 * @param map 查询条件
	 * @return 删除件数
	 */
	public int deleteRuleExecRecord(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRHAN01.deleteRuleExecRecord");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 
	 * 删除指定会员重算时间点以后的积分变化记录
	 * 
	 * @param map 查询条件
	 * @return 删除件数
	 */
	public int deletePointChange(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRHAN01.deletePointChange");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 
	 * 取得重算时间点后清零记录
	 * 
	 * @param map 查询条件
	 * @return 重算时间点后清零记录
	 */
	public List<Map<String, Object>> getPrePCList(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRHAN01.getPrePCList");
		return (List<Map<String, Object>>) baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 
	 * 取得重算时间点后奖励积分记录数 
	 * 
	 * @param map 查询条件
	 * @return 重算时间点后奖励积分记录数 
	 */
	public List<Map<String, Object>> getPtRewardList(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRHAN01.getPtRewardList");
		return (List<Map<String, Object>>) baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 更新积分变化关联的奖励单号
	 * 
	 * @param campBaseDTO
	 * 			会员活动基础 DTO
	 */
	public int upReleUsedNo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRHAN01.upHANReleUsedNo");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 取得重算时间点后清零记录数
	 * 
	 * @param map
	 * 			查询参数
	 * @return int
	 * 			重算时间点后清零记录数
	 * 
	 */
	public int getPtClearCount(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRHAN01.getPtClearCount");
		return baseServiceImpl.getSum(paramMap);
	}
	
	/**
	 * 
	 * 取得会员重算时间点后第一条积分清零时间
	 * 
	 * @param map 查询条件
	 * @return 会员重算时间点后第一条积分清零时间
	 */
	public Map<String, Object> getMemFirstPCInfo(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRHAN01.getMemFirstPCInfo");
		return (Map<String, Object>) baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 
	 * 取得会员清零后的负积分之和
	 * 
	 * @param map 查询条件
	 * @return 会员清零后的负积分之和
	 */
	public Map<String, Object> getMemSubPoint(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRHAN01.getMemSubPointInfo");
		return (Map<String, Object>) baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 
	 * 取得所有会员信息List
	 * 
	 * @param map 查询条件
	 * @return 所有会员信息List
	 */
	public List<Map<String, Object>> getAllMemberList(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRHAN01.getAllMemberList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 查询会员等级信息List
	 * 
	 * @param map 查询条件
	 * @return 会员等级信息List
	 */	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getMemberLevelInfoList(Map<String, Object> map) throws Exception{
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEDRHAN01.getMemberLevelInfoList");
		List<Map<String, Object>> levelList = (List<Map<String, Object>>) baseServiceImpl.getList(paramMap);
		// 循环等级列表
		if (null != levelList) {
			for (Map<String, Object> levelInfo : levelList) {
				// 会员等级有效期
				String periodValidity = (String) levelInfo.get("periodValidity");
				if (!CherryChecker.isNullOrEmpty(periodValidity)) {
					Map<String, Object> periodValidityMap = (Map<String,Object>) 
					JSONUtil.deserialize(periodValidity);
					levelInfo.put("periodValidity", periodValidityMap);
				}
			}
		}
		return levelList;
	}
	
	/**
	 * 
	 * 查询会员规则履历是否正在迁移到MongoDB
	 * 
	 * @param map 查询条件
	 * @return 件数
	 */
	public int getMoveMemRuleRecordCount(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRHAN01.getMoveMemRuleRecordCount");
		return baseServiceImpl.getSum(paramMap);
	}
	
	/**
	 * 批量插入规则执行履历表
	 * 
	 * @param list 规则履历List
	 */
	public void addRuleExecRecord(List<Map<String, Object>> list){
		baseServiceImpl.saveAll(list, "BINBEDRHAN01.addRuleExecRecord");
	}
	
	/**
	 * 把规则履历历史表中的数据迁移到规则履历表中
	 * 
	 * @param map 迁移条件
	 */
	public void moveHistoryToCurrent(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRHAN01.moveHistoryToCurrent");
		baseServiceImpl.save(paramMap);
	}
	
	/**
	 * 删除会员规则履历历史
	 * 
	 * @param map 删除条件
	 * @return 删除件数
	 */
	public int delMemRuleRecordHistory(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRHAN01.delMemRuleRecordHistory");
		return baseServiceImpl.remove(paramMap);
	}
	
	/**
	 * 从规则履历历史表查询基准点的累计金额, 等级, 化妆次数
	 * 
	 * @param campBaseDTO 查询条件
	 * @return 基准点的累计金额, 等级, 化妆次数
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getReferenceRecordByHistory(CampBaseDTO campBaseDTO){
		return (Map) baseServiceImpl.get(campBaseDTO, "BINBEDRHAN01.getReferenceRecordByHistory");
	}
	
	/**
	 * 
	 * 查询会员初始金额信息
	 * 
	 * @param campBaseDTO 查询条件
	 * @return 会员初始金额信息
	 */
	public Map<String, Object> getMemExtInitInfo(CampBaseDTO campBaseDTO){
		return (Map) baseServiceImpl.get(campBaseDTO, "BINBEDRHAN01.getMemExtInitInfo");
	}
	
	/**
	 * 
	 * 查询默认等级履历
	 * 
	 * @param campBaseDTO 查询条件
	 * @return 默认等级履历
	 */
	public Map<String, Object> getMBLevelRecord(CampBaseDTO campBaseDTO){
		return (Map) baseServiceImpl.get(campBaseDTO, "BINBEDRHAN01.getMBLevelRecord");
	}
	
	/**
	 * 恢复默认等级履历
	 * 
	 * @param campBaseDTO
	 * 			会员活动基础 DTO
	 */
	public int updateMBLevel(CampBaseDTO campBaseDTO){
		return baseServiceImpl.update(campBaseDTO, "BINBEDRHAN01.updateMBLevel");
	}
	
	/**
	 * 
	 * 查询会员最早的开卡信息
	 * 
	 * @param campBaseDTO 查询条件
	 * @return 会员最早的开卡信息
	 */
	public Map<String, Object> getMemGrantInfo(CampBaseDTO campBaseDTO){
		return (Map) baseServiceImpl.get(campBaseDTO, "BINBEDRHAN01.getMemGrantInfo");
	}
	
	/**
	 * 
	 * 查询会员最早的开卡信息
	 * 
	 * @param campBaseDTO 查询条件
	 * @return 会员最早的开卡信息
	 */
	public Map<String, Object> getCardGrantInfo(CampBaseDTO campBaseDTO){
		return (Map) baseServiceImpl.get(campBaseDTO, "BINBEDRHAN01.getCardGrantInfo");
	}
	
	/**
	 * 
	 * 查询会员俱乐部最早的开卡信息
	 * 
	 * @param campBaseDTO 查询条件
	 * @return 会员最早的开卡信息
	 */
	public Map<String, Object> getClubCardGrantInfo(CampBaseDTO campBaseDTO){
		return (Map) baseServiceImpl.get(campBaseDTO, "BINBEDRHAN01.getClubCardGrantInfo");
	}
	
	/**
	 * 更新会员默认等级
	 * 
	 * @param campBaseDTO
	 * 			会员活动基础 DTO
	 */
	public int updateMemDeftLevel(CampBaseDTO campBaseDTO){
		return baseServiceImpl.update(campBaseDTO, "BINBEDRHAN01.updateMemDeftLevel");
	}
	
	/**
	 * 更新会员俱乐部默认等级
	 * 
	 * @param campBaseDTO
	 * 			会员活动基础 DTO
	 */
	public int updateMemClubDeftLevel(CampBaseDTO campBaseDTO){
		return baseServiceImpl.update(campBaseDTO, "BINBEDRHAN01.updateMemClubDeftLevel");
	}
	
	/**
	 * 
	 * 通过前置单号查询原单信息
	 * 
	 * @param map 查询参数
	 * @return 原单信息
	 * 
	 */
	public Map<String, Object> getPreSaleInfo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRHAN01.getPreSaleInfo");
		return (Map)baseServiceImpl.get(map);
	}
	
	/**
	 * 
	 * 取得重算时间点后撤销的清零记录
	 * 
	 * @param map 查询参数
	 * @return 撤销的清零记录
	 * 
	 */
	public List<Map<String, Object>> getDelPtClearList(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRHAN01.getBTPointClearList");
		return (List)baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 插入积分清零明细下发履历表
	 * 
	 * @param map
	 * 			积分清零明细
	 */
	public void addPointsClearRecord(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRHAN01.addBTPointsClearRecord");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 查询已记录的履历
	 * 
	 * @param map
	 * 			查询参数
	 * @return Map
	 * 			已记录的履历
	 * 
	 */
	public Map<String, Object> getClearRecordById(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRHAN01.getBTClearRecordById");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
	
	/**
	 * 
	 * 更新会员俱乐部等级信息（带排他）
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateMemberClubInfoExc(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRHAN01.updateHANMemberClubInfoExc");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 
	 * 插入会员俱乐部等级信息
	 * 
	 * @param map
	 */
	public void addMemClubLevelExc(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRHAN01.addHANMemClubLevelExc");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 取得会员天猫积分信息 
	 * @param map
	 * @return
	 * 		会员初始积分信息
	 */
	public Map<String, Object> getTMPointInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRHAN01.getHANTMPointInfo");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	
	/**
	 * 
	 * 更新会员天猫积分信息
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateTMPointInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRHAN01.updateHANTMPointInfo");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 
	 * 更新积分变化主表
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateTMUsedInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRHAN01.updateHANTMUsedInfo");
		return baseServiceImpl.update(map);
	}
}
