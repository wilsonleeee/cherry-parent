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
import com.cherry.pt.rps.interfaces.BINOLPTRPS15_IF;
import com.cherry.pt.rps.service.BINOLPTRPS15_Service;

/**
 * 统计销售信息BL
 * 
 * @author WangCT
 * @version 1.0 2012/10/31
 */
public class BINOLPTRPS15_BL implements BINOLPTRPS15_IF, BINOLCM37_IF {
	
	/** 统计销售信息service **/
	@Resource
	private BINOLPTRPS15_Service binOLPTRPS15_Service;

	/**
	 * 查询销售统计总数
	 * 
	 * @param map 查询条件
	 * @return 销售统计总数
	 */
	@Override
	public int getSaleCountInfoCount(Map<String, Object> map) {
		
		// 查询销售统计总数
		return binOLPTRPS15_Service.getSaleCountInfoCount(map);
	}
	
	/**
	 * 查询销售统计信息
	 * 
	 * @param map 查询条件
	 * @return 销售统计信息
	 */
	@Override
	public Map<String, Object> getSaleCountInfo(Map<String, Object> map) {
		
		// 查询销售统计总数
		return binOLPTRPS15_Service.getSaleCountInfo(map);
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
		
		// 查询销售统计信息List
		return binOLPTRPS15_Service.getSaleCountInfoList(map);
	}

	/**
	 * 取得需要转成Excel文件的数据List
	 * @param map 查询条件
	 * @return 需要转成Excel文件的数据List
	 */
	@Override
	public List<Map<String, Object>> getDataList(Map<String, Object> map)
			throws Exception {
		// 查询销售统计信息List
		return binOLPTRPS15_Service.getSaleCountInfoList(map);
	}
	
	/**
	 * 取得开始、截止日期是否处于同一财务年标志
	 * @param map
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public Map<String, Object> getFiscalFlagAndYearValue(Map<String, Object> map, String startTime,
			String endTime) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put("dateValue", startTime);
		Map<String, Object> startFiscalYearAndMinMaxDate = binOLPTRPS15_Service
				.getFiscalYearAndMinMaxDate(paramMap);
		paramMap.put("dateValue", endTime);
		Map<String, Object> endFiscalYearAndMinMaxDate = binOLPTRPS15_Service
				.getFiscalYearAndMinMaxDate(paramMap);
		boolean isSameFiscalYear = startFiscalYearAndMinMaxDate.get(
				"fiscalYear").equals(
				endFiscalYearAndMinMaxDate.get("fiscalYear"));
		if(isSameFiscalYear){
			startFiscalYearAndMinMaxDate.put("fiscalFlag", "2");
		}else{
			startFiscalYearAndMinMaxDate.clear();
		}
		return startFiscalYearAndMinMaxDate;
	}

}
