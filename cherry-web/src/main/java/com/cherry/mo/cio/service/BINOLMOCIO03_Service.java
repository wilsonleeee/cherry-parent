/*  
 * @(#)BINOLMOCIO03_Service.java     1.0 2011/05/31      
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
package com.cherry.mo.cio.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

public class BINOLMOCIO03_Service extends BaseService {

	/**
	 * 保存问卷
	 * 
	 * @param map
	 *            存放要插入的数据
	 * @return int 返回自增长ID
	 * 
	 * */
	public int savePaper(Map<String, Object> map) {
		// map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO03.insertPaper");
		return baseServiceImpl.saveBackId(map, "BINOLMOCIO03.insertPaper");
	}

	/**
	 * 保存问题
	 * 
	 * @param list
	 *            存放要保存的问题
	 * 
	 * 
	 * */
	public void saveQuestion(List<Map<String, Object>> list) {
		baseServiceImpl.saveAll(list, "BINOLMOCIO03.insertPaperQuestion");
	}

	/**
	 * 根据品牌id获取品牌code
	 * 
	 * @param map
	 *            存放品牌id
	 * @return string 品牌code
	 * 
	 * */
	public String getBrandCode(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO03.getBrandCode");
		return (String) baseServiceImpl.get(map);
	}

	/**
	 * 根据问卷id获取问卷信息，供下发时使用
	 * 
	 * @param map
	 *            存放问卷id
	 * @return map 返回的问卷信息
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getPaper(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO03.getPaper");
		return (Map<String, Object>) baseServiceImpl.get(map);
	}

	/**
	 * 根据问卷id获取问题信息，供下发时使用
	 * 
	 * @param map
	 *            存放问卷id
	 * @return list 返回的问题的list
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getQuestion(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO03.getQuestion");
		return baseServiceImpl.getList(map);
	}
	
	/**
	 * 判断系统中是否已经存在相同名称的问卷
	 * 
	 * */
	public List<Map<String, Object>> isExsitSameNamePaper(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO03.isExsitSameNamePaper");
		return baseServiceImpl.getList(map);
	}
}
