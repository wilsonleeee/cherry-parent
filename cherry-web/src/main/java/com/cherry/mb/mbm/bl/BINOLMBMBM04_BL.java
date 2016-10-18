/*
 * @(#)BINOLMBMBM04_BL.java     1.0 2012.10.10
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

import com.cherry.cm.cmbussiness.interfaces.BINOLCM37_IF;
import com.cherry.mb.mbm.service.BINOLMBMBM04_Service;

/**
 * 会员积分详细画面BL
 * 
 * @author WangCT
 * @version 1.0 2012.10.10
 */
public class BINOLMBMBM04_BL implements BINOLCM37_IF {
	
	/** 会员积分详细画面Service */
	@Resource
	private BINOLMBMBM04_Service binOLMBMBM04_Service;
	
	/**
	 * 查询会员积分信息
	 * 
	 * @param map 检索条件
	 * @return 会员积分信息
	 */
	public Map<String, Object> getMemberPointInfo(Map<String, Object> map) {
		
		// 查询会员积分信息
		return binOLMBMBM04_Service.getMemberPointInfo(map);
	}
	
	/**
	 * 查询积分明细信息总数
	 * 
	 * @param map 检索条件
	 * @return 积分明细信息总数
	 */
	public int getPointDetailCount(Map<String, Object> map) {
		
		// 查询积分明细信息总数
		return binOLMBMBM04_Service.getPointDetailCount(map);
	}
	
	/**
	 * 查询积分明细信息List
	 * 
	 * @param map 检索条件
	 * @return 积分明细信息List
	 */
	public List<Map<String, Object>> getPointDetailList(Map<String, Object> map) {
		
		// 查询积分明细信息List
		return binOLMBMBM04_Service.getPointDetailList(map);
	}
	
	/**
	 * 查询积分明细信息总数
	 * 
	 * @param map 检索条件
	 * @return 积分明细信息总数
	 */
	public int getPointDetail2Count(Map<String, Object> map) {
		
		// 查询积分明细信息总数
		return binOLMBMBM04_Service.getPointDetail2Count(map);
	}
	
	/**
	 * 查询积分明细信息List
	 * 
	 * @param map 检索条件
	 * @return 积分明细信息List
	 */
	public List<Map<String, Object>> getPointDetail2List(Map<String, Object> map) {
		
		// 查询积分明细信息List
		return binOLMBMBM04_Service.getPointDetail2List(map);
	}

	/**
	 * 取得需要转成Excel文件的数据List
	 * @param map 查询条件
	 * @return 需要转成Excel文件的数据List
	 */
	@Override
	public List<Map<String, Object>> getDataList(Map<String, Object> map)
			throws Exception {
		// 查询积分明细信息List
		return binOLMBMBM04_Service.getPointDetail2List(map);
	}
}
