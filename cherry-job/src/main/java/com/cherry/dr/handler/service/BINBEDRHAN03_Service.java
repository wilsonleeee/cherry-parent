/*	
 * @(#)BINBEDRHAN03_BL.java     1.0 2012/04/23
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

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.dr.cmbussiness.dto.core.CampBaseDTO;

/**
 * 降级处理Service
 * 
 * @author hub
 * @version 1.0 2012.04.23
 */
public class BINBEDRHAN03_Service extends BaseService{
	
	/**
	 * 取得需要降级处理的会员信息List
	 * 
	 * @param map
	 * 			查询参数
	 * @return List
	 * 			需要降级处理的会员信息List
	 * 
	 */
	public List<CampBaseDTO> getLevelDownList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINBEDRHAN03.getLevelDownList");
		return (List<CampBaseDTO>) baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得需要降级处理的会员信息List
	 * 
	 * @param map
	 * 			查询参数
	 * @return List
	 * 			需要降级处理的会员信息List
	 * 
	 */
	public List<CampBaseDTO> getClubLevelDownList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINBEDRHAN03.getClubLevelDownList");
		return (List<CampBaseDTO>) baseServiceImpl.getList(map);
	}
	
	/**
	 * 
	 * 更新会员BATCH执行状态
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateMemBatchExec(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRHAN03.updateMemDGBatchExec");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 
	 * 更新会员BATCH执行状态
	 * 
	 * @param map 更新条件
	 * @return 更新件数
	 */
	public int updateClubMemDGBatchExec(Map<String, Object> map){
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRHAN03.updateClubMemDGBatchExec");
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
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRHAN03.updateClearDGBatchExec");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 更新会员等级有效期
	 * 
	 * @param campBaseDTO
	 * 			会员活动基础 DTO
	 */
	public int updateLevelValidity(CampBaseDTO campBaseDTO){
		return baseServiceImpl.update(campBaseDTO, "BINBEDRHAN03.updateLevelValidity");
	}
	
	/**
	 * 更新会员等级有效期
	 * 
	 * @param campBaseDTO
	 * 			会员活动基础 DTO
	 */
	public int updateClubLevelValidity(CampBaseDTO campBaseDTO){
		return baseServiceImpl.update(campBaseDTO, "BINBEDRHAN03.updateClubLevelValidity");
	}
	
}
