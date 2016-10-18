/*  
 * @(#)BINOLPTUNQ01_Service     1.0 2016-05-26     
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

package com.cherry.pt.unq.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
/**
 * 
 * 唯一码生成Service
 * 
 * @author zw
 * @version 1.0 2016.05.26
 */
public class BINOLPTUNQ01_Service  extends BaseService implements Serializable{
	
	private static final long serialVersionUID = 5215501807776768806L;


	/** 
	 * 查询唯一码生成总数
	 * @param map
	 * @return int
	 * 
	 */
	public int getUnqGenerateCount(Map<String, Object> map) {
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTUNQ01.getUnqGenerateCount");
		return baseServiceImpl.getSum(paramMap);
		
	}
	
	/** 
	 * 查询唯一码一览列表
	 * @param map
	 * @return list
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getUnqViewList(Map<String, Object> map) {
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTUNQ01.getUnqViewList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/** 
	 * 查询唯一码明细列表
	 * @param map
	 * @return list
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getUnqDetailsList(Map<String, Object> map) {
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTUNQ01.getUnqDetailsList");
		return baseServiceImpl.getList(paramMap);
	}
	
	/**
	 * 修改导出Excel次数和最终导出Excel时间
	 * @param map
	 * @return
	 * @throws Exception 
	 */
	public void updateExportExcelCountAndExportExcelTime(Map<String, Object> map) {
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTUNQ01.updateExportExcelCountAndExportExcelTime");
		baseServiceImpl.update(map);
	}

	/* ************************************************************************************************************* */
	
	/**
	 * 插入产品唯一码批次表主键ID
	 * 
	 * @param map
	 * @return int
	 */
	public int insertPrtUniqueCodeBatch(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTUNQ01.insertPrtUniqueCodeBatch");
		return baseServiceImpl.saveBackId(map);
	}
	
	/**
	 * 插入产品表并返回产品ID
	 * 
	 * @param map
	 * @return int
	 */
	public void insertPrtUniqueCodeDetail(List list) {
		baseServiceImpl.saveAll(list, "BINOLPTUNQ01.insertPrtUniqueCodeDetail");
	}


	/**
	 * 取得批次号
	 * 
	 * @param map 查询条件
	 * @return 品牌code
	 */
	public String getNewPrtUniqueCodeBatchNo(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLPTUNQ01.getNewPrtUniqueCodeBatchNo");
		return (String)baseServiceImpl.get(parameterMap);
	}


}
