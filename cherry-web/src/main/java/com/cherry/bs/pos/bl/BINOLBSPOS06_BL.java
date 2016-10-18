/*
 * @(#)BINOLBSPOS06_BL.java     1.0 2010/10/27
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

package com.cherry.bs.pos.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.pos.service.BINOLBSPOS06_Service;

/**
 * 岗位类别一览画面BL
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
public class BINOLBSPOS06_BL {
	
	/** 岗位类别一览画面Service */
	@Resource
	private BINOLBSPOS06_Service binOLBSPOS06_Service;
	
	/**
	 * 取得岗位类别总数
	 * 
	 * @param map 检索条件
	 * @return 返回岗位类别总数
	 */
	public int getPositionCategoryCount(Map<String, Object> map) {
		
		// 取得岗位类别总数
		return binOLBSPOS06_Service.getPositionCategoryCount(map);
	}
	
	/**
	 * 取得岗位类别信息List
	 * 
	 * @param map 检索条件
	 * @return 岗位类别信息List
	 */
	public List<Map<String, Object>> getPositionCategoryList(Map<String, Object> map) {
		
		// 取得岗位类别信息List
		return binOLBSPOS06_Service.getPositionCategoryList(map);
	}

}
