/*
 * @(#)BINOLPLSCF10_BL.java     1.0 2010/10/27
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

package com.cherry.pl.scf.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.pl.scf.service.BINOLPLSCF10_Service;

/**
 * code值编辑BL
 * 
 * @author zhangjie
 * @version 1.0 2010.10.27
 */
public class BINOLPLSCF10_BL {
	
	/** code值编辑Service */
	@Resource
	private BINOLPLSCF10_Service binolplscf10Service;

	/**
	 * 查询code表信息总数
	 * 
	 * @param map 查询条件
	 * @return int
	 */
	public int getCoderCount(Map<String, Object> map) {
		
		return binolplscf10Service.getCoderCount(map);
	}

	/**
	 * 查询code管理表信息LIST
	 * 
	 * @param map 查询条件
	 * @return int
	 */
	public List<Map<String, Object>> getCoderList(Map<String, Object> map) {
		
		// 查询业务类型List
		return binolplscf10Service.getCoderList(map);
	}


}
