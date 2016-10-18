/*
 * @(#)BINOLCTRPT06_IF.java     1.0 2014/11/11
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

/**
 * 会员沟通效果统计 BL
 * @author menghao
 * @version 1.0 2014/11/11
 *
 */
public interface BINOLCTRPT06_IF {
	
	/**
	 * 获取会员沟通效果统计记录总数
	 * @param map
	 * @return
	 */
	public int getMemCommunStatisticsCount(Map<String, Object> map);
	
	/**
	 * 获取会员沟通效果统计记录List
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getMemCommunStatisticsList (Map<String, Object> map) throws Exception;
	
	/**
	 * 获取指定沟通的会员销售情况记录总数
	 * @param map
	 * @return
	 */
	public int getCommunEffectDetailCount(Map<String, Object> map);
	
	/**
	 * 获取指定沟通的会员销售情况记录List
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getCommunEffectDetailList (Map<String, Object> map) throws Exception;
	
	/**
	 * 获取会员沟通效果统计信息
	 * @param map
	 * @return
	 */
	public Map<String, Object> getMemCommunResultInfo(Map<String, Object> map);
	
}
