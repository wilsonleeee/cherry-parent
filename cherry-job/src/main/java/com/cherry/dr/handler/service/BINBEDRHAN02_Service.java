/*	
 * @(#)BINBEDRHAN02_Service.java     1.0 2011/09/13
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

/**
 * 清零处理Service
 * 
 * @author hub
 * @version 1.0 2011.09.13
 */
public class BINBEDRHAN02_Service extends BaseService{
	
	/**
	 * 取得会员信息List
	 * 
	 * @param map
	 * 			查询参数
	 * @return List
	 * 			会员卡信息List
	 * 
	 */
	public List<CampBaseDTO> getCampBaseDTOList(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID,
				"BINBEDRHAN02.getCampBaseDTOList");
		return (List<CampBaseDTO>) baseServiceImpl.getList(map);
	}
	
	/**
	 * 取得规则执行记录数
	 * 
	 * @param campBaseDTO
	 * 			会员活动基础 DTO
	 * @return int
	 * 			规则执行记录数
	 * 
	 */
	public int getRuleExecCountDTO(CampBaseDTO campBaseDTO) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 所属品牌ID
		paramMap.put("brandInfoId", campBaseDTO.getBrandInfoId());
		// 所属组织ID
		paramMap.put("organizationInfoId", campBaseDTO.getOrganizationInfoId());
		// 单据产生日期
		paramMap.put("ticketDate", campBaseDTO.getTicketDate());
		// 会员信息ID
		paramMap.put("memberInfoId", campBaseDTO.getMemberInfoId());
		paramMap.put(CherryConstants.IBATIS_SQL_ID,
				"BINBEDRHAN02.getRuleExecCountDTO");
		return baseServiceImpl.getSum(paramMap);
	}

}
