/*  
 * @(#)MemberPointInfoService.java     1.0 2014/08/01      
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

package com.cherry.webservice.member.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 会员下一等级差额Service
 * 
 * @author GeHequn
 * @version 1.0 2016.10.20
 */
public class MemberNextAmountService extends BaseService {
	
	@SuppressWarnings("unchecked")
	public Map<String,Object> getMemberInfoMap(Map<String,Object> paramMap){
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "MemberNextAmount.getMemberInfoMap");
		return (Map<String, Object>) baseServiceImpl.get(paramMap);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getMemberLevelList(Map<String,Object> paramMap){
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "MemberNextAmount.getMemberLevelList");
		return baseServiceImpl.getList(paramMap);
	}
}