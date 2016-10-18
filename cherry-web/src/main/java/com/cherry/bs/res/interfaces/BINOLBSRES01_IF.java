/*
 * @(#)BINOLBSRES01_IF.java     1.0 2014/10/29
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
package com.cherry.bs.res.interfaces;

import java.util.List;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM37_IF;

import java.util.Map;

import com.cherry.cm.core.CherryException;

/**
 * 经销商一览
 * 
 * @author hujh
 * @version 1.0 2014/11/11
 */
public interface BINOLBSRES01_IF extends BINOLCM37_IF{

	/**
	 * 获取经销商总数
	 * 
	 * @param map
	 * @return
	 */
	public int getResCount(Map<String, Object> map);

	/**
	 * 获取经销商List
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getResList(Map<String, Object> map);
	
	/**
	 * 启用经销商
	 * @param map
	 * @return
	 * @throws CherryException
	 */
	public int tran_enableRes(Map<String, Object> map) throws CherryException;
    
	/**
	 * 停用经销商
	 * @param map
	 * @return
	 * @throws CherryException
	 */
	public int tran_disableRes(Map<String, Object> map) throws CherryException;

	/**
	 * excel 导出
	 * @param searchMap
	 * @return
	 * @throws Exception
	 */	
	
	public Map<String, Object> getExportMap(Map<String, Object> map);
	public String export(Map<String, Object> map) throws Exception;
	
}