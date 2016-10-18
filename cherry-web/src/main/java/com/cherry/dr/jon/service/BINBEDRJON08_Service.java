/*	
 * @(#)BINBEDRJON08_Service.java     1.0 2013/03/26
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

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
import com.cherry.dr.cmbussiness.dto.core.CampBaseDTO;

/**
 * 建档处理执行 Service
 * 
 * @author hub
 * @version 1.0 2013.03.26
 */
public class BINBEDRJON08_Service extends BaseService{
	
	/**
	 * 取得会员信息
	 * 
	 * @param campBaseDTO
	 * 			会员实体
	 * @return Map
	 * 			会员信息
	 * 
	 */
	public Map<String, Object> getMemberInfo(CampBaseDTO campBaseDTO) {
		return (Map<String, Object>) baseServiceImpl.get(campBaseDTO, "BINBEDRJON08.getJNMemberInfo");
	}
	
	/**
	 * 更新会员信息表
	 * 
	 * @param Map
	 * 
	 * 
	 * @return int
	 * 
	 */
	public int updateMemberInfo(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRJON08.updateJNMemberInfo");
		return baseServiceImpl.update(map);
	}
	
	/**
	 * 验证手机号码唯一
	 * 
	 * @param Map
	 * 
	 * 
	 * @return boolean
	 * 			验证结果 true: 唯一   false:不唯一
	 * 
	 */
	public boolean checkUniquePhone(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINBEDRJON08.getPhoneCount");
		Integer count = baseServiceImpl.getSum(map);
		if (null == count || count == 0) {
			return true;
		}
		return false;
	}
}
