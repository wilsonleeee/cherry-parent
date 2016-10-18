/*
 * @(#)BINOLBSDEP10_BL.java     1.0 2011.2.10
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

import com.cherry.bs.dep.service.BINOLBSDEP92_Service;

/**
 * 品牌一览画面BL
 * 
 * @author WangCT
 * @version 1.0 2011.2.10
 */
public class BINOLBSDEP10_BL {
	
	/** 品牌信息管理共通Service */
	@Resource
	private BINOLBSDEP92_Service binOLBSDEP92_Service;
	
	/**
	 * 查询品牌总数
	 * 
	 * @param map 检索条件
	 * @return 返回品牌总数
	 */
	public int getBrandInfoCount(Map<String, Object> map) {
		
		// 查询品牌总数
		return binOLBSDEP92_Service.getBrandInfoCount(map);
	}

	/**
	 * 取得品牌信息List
	 * 
	 * @param map 检索条件
	 * @return 品牌信息List
	 */
	public List<Map<String, Object>> getBrandInfoList(Map<String, Object> map) {
		
		// 取得品牌信息List
		return binOLBSDEP92_Service.getBrandInfoList(map);
	}

}
