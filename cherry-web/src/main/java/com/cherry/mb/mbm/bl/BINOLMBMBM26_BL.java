/*
 * @(#)BINOLMBMBM26_BL.java     1.0 2013.09.23
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
package com.cherry.mb.mbm.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.mb.mbm.service.BINOLMBMBM26_Service;

/**
 * 会员问题画面BL
 * 
 * @author WangCT
 * @version 1.0 2013.09.23
 */
public class BINOLMBMBM26_BL {
	
	/** 会员问题画面Service **/
	@Resource
	private BINOLMBMBM26_Service binOLMBMBM26_Service;
	
	/**
	 * 取得会员问题总数
	 * 
	 * @param map 检索条件
	 * @return 会员问题总数
	 */
	public int getIssueCount(Map<String, Object> map) {
		
		// 取得会员问题总数
		return binOLMBMBM26_Service.getIssueCount(map);
	}
	
	/**
	 * 取得会员问题List
	 * 
	 * @param map 检索条件
	 * @return 会员问题List
	 */
	public List<Map<String, Object>> getIssueList(Map<String, Object> map) {
		
		// 取得会员问题List
		return binOLMBMBM26_Service.getIssueList(map);
	}

}
