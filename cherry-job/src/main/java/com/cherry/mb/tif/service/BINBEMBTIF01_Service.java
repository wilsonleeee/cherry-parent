/*	
 * @(#)BINBEMBTIF01_Service.java     1.0 2015/06/24
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
package com.cherry.mb.tif.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchConstants;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.TmallKeys;
import com.cherry.cm.service.BaseService;

/**
 * 同步天猫会员处理Service
 * 
 * @author hub
 * @version 1.0 2015/06/24
 */
public class BINBEMBTIF01_Service extends BaseService{
	
	/**
	 * 取得需要同步的会员信息List
	 * 
	 * @param map
	 * 			查询参数
	 * @return List
	 * 			需要同步的会员信息List
	 * 
	 */
	public List<Map<String, Object>> getMemSyncList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINBEMBTIF01.getMemSyncList");
		return (List<Map<String, Object>>) baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得需要手机加密的会员信息List
	 * 
	 * @param map
	 * 			查询参数
	 * @return List
	 * 			需要手机加密的会员信息List
	 * 
	 */
	public List<Map<String, Object>> getMemMixPhoneList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINBEMBTIF01.getMemMixPhoneList");
		return (List<Map<String, Object>>) baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得添加天猫加密手机号的会员信息List
	 * 
	 * @param map
	 * 			查询参数
	 * @return List
	 * 			需要手机加密的会员信息List
	 * 
	 */
	public List<Map<String, Object>> getAddMixMobileMemList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINBEMBTIF01.getAddMixMobileMemList");
		return (List<Map<String, Object>>) baseServiceImpl.getList(map);
	}

	/**
	 * 取得需要手机加密的会员信息List
	 *
	 * @param map
	 * 			查询参数
	 * @return List
	 * 			需要手机加密的会员信息List
	 *
	 */
	public List<Map<String, Object>> getMemPhoneList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINBEMBTIF01.getMemPhoneList");
		return (List<Map<String, Object>>) baseServiceImpl.getList(map);
	}

	/**
	 * 取得要更新明文手机号的会员信息List
	 *
	 * @param map
	 * 			查询参数
	 * @return List
	 * 			要更新明文手机号的会员信息List
	 *
	 */
	public List<Map<String, Object>> getUpdatedMemList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINBEMBTIF01.getUpdatedMemList");
		return (List<Map<String, Object>>) baseServiceImpl.getList(map);
	}

	/**
	 * 取得要转成正式会员的线上会员信息List
	 *
	 * @param map
	 * 			查询参数
	 * @return List
	 * 			要转成正式会员的线上会员信息List
	 *
	 */
	public List<Map<String, Object>> getRegMemList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINBEMBTIF01.getRegMemList");
		return (List<Map<String, Object>>) baseServiceImpl.getList(map);
	}

	/**
	 * 取得要合并的会员信息List
	 *
	 * @param map
	 * 			查询参数
	 * @return List
	 * 			要合并的会员信息List
	 *
	 */
	public List<Map<String, Object>> getMergedMemberList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINBEMBTIF01.getMergedMemberList");
		return (List<Map<String, Object>>) baseServiceImpl.getList(map);
	}

	/**
	 * 取得历史注册的新会员信息List
	 * 
	 * @param map
	 * 			查询参数
	 * @return List
	 * 			需要同步的会员信息List
	 * 
	 */
	public List<Map<String, Object>> getHisMemRegisterList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINBEMBTIF01.getHisMemRegisterList");
		return (List<Map<String, Object>>) baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得需要绑定的新会员信息List
	 * 
	 * @param map
	 * 			查询参数
	 * @return List
	 * 			需要绑定的新会员信息List
	 * 
	 */
	public List<Map<String, Object>> getMemRegisterList(Map<String, Object> map) {
		if (!map.containsKey("tmallCounterArr")) {
			String tmallCounters = TmallKeys.getTmallCounters((String) map.get("brandCode"));
			if (!CherryChecker.isNullOrEmpty(tmallCounters)) {
				map.put("tmallCounterArr", tmallCounters.split(","));
			}
		}
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINBEMBTIF01.getMemRegisterList");
		return (List<Map<String, Object>>) baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得需要合并的会员信息List
	 * 
	 * @param map
	 * 			查询参数
	 * @return List
	 * 			需要绑定的新会员信息List
	 * 
	 */
	public List<Map<String, Object>> getMemMergeList(Map<String, Object> map) {
		if (!map.containsKey("tmallCounterArr")) {
			String tmallCounters = TmallKeys.getTmallCounters((String) map.get("brandCode"));
			if (!CherryChecker.isNullOrEmpty(tmallCounters)) {
				map.put("tmallCounterArr", tmallCounters.split(","));
			}
		}
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINBEMBTIF01.getMemMergeList");
		return (List<Map<String, Object>>) baseServiceImpl.getList(map);
	}
	
	/**
	 *  取得需要处理天猫积分的会员List
	 * 
	 * @param map
	 * 			查询参数
	 * @return List
	 * 			需要处理天猫积分的会员List
	 * 
	 */
	public List<Map<String, Object>> getMemPointList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINBEMBTIF01.getMemPointList");
		return (List<Map<String, Object>>) baseServiceImpl.getList(map);
	}
	
	/**
	 * 
	 * 去除会员BATCH执行状态
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateClearBatchExec(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBTIF01.updateClearBatchExec");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 
	 * 去除会员BATCH执行状态(新会员注册表)
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateClearRegisterExec(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBTIF01.updateClearRegisterExec");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 
	 * 去除会员BATCH执行状态(天猫会员合并表)
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateClearMergeExec(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBTIF01.updateClearMergeExec");
		return baseServiceImpl.update(map);
	}
	
	
	/**
	 * 
	 * 更新会员BATCH执行状态(手机号码加密)
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateMixMobileBatchExec(Map<String, Object> map){
		if (!map.containsKey("tmallCounterArr")) {
			String tmallCounters = TmallKeys.getTmallCounters((String) map.get("brandCode"));
			if (!CherryChecker.isNullOrEmpty(tmallCounters)) {
				map.put("tmallCounterArr", tmallCounters.split(","));
			}
		}
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBTIF01.updateMixMobileBatchExec");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 
	 * 更新会员BATCH执行状态(新会员注册)
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateRegisterBatchExec(Map<String, Object> map){
		if (!map.containsKey("tmallCounterArr")) {
			String tmallCounters = TmallKeys.getTmallCounters((String) map.get("brandCode"));
			if (!CherryChecker.isNullOrEmpty(tmallCounters)) {
				map.put("tmallCounterArr", tmallCounters.split(","));
			}
		}
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBTIF01.updateRegisterBatchExec");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 
	 * 更新会员BATCH执行状态(天猫会员合并)
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateMergeBatchExec(Map<String, Object> map){
		if (!map.containsKey("tmallCounterArr")) {
			String tmallCounters = TmallKeys.getTmallCounters((String) map.get("brandCode"));
			if (!CherryChecker.isNullOrEmpty(tmallCounters)) {
				map.put("tmallCounterArr", tmallCounters.split(","));
			}
		}
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBTIF01.updateMergeBatchExec");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 
	 * 更新会员BATCH执行状态(积分兑换)
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updatePointBatchExec(Map<String, Object> map){
		if (!map.containsKey("tmallCounterArr")) {
			String tmallCounters = TmallKeys.getTmallCounters((String) map.get("brandCode"));
			if (!CherryChecker.isNullOrEmpty(tmallCounters)) {
				map.put("tmallCounterArr", tmallCounters.split(","));
			}
		}
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBTIF01.updatePointBatchExec");
		return baseServiceImpl.update(map);
	}
	
	
	/**
	 * 
	 * 更新会员BATCH执行状态(历史注册会员)
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateHisRegBatchExec(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBTIF01.updateHisRegBatchExec");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 
	 * 更新会员加密手机号
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateMemMixMobile(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBTIF01.updateMemMixMobile");
		return baseServiceImpl.update(map);
	}

	/**
	 *
	 * 更新会员明文手机号
	 *
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateExpressMobile(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBTIF01.updateExpressMobile");
		return baseServiceImpl.update(map);
	}

	/**
	 * 
	 * 更新新会员注册表
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateMemRegister(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBTIF01.updateMemRegister");
		return baseServiceImpl.update(map);
	}

	/**
	 *
	 * 更新新会员注册表
	 *
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateMemRegisterInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBTIF01.updateMemRegisterInfo");
		return baseServiceImpl.update(map);
	}

	/**
	 * 
	 * 更新注册会员绑定时间
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateRegMemBindTime(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBTIF01.updateRegMemBindTime");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 
	 * 更新天猫积分兑换履历表
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateTmallPointMemId(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBTIF01.updateTmallPointMemId");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 
	 * 更新天猫积分兑换同步结果
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateTmallPointPTFlag(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBTIF01.updateTmallPointPTFlag");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 获取假登陆的会员数 
	 * 
	 * @param map 查询条件
	 * 
	 */
	public int getTempMemCount(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEMBTIF01.getTempMemCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 取得天猫加密秘钥
	 * 
	 * @param
	 *
	 * @return String
	 * 
	 */
	public String getTmMixKey(int brandInfoId) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBTIF01.getTmMixKey");
		paramMap.put(CherryConstants.BRANDINFOID, brandInfoId);
		return (String)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 获取加密手机号对应的会员数 
	 * 
	 * @param map 查询条件
	 * 
	 */
	public int getMixMemCount(Map<String, Object> map) {
		map.put(CherryBatchConstants.IBATIS_SQL_ID, "BINBEMBTIF01.getMixMemCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 插入天猫会员合并表
	 * 
	 * @param map
	 * 			会员合并信息
	 */
	public void addTmallMemMergeInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBTIF01.addTmallMemMergeInfo");
		baseServiceImpl.save(map);
	}

	/**
	 * 插入会员信息合并记录表
	 *
	 * @param map
	 * 			会员合并信息
	 */
	public void addMemberMergeInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBTIF01.addMemberMergeInfo");
		baseServiceImpl.save(map);
	}

	/**
	 * 插入会员信息合并历史表
	 *
	 * @param map
	 * 			会员合并信息
	 */
	public void addMemberMergeHistory(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBTIF01.addMemberMergeHistory");
		baseServiceImpl.save(map);
	}

	/**
	 * 
	 * 更新天猫会员合并表
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateTmallMemMergeInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBTIF01.updateTmallMemMergeInfo");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 取得会员信息
	 * 
	 * @param map
	 * 			查询参数
	 * @return List
	 * 			需要同步的会员信息List
	 * 
	 */
	public Map<String, Object> getNewMemberInfo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINBEMBTIF01.getNewMemberInfo");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}

	/**
	 * 取得会员信息
	 *
	 * @param map
	 * 			查询参数
	 * @return List
	 * 			会员信息
	 *
	 */
	public Map<String, Object> getMemberInfoByIdAndMemCode(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINBEMBTIF01.getMemberInfoByIdAndMemCode");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}

	/**
	 * 取得假登陆会员积分信息
	 * 
	 * @param map
	 * 			查询参数
	 * @return List
	 * 			需要同步的会员信息List
	 * 
	 */
	public Map<String, Object> getPrePointInfo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINBEMBTIF01.getPrePointInfo");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}

	/**
	 * 根据卡号获得持卡信息
	 *
	 * @param map
	 * 			查询参数
	 * @return 持卡信息
	 *
	 *
	 */
	public Map<String, Object> getMemCardInfo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINBEMBTIF01.getMemCardInfo");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}

	/**
	 * 获取组织ID
	 *
	 * @param map
	 * 			查询参数
	 * @return 持卡信息
	 *
	 *
	 */
	public Map<String, Object> getOrganizationId(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINBEMBTIF01.getOrganizationId");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
	/**
	 * 获取会员ID
	 *
	 * @param map
	 * 			查询参数
	 * @return 会员ID
	 *
	 *
	 */
	public Map<String, Object> getEmpId(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINBEMBTIF01.getEmpId");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
	/**
	 * 根据会员ID和卡号获得合并信息
	 *
	 * @param map
	 * 			查询参数
	 * @return 持卡信息
	 *
	 *
	 */
	public Map<String, Object> getMemMergeInfoByIdAndMemCode(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINBEMBTIF01.getMemMergeInfoByIdAndMemCode");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}

	/**
	 * 查询会员的合并历史记录
	 *
	 * @param map
	 * 			查询参数
	 * @return 持卡信息
	 *
	 *
	 */
	public Map<String, Object> getMemMergeHistory(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINBEMBTIF01.getMemMergeHistory");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}

	/**
	 * 获取会员最早的销售时间
	 *
	 * @param map
	 * 			查询参数
	 * @return 持卡信息
	 *
	 *
	 */
	public Map<String, Object> getEarliestSaleTime(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINBEMBTIF01.getEarliestSaleTime");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}

	/**
	 * 根据天猫加密手机号索取会员信息
	 *
	 * @param map
	 * 			查询参数
	 * @return 持卡信息
	 *
	 *
	 */
	public Map<String, Object> getMemberInfoByMixMobile(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINBEMBTIF01.getMemberInfoByMixMobile");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}

	/**
	 * 删除假登陆会员信息
	 * @param map
	 */
	public int delPreMemberInfo(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBTIF01.delPreMemberInfo");
		return baseServiceImpl.remove(map);
	}

	/**
	 * 删除会员信息
	 * @param map
	 */
	public int deleteMemberInfo(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBTIF01.deleteMemberInfo");
		return baseServiceImpl.remove(map);
	}

	/**
	 * 删除会员持卡信息
	 * @param map
	 */
	public int deleteMemCardInfo(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBTIF01.deleteMemCardInfo");
		return baseServiceImpl.remove(map);
	}

	/**
	 * 删除会员合并信息
	 * @param map
	 */
	public int deleteMemberMergeInfo(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBTIF01.deleteMemberMergeInfo");
		return baseServiceImpl.remove(map);
	}

	/**
	 * 删除假登陆会员卡信息
	 * @param map
	 */
	public int delPreMemCode(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBTIF01.delPreMemCode");
		return baseServiceImpl.remove(map);
	}
	
	/**
	 * 删除假登陆会员积分信息 
	 * @param map
	 */
	public int delPreMemPoint(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBTIF01.delPreMemPoint");
		return baseServiceImpl.remove(map);
	}
	
	/**
	 * 
	 * 更新新会员信息
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateNewMemInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBTIF01.updateNewMemInfo");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 
	 * 变更积分表中假登陆会员的ID
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updatePointMemId(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBTIF01.updatePointMemId");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 
	 * 更新新会员的积分值
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateNewMemPoint(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBTIF01.updateNewMemPoint");
		return baseServiceImpl.update(map);
	}
	/**
	 * 
	 * 变更会员使用化妆次数积分明细记录的假登陆会员的ID
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateTmMemUsedMemId(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBTIF01.updateTmMemUsedMemId");
		return baseServiceImpl.update(map);
	}
	/**
	 * 
	 * 变更规则执行履历记录的假登陆会员的ID
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateTmRuleRecordMemId(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBTIF01.updateTmRuleRecordMemId");
		return baseServiceImpl.update(map);
	}
	/**
	 * 
	 * 变更积分变化记录的假登陆会员的ID
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateTmPointChangeMemId(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBTIF01.updateTmPointChangeMemId");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 
	 * 变更新会员注册表的假登陆会员的ID
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updatePreMemRegister(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBTIF01.updatePreMemRegister");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 
	 * 变更天猫积分兑换履历表 的假登陆会员的ID
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updatePreTmallPointMemId(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBTIF01.updatePreTmallPointMemId");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 
	 * 更新天猫会员合并表执行结果
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateMemMergeResult(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBTIF01.updateMemMergeResult");
		return baseServiceImpl.update(map);
	}

	/**
	 *
	 * 更新会员信息表
	 *
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateMemberInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBTIF01.updateMemberInfo");
		return baseServiceImpl.update(map);
	}
	/**
	 *
	 * 更新会员持卡表
	 *
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateMemCardInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBTIF01.updateMemCardInfo");
		return baseServiceImpl.update(map);
	}
	/**
	 *
	 * 更新电商订单主表
	 *
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateESOrderMain(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBTIF01.updateESOrderMain");
		return baseServiceImpl.update(map);
	}
	/**
	 *
	 * 更新销售主表
	 *
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateSaleMaster(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBTIF01.updateSaleMaster");
		return baseServiceImpl.update(map);
	}
	/**
	 *
	 * 更新会员使用明细表
	 *
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateMemUsedDetail(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBTIF01.updateMemUsedDetail");
		return baseServiceImpl.update(map);
	}

	/**
	 *
	 * 更新会员积分表
	 *
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateMemberPoint(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBTIF01.updateMemberPoint");
		return baseServiceImpl.update(map);
	}

	/**
	 *
	 * 更新会员积分变化主表
	 *
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updatePointChange(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBTIF01.updatePointChange");
		return baseServiceImpl.update(map);
	}

	/**
	 * 查询会员产生的最早业务时间
	 * @param map
	 * @return
	 */
	public String getMinBillDate(Map map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBTIF01.getMinBillDate");
		return  (String)baseServiceImpl.get(map);
	}
	
	/**
	 * 插入会员信息表
	 * @param map
	 * @return
	 */
	public int addMemberInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBTIF01.addBETmallMemInfo");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 
	 * 插入会员持卡信息表
	 * 
	 * @param map
	 */
	public void addMemCardInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBTIF01.addBETmallMemCardInfo");
		baseServiceImpl.save(map);
	}
	
	/**
	 * 临时会员绑定处理
	 * 
	 * @param map
	 * @return int
	 */
	public int updateTempMemRegInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBTIF01.updateBETempMemRegInfo");
		return baseServiceImpl.update(map);
		 
	}
	
	/**
	 * 取得回调天猫积分信息 List
	 * 
	 * @param map
	 * 			查询参数
	 * @return List
	 * 			需要手机加密的会员信息List
	 * 
	 */
	public List<Map<String, Object>> getRecallPointList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINBEMBTIF01.getRecallPointList");
		return (List<Map<String, Object>>) baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得回调失败的天猫积分信息
	 * 
	 * @param map
	 * 			查询参数
	 * @return List
	 * 			回调失败的天猫积分信息
	 * 
	 */
	public List<Map<String, Object>> getFailPointList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINBEMBTIF01.getFailPointList");
		return (List<Map<String, Object>>) baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得回调天猫积分信息总数
	 * 
	 * @param map
	 * @return
	 */
	public int getRecallPointCount(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBTIF01.getRecallPointCount");
		return baseServiceImpl.getSum(map);
	}
	
	/**
	 * 
	 * 更新积分变化主表
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateTMUsedInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBTIF01.updateTIFTMUsedInfo");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 
	 * 更新天猫积分表
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateFailPointInfo(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEMBTIF01.updateFailPointInfo");
		return baseServiceImpl.update(map);
	}
}
