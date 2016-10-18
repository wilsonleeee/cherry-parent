/*
 * @(#)BINOLBSDEP06_BL.java     1.0 2011.2.10
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

package com.cherry.bs.dep.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.dep.service.BINOLBSDEP91_Service;

/**
 * 组织一览画面BL
 * 
 * @author WangCT
 * @version 1.0 2011.2.10
 */
public class BINOLBSDEP06_BL {
	
	/** 组织信息管理共通Service */
	@Resource
	private BINOLBSDEP91_Service binOLBSDEP91_Service;
	
	/**
	 * 查询组织总数
	 * 
	 * @param map 检索条件
	 * @return 返回组织总数
	 */
	public int getOrganizationCount(Map<String, Object> map) {
		
		// 查询组织总数
		return binOLBSDEP91_Service.getOrganizationCount(map);
	}

	/**
	 * 取得组织信息List
	 * 
	 * @param map 检索条件
	 * @return 组织信息List
	 */
	public List<Map<String, Object>> getOrganizationList(Map<String, Object> map) {
		
		// 取得组织信息List
		return binOLBSDEP91_Service.getOrganizationList(map);
	}

}
