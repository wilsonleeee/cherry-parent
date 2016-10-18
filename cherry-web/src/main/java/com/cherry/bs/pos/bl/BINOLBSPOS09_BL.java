/*
 * @(#)BINOLBSPOS09_BL.java     1.0 2010/10/27
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

import com.cherry.bs.pos.service.BINOLBSPOS09_Service;

/**
 * 添加岗位类别画面BL
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
public class BINOLBSPOS09_BL {
	
	/** 添加岗位类别画面Service */
	@Resource
	private BINOLBSPOS09_Service binOLBSPOS09_Service;
	
	/**
	 * 添加岗位类别
	 * 
	 * @param map 添加内容
	 */
	public void tran_addPosCategory(Map<String, Object> map) {
		
		// 添加岗位类别
		binOLBSPOS09_Service.addPosCategory(map);
	}
	
	/**
	 * 验证同一组织中是否存在同样的岗位代码
	 * 
	 * @param map 检索条件
	 * @return 返回岗位类别ID
	 */
	public String getPosCategoryCheck(Map<String, Object> map) {
		
		// 验证同一组织中是否存在同样的岗位代码
		return binOLBSPOS09_Service.getPosCategoryCheck(map);
	}
	
	/**
	 * 验证同一组织中是否存在同样的岗位名称
	 * 
	 * @param map 检索条件
	 * @return 返回岗位类别ID
	 */
	public String getPosCategoryNameCheck(Map<String, Object> map) {
		
		// 验证同一组织中是否存在同样的岗位名称
		return binOLBSPOS09_Service.getPosCategoryNameCheck(map);
	}

}
