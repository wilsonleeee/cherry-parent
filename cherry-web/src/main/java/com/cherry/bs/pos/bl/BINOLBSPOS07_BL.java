/*
 * @(#)BINOLBSPOS07_BL.java     1.0 2010/10/27
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

import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.pos.service.BINOLBSPOS07_Service;

/**
 * 岗位类别详细画面BL
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
public class BINOLBSPOS07_BL {
	
	/** 岗位详细画面Service */
	@Resource
	private BINOLBSPOS07_Service binOLBSPOS07_Service;
	
	/**
	 * 查询岗位类别信息
	 * 
	 * @param map 查询条件
	 * @return 岗位类别信息
	 */
	public Map<String, Object> getPosCategoryInfo(Map<String, Object> map) {
		
		// 查询岗位类别信息
		return  binOLBSPOS07_Service.getPosCategoryInfo(map);
	}

}
