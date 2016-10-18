/*		
 * @(#)BINBEMQMES08_Service.java     1.0 2011/08/24		
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

package com.cherry.mq.mes.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 会员初始数据采集信息接收处理Service
 * 
 * @author WangCT
 *
 */
public class BINBEMQMES08_Service extends BaseService {
	
	/**
	 * 
	 * 查询会员信息
	 * 
	 * @param map
	 * @return 会员信息
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getMemberInfoID(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES08.getMemberInfoID");
		Map resultMap = (Map)baseServiceImpl.get(paramMap);
		if (null != resultMap && !resultMap.isEmpty() && 
				!CherryChecker.isNullOrEmpty(paramMap.get("memberClubId"))) {
			paramMap.put("memberInfoId", resultMap.get("memberInfoId"));
			paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES08.getComClubInitInfo");
			Map<String, Object> initMap = (Map<String, Object>)baseServiceImpl.get(paramMap);
			if (null != initMap && !initMap.isEmpty()) {
				resultMap.put("initialDate", initMap.get("initialDate"));
			}
		}
		return resultMap;
	}
	
	/**
	 * 
	 * 查询天猫会员信息
	 * 
	 * @param map
	 * @return 会员信息
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getTmallMember(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES08.getTmallMember");
		return (Map)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 
	 * 更新会员入会时间
	 * 
	 * @param map
	 * @return 更新件数
	 */
	public int updateJoinDate(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES08.updateJoinDate");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 
	 * 插入规则执行履历表
	 * 
	 * @param map
	 */
	public void addRuleExecRecord(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES08.addRuleExecRecord");
		baseServiceImpl.save(paramMap);
	}
	
	/**
	 * 
	 * 查询重算记录数
	 * 
	 * @param map
	 * @return 重算记录数
	 */
	public int getReCalcCount(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES08.getReCalcCount");
		return (Integer)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 
	 * 更新重算日期
	 * 
	 * @param map
	 * @return 更新件数
	 */
	public int updateReCalcDate(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES08.updateReCalcDate");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 
	 * 插入重算信息表
	 * 
	 * @param map
	 */
	public void addReCalcInfo(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES08.addReCalcInfo");
		baseServiceImpl.save(paramMap);
	}
	
	/**
	 * 
	 * 取得日期大于初始数据采集日期的规则执行记录数
	 * 
	 * @param map 查询条件
	 * @return 记录数
	 */
	public int getRuleExecCount(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES08.getRuleExecCount");
		return baseServiceImpl.getSum(paramMap);
	}
	
	/**
	 * 
	 * 更新会员等级
	 * 
	 * @param map
	 * @return 更新件数
	 */
	public int updateMemberLevel(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES08.updateMemberLevel");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 
	 * 判断MQ接收单据号是否已经存在规则执行记录里
	 * 
	 * @param map 查询条件
	 * @return 记录数
	 */
	public int getRuleExecCountCheck(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES08.getRuleExecCountCheck");
		return baseServiceImpl.getSum(paramMap);
	}
	
	/**
	 * 
	 * 查询会员最新规则执行履历记录
	 * 
	 * @param map 查询条件
	 * @return 会员最新规则执行履历记录
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getNewRuleExecRecord(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES08.getNewRuleExecRecord");
		return (Map)baseServiceImpl.get(paramMap);
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
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES08.getLevelCode");
		return (String)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 
	 * 通过会员等级代码取得会员等级ID
	 * 
	 * @param map 查询条件
	 * @return 会员等级ID
	 */
	public String getMemberLevelID(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES08.getMemberLevelID");
		return (String)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 
	 * 插入会员使用化妆次数积分主表
	 * 
	 * @param map
	 */
	public int addMemUsedInfo(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES08.addMemUsedInfo");
		return baseServiceImpl.saveBackId(paramMap);
	}
	
	/**
	 * 
	 * 插入会员使用化妆次数积分明细表
	 * 
	 * @param map
	 */
	public void addMemUsedDetail(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES08.addMemUsedDetail");
		baseServiceImpl.save(paramMap);
	}
	
	/**
	 * 
	 * 通过会员等级ID取得会员等级级别
	 * 
	 * @param map
	 */
	public Integer getMemLevelGrade(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES08.getMemLevelGrade");
		return (Integer)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 
	 * 插入会员信息扩展表
	 * 
	 * @param map
	 */
	public void addMemberExtInfo(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES08.addMemberExtInfo");
		baseServiceImpl.save(paramMap);
	}
	
	/**
	 * 
	 * 更新会员信息扩展表
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateMemberExtInfo(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES08.updateMemberExtInfo");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 
	 * 更新会员信息（带排他）
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateMemberInfoExc(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES08.updateMemberInfoExc");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 
	 * 更新会员俱乐部等级信息（带排他）
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateMemberClubInfoExc(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES08.updateMemberClubInfoExc");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 
	 * 插入会员俱乐部等级信息
	 * 
	 * @param map
	 */
	public void addMemClubLevelExc(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES08.addMemClubLevelExc");
		baseServiceImpl.save(paramMap);
	}
	
	/**
	 * 
	 * 更新会员信息扩展表（带排他）
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateMemberExtInfoExc(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES08.updateMemberExtInfoExc");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 
	 * 更新状态维护明细信息
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateMemUsedDetailInfo(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES08.updateMemUsedDetailInfo");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 
	 * 更新会员初始积分主记录
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateMemberMTInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES08.updateMemberMTInfo");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 
	 * 更新会员初始积分明细记录
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateMemberMTDtlInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES08.updateMemberMTDtlInfo");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 
	 * 删除状态维护信息
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int delMemUsedInfo(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES08.delMemUsedInfo");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 
	 * 恢复状态维护信息
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int validMemUsedInfo(Map<String, Object> map){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES08.validMemUsedInfo");
		return baseServiceImpl.update(paramMap);
	}
	
	/**
	 * 查询预约单信息
	 * @param map
	 * @return
	 * 		预约单信息
	 */
	public List<Map<String, Object>> getPBTicketList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES08.getPBTicketList");
		return (List<Map<String, Object>>)baseServiceImpl.getList(map);
	}
	
	/**
	 * 查询状态维护信息
	 * @param map
	 * @return
	 * 		状态维护信息
	 */
	public Map<String, Object> getPTUsedInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES08.getPTUsedInfo");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	
	/**
	 * 查询会员初始积分信息
	 * @param map
	 * @return
	 * 		会员初始积分信息
	 */
	public Map<String, Object> getMTUsedInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES08.getMTUsedInfo");
		return (Map<String, Object>)baseServiceImpl.get(map);
	}
	
	/**
	 * 
	 * 查询撤销的单据件数
	 * 
	 * @param map
	 * @return 维护记录数
	 */
	public int getPTCXCount(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES08.getPTCXCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 
	 * 取得会员某时间的维护记录数
	 * 
	 * @param map
	 * @return 维护记录数
	 */
	public int getUsedCount(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES08.getUsedCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 查询会员俱乐部扩展信息
	 * @param map
	 * @return
	 * 		会员俱乐部扩展信息
	 */
	public List<Map<String, Object>> getClubExtList(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES08.getClubExtList");
		return (List<Map<String, Object>>)baseServiceImpl.getList(map);
	}
	
	/**
	 * 
	 * 更新会员俱乐部扩展信息
	 * 
	 * @param map 更新条件
	 */
	public void updateClubExtList(List<Map<String, Object>> list){
		baseServiceImpl.updateAll(list, "BINBEMQMES08.updateClubExtInfo");
	}
	
	/**
	 * 
	 * 插入会员俱乐部扩展信息
	 * 
	 * @param map 更新条件
	 */
	public void addClubExtList(List<Map<String, Object>> list){
		baseServiceImpl.saveAll(list, "BINBEMQMES08.addClubExtInfo");
	}
	
	/**
	 * 取得会员天猫积分信息 
	 * @param map
	 * @return
	 * 		会员初始积分信息
	 */
	public Map<String, Object> getTMPointInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES08.getTMPointInfo");
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
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES08.updateTMPointInfo");
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
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMQMES08.updateTMUsedInfo");
		return baseServiceImpl.update(map);
	}
}
