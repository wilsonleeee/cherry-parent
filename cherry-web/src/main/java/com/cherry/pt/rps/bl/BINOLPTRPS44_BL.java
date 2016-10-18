/*	
 * @(#)BINOLPTRPS15_BL.java     1.0 2012/10/31		
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
package com.cherry.pt.rps.bl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.interfaces.BINOLCM37_IF;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.pt.rps.interfaces.BINOLPTRPS15_IF;
import com.cherry.pt.rps.interfaces.BINOLPTRPS44_IF;
import com.cherry.pt.rps.service.BINOLPTRPS15_Service;
import com.cherry.pt.rps.service.BINOLPTRPS44_Service;

/**
 * 统计销售信息BL
 * 
 * @author WangCT
 * @version 1.0 2012/10/31
 */
public class BINOLPTRPS44_BL implements BINOLPTRPS44_IF, BINOLCM37_IF {
	
	/** 统计销售信息service **/
	@Resource
	private BINOLPTRPS15_Service binOLPTRPS15_Service;
	/** 统计销售信息service **/
	@Resource
	private BINOLPTRPS44_Service binOLPTRPS44_Service;

	/**
	 * 查询销售统计总数
	 * 
	 * @param map 查询条件
	 * @return 销售统计总数
	 */
	@Override
	public int getSaleCountInfoCount(Map<String, Object> map) {
		
		// 产品选择模式
		String selectMode = ConvertUtil.getString(map.get("selectModel"));		
		// 查询销售统计总数
		if (("1").equals(selectMode)) {
			// 查询销售统计总数
			return binOLPTRPS44_Service.getSaleCountInfoCount(map);
		} else {
			// 查询销售统计总数
			return binOLPTRPS15_Service.getSaleCountInfoCount(map);
		}
	}
	
	/**
	 * 查询销售统计信息
	 * 
	 * @param map 查询条件
	 * @return 销售统计信息
	 */
	@Override
	public Map<String, Object> getSaleCountInfo(Map<String, Object> map) {
		// 产品选择模式
		String selectMode = ConvertUtil.getString(map.get("selectModel"));		
		// 查询销售统计总数
		if (("1").equals(selectMode)) {
			return binOLPTRPS44_Service.getSaleCountInfo(map);
		} else {
			return binOLPTRPS15_Service.getSaleCountInfo(map);
		}
	}

	/**
	 * 查询销售统计信息List
	 * 
	 * @param map 检索条件
	 * @return 销售统计信息List
	 */
	@Override
	public List<Map<String, Object>> getSaleCountInfoList(
			Map<String, Object> map) {

		// 产品选择模式
		String selectMode = ConvertUtil.getString(map.get("selectModel"));		
		// 查询销售统计总数
		if (("1").equals(selectMode)) {
			// 查询销售统计信息List
			return binOLPTRPS44_Service.getSaleCountInfoList(map);
		} else {
			// 查询销售统计信息List
			return binOLPTRPS15_Service.getSaleCountInfoList(map);
		}
	}

	/**
	 * 取得需要转成Excel文件的数据List
	 * @param map 查询条件
	 * @return 需要转成Excel文件的数据List
	 */
	@Override
	public List<Map<String, Object>> getDataList(Map<String, Object> map)
			throws Exception {
		
		// 产品选择模式
		String selectMode = ConvertUtil.getString(map.get("selectModel"));		
		// 查询销售统计总数
		if (("1").equals(selectMode)) {
			// 查询销售统计信息List
			return binOLPTRPS44_Service.getSaleCountInfoList(map);
		} else {
			// 查询销售统计信息List
			return binOLPTRPS15_Service.getSaleCountInfoList(map);
		}
	}
	/**
	 * 查询未进行销售统计且为销售记录修改的记录数
	 * 
	 * @param map 查询条件
	 * @return 未进行销售统计且为销售记录修改的记录数
	 */
	public int getModifiedSaleCount(Map<String, Object> map) {
		
		// 产品选择模式
		String selectMode = ConvertUtil.getString(map.get("selectModel"));		
		// 查询销售统计总数
		if (("1").equals(selectMode)) {
			// 查询销售统计信息List
			return binOLPTRPS44_Service.getModifiedSaleCount(map);
		} else {
			// 查询销售统计信息List
			return binOLPTRPS15_Service.getModifiedSaleCount(map);
		}
	}
	
}
