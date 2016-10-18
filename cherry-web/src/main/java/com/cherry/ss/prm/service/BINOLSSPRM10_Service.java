/*
 * @(#)BINOLSSPRM10_Service.java     1.0 2011/02/24
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
package com.cherry.ss.prm.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;
/**
 * 促销品类别添加
 * @author Administrator
 *
 */
@SuppressWarnings("unchecked")
public class BINOLSSPRM10_Service extends BaseService {
   
    /**
     * 取得某促销品类别的上级类别
     * 
     * @param map 检索条件
     * @return 促销品类别List
     */
     public List getHigherCategoryList(Map<String, Object> map) {
 	   Map<String, Object> parameterMap = new HashMap<String, Object>();
 	   parameterMap.putAll(map);
 	   parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM10.getHigherCategoryList");
 	   return baseServiceImpl.getList(parameterMap);
     }
     
     /**
 	 * 取得新节点
 	 * 
 	 * @param map 查询条件
 	 * @return 新节点
 	 */
 	public String getNewNodeId(Map<String, Object> map) {
 		Map<String, Object> parameterMap = new HashMap<String, Object>();
 		parameterMap.putAll(map);
 		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM10.getNewNodeId");
 		return (String)baseServiceImpl.get(parameterMap);
 	}
	
	/**
	 * 插入促销品类别信息
	 * 
	 * @param map
	 * @return
	 */
 	public void insertPrmCategory(Map<String, Object> map) {
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.putAll(map);
		parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM10.insertPrmCategory");
		baseServiceImpl.save(parameterMap);
	}
	
 	/**
     * 取得某促销品类别的上级类别
     * 
     * @param map 检索条件
     * @return 促销品类别
     */
     public List getHigherClass(Map<String, Object> map) {
 	   Map<String, Object> parameterMap = new HashMap<String, Object>();
 	   parameterMap.putAll(map);
 	   parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLSSPRM10.getHigherClass");
 	   return baseServiceImpl.getList(parameterMap);
     }
	
}
