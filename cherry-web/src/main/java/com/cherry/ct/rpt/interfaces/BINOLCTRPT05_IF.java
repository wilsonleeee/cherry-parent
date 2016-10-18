/*
 * @(#)BINOLCTRPT03_IF.java     1.0 2013/08/06
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
package com.cherry.ct.rpt.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.cmbussiness.interfaces.BINOLCM37_IF;

public interface BINOLCTRPT05_IF extends BINOLCM37_IF{
	
	/**
	 * 获取沟通效果统计List
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getAnalysisList(Map<String, Object> map);
	
	/**
	 * 获取沟通效果统计count
	 * @param map
	 * @return
	 */
	public int getAnalysisCount(Map<String, Object> map);
	
	/**
	 * 获取信息发送时间
	 * @param map
	 * @return
	 */
	public String getSendTime(Map<String, Object> map) throws Exception;
	
	/**
	 * 获取沟通统计的统计信息
	 * @param map
	 * @return
	 */
	public Map<String, Object> getAnalysisTotal(Map<String, Object> map);
	
	/**
	 * 获取会员参与明细List
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getJoinDetailList(Map<String, Object> map);
	  
	/**
	 * 获取会员参与明细count
	 * @param map
	 * @return
	 */
	public int getJoinDetailCount(Map<String, Object> map);
	
	/**
	 *  获取会员销售明细List
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getSaleDetailList(Map<String, Object> map);
	
	/**
	 * 获取会员销售明细Count
	 * @param map
	 * @return
	 */
	public int getSaleDetailCount(Map<String, Object> map);
	
	/**
	 * 获取导出Map
	 * @param map
	 * @return
	 */
	public Map<String, Object> getExportMap(Map<String, Object> map);
	
	/**
	 * 获取参与明细导出Map
	 * @param map
	 * @return
	 */
	public Map<String, Object> getJoinExportMap(Map<String, Object> map);
	
	/**
	 * 获取购买明细导出Map
	 * @param map
	 * @return
	 */
	public Map<String, Object> getSaleExportMap(Map<String, Object> map);
	
	/**
	 * 导出CSV处理
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public String export(Map<String, Object> map) throws Exception ;
	


}
