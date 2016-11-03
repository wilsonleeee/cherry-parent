/*	
 * @(#)BINBEDRHAN04_Service.java     1.0 2013/05/13
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

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.dr.cmbussiness.dto.core.CampBaseDTO;
import com.cherry.dr.cmbussiness.dto.core.PointChangeDTO;
import com.cherry.dr.cmbussiness.dto.core.PointChangeDetailDTO;
import com.cherry.dr.cmbussiness.dto.core.PointDTO;

/**
 * 积分清零处理Service
 * 
 * @author hub
 * @version 1.0 2013.05.13
 */
public class BINBEDRHAN04_Service extends BaseService{
	
	/**
	 * 取得需要积分清零的会员信息List
	 * 
	 * @param map
	 * 			查询参数
	 * @return List
	 * 			需要积分清零的会员信息List
	 * 
	 */
	public List<Map<String, Object>> getMemClearList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINBEDRHAN04.getMemClearList");
		return (List<Map<String, Object>>) baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得需要积分清零的会员信息List
	 * 
	 * @param map
	 * 			查询参数
	 * @return List
	 * 			需要积分清零的会员信息List
	 * 
	 */
	public List<Map<String, Object>> getClubMemClearList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINBEDRHAN04.getClubMemClearList");
		return (List<Map<String, Object>>) baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得单个清零的会员信息
	 * 
	 * @param map
	 * 			查询参数
	 * @return List
	 * 			单个清零的会员信息
	 * 
	 */
	public Map<String, Object> getMemClearInfo(CampBaseDTO campBaseDTO) {
		return (Map<String, Object>) baseServiceImpl.get(campBaseDTO, "BINBEDRHAN04.getMemClearInfo");
	}
	
	/**
	 * 
	 * 更新会员BATCH执行状态
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateMemBatchExec(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRHAN04.updateMemBatchExec");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 
	 * 更新最近有积分产生的会员
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updatePointBatchExec(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRHAN04.updatePointBatchExec");
		return baseServiceImpl.update(map);
	}
	/**
	 * 
	 * 去除会员BATCH执行状态
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateClearBatchExec(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRHAN04.updateClearBatchExec");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 
	 * 更新会员BATCH执行状态
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateClubMemBatchExec(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRHAN04.updateClubMemBatchExec");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 
	 * 更新最近有积分产生的会员
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateClubPointBatchExec(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRHAN04.updateClubPointBatchExec");
		return baseServiceImpl.update(map);
	}
	/**
	 * 
	 * 去除会员BATCH执行状态
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateClubClearBatchExec(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRHAN04.updateClearBatchExec");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 更新会员积分变化信息
	 * @param pointChange
	 * 			会员积分变化 DTO
	 * @throws Exception 
	 * 
	 */
	public void updatePointChangeInfo(PointChangeDTO pointChange) throws Exception{
		// 取得会员积分变化的最大重算次数
		Map<String, Object> pointReCalcInfo = getPointReCalcInfo(pointChange);
		if (null == pointReCalcInfo || pointReCalcInfo.isEmpty()) {
			// 插入会员积分变化主表
			addPointChange(pointChange);
		} else {
			// 会员积分变化ID
			int pointChangeId = Integer.parseInt(pointReCalcInfo.get("pointChangeId").toString());
			// 重算次数
			int reCalcCount = Integer.parseInt(pointReCalcInfo.get("reCalcCount").toString());
			pointChange.setPointChangeId(pointChangeId);
			// 更新会员积分变化主表
			updatePointChange(pointChange);
			pointChange.setReCalcCount(reCalcCount);
			Map<String, Object> delMap = new HashMap<String, Object>();
			delMap.put("pointChangeId", pointChangeId);
			// 删除会员积分变化明细表
			delPointChangeDetail(delMap);
		}
		List<PointChangeDetailDTO> changeDetailList = pointChange.getChangeDetailList();
		if (null != changeDetailList) {
			for (PointChangeDetailDTO changeDetailDTO : changeDetailList) {
				// 会员积分变化主ID
				changeDetailDTO.setPointChangeId(pointChange.getPointChangeId());
				// 插入会员积分变化明细表
				addPointChangeDetail(changeDetailDTO);
			}
		}
	}
	
	/**
	 * 取得会员积分变化的最大重算次数
	 * 
	 * @param campBaseDTO
	 * 			会员活动基础 DTO
	 * @return Map
	 * 			最大重算次数
	 * 
	 */
	public Map<String, Object> getPointReCalcInfo(PointChangeDTO pointChangeDTO) {
		return (Map<String, Object>) baseServiceImpl.get(pointChangeDTO, "BINBEDRHAN04.getBTPointReCalcInfo");
	}
	
	/**
	 * 插入会员积分变化主表
	 * 
	 * @param pointChangeDTO
	 * 			会员积分变化主 DTO
	 */
	public int addPointChange(PointChangeDTO pointChangeDTO){
		return baseServiceImpl.saveBackId(pointChangeDTO, "BINBEDRHAN04.addBTPointChange");
	}
	
	/**
	 * 插入会员积分变化明细表
	 * 
	 * @param pointChangeDTO
	 * 			会员积分变化明细DTO
	 */
	public void addPointChangeDetail(PointChangeDetailDTO pointChangeDetailDTO){
		baseServiceImpl.save(pointChangeDetailDTO, "BINBEDRHAN04.addBTPointChangeDetail");
	}
	
	/**
	 * 更新会员积分变化主表
	 * 
	 * @param pointChangeDTO
	 * 			会员积分变化主 DTO
	 */
	public int updatePointChange(PointChangeDTO pointChangeDTO){
		return baseServiceImpl.update(pointChangeDTO, "BINBEDRHAN04.updateBTPointChange");
	}
	
	/**
	 * 取得规则内容
	 * 
	 * @param pointChangeDTO
	 * 			会员积分变化主 DTO
	 */
	public String getRuleContent(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINBEDRHAN04.getRuleContent");
		return (String) baseServiceImpl.get(map);
	}
	
	/**
	 * 
	 * 删除会员积分变化明细表
	 * 
	 * @param pointChangeDTO
	 * 			会员积分变化主 DTO
	 * 
	 */
	public int delPointChangeDetail(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRHAN04.delBTPointChangeDetail");
		return baseServiceImpl.remove(map);
	}
	
	/**
	 * 更新会员积分表
	 * 
	 * @param PointDTO
	 * 			会员积分变化主 DTO
	 */
	public int updateMemberPoint(PointDTO pointDTO){
		return baseServiceImpl.update(pointDTO, "BINBEDRHAN04.updateBTMemberPoint");
	}
	
	/**
	 * 
	 * 更新积分变化表清零标识
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updatePointClearFlag(CampBaseDTO campBaseDTO){
		return baseServiceImpl.update(campBaseDTO, "BINBEDRHAN04.updatePointClearFlag");
	}
	
	/**
	 * 
	 * 更新积分清零日期
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updatePointDisPointTime(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRHAN04.updatePointDisPointTime");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 插入积分清零明细下发履历表
	 * 
	 * @param map
	 * 			积分清零明细
	 */
	public void addPointsClearRecord(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRHAN04.addPointsClearRecord");
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
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRHAN04.getClearRecordById");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}
}
