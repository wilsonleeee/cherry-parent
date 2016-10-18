/*	
 * @(#)BINBEDRJON01_Service.java     1.0 2011/05/17
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
package com.cherry.dr.jon.service;

import com.cherry.cm.service.BaseService;
import com.cherry.dr.cmbussiness.dto.core.CampBaseDTO;

/**
 * 会员入会Service
 * 
 * @author hub
 * @version 1.0 2011.05.17
 */
public class BINBEDRJON01_Service extends BaseService{
	
	/**
	 * 更新会员信息表
	 * 
	 * @param Map
	 * 
	 * 
	 * @return int
	 * 
	 */
	public int updateMemberInfo(CampBaseDTO campBaseDTO) {
		return baseServiceImpl.update(campBaseDTO, "BINBEDRJON01.updateMemberInfo");
	}
	
	/**
	 * 更新会员信息表(会员俱乐部)
	 * 
	 * @param Map
	 * 
	 * 
	 * @return int
	 * 
	 */
	public int updateMemberInfoClub(CampBaseDTO campBaseDTO) {
		return baseServiceImpl.update(campBaseDTO, "BINBEDRJON01.updateMemberInfoClub");
	}
	
	
	/**
	 * 插入会员等级信息表(俱乐部)
	 * 
	 * @param Map
	 * 
	 * 
	 * @return int
	 * 
	 */
	public void addMemClubLevel(CampBaseDTO campBaseDTO) {
		baseServiceImpl.save(campBaseDTO, "BINBEDRJON01.addMemClubLevel");
	}
	
	/**
	 * 更新会员等级信息表(俱乐部)
	 * 
	 * @param Map
	 * 
	 * 
	 * @return int
	 * 
	 */
	public int updateMemClubLevel(CampBaseDTO campBaseDTO) {
		return baseServiceImpl.update(campBaseDTO, "BINBEDRJON01.updateMemClubLevel");
	}

}
