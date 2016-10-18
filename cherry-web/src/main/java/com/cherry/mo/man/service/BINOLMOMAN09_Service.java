/*
 * @(#)BINOLMOMAN01_Service.java     1.0 2011/3/15
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
package com.cherry.mo.man.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * POS菜单Service
 * 
 * @author 
 * @version 
 */
@SuppressWarnings("unchecked")
public class BINOLMOMAN09_Service extends BaseService{
	
	
	/**
     * 取得POS菜单总数
     * 
     * @param map
     * @return POS菜单总数
     */
    public int getPosMemuInfoCount(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOMAN09.getPosMemuInfoCount");
        return baseConfServiceImpl.getSum(map);
    }

    /**
     * 取得POS菜单List 
     * 
     * @param map
     * @return POS菜单List
     */
    public List getPosMemuInfoList(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOMAN09.getPosMemuInfoList");
        return baseConfServiceImpl.getList(map);
    }
    
    public Map getPosMemu(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOMAN09.getPosMemu");
		return(Map) baseConfServiceImpl.get(map);
	}
    
    /**
     * 修改菜单数据
     * 
     * @param map
     */
    public  void updatePosMemu(Map<String, Object> map) throws Exception {
    	 Map<String, Object> parameterMap = new HashMap<String, Object>();
         parameterMap.putAll(map);
         parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOMAN09.updatePosMemu");
         baseConfServiceImpl.update(parameterMap);
    }
    
    /**
     * 新增菜单数据
     * 
     * @param map
     */
    public  int addPosMemu(Map<String, Object> map) throws Exception {
    	 Map<String, Object> parameterMap = new HashMap<String, Object>();
         parameterMap.putAll(map);
         parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOMAN09.addPosMemu");
         return baseConfServiceImpl.saveBackId(parameterMap);
    }
    
    /**
     * 删除菜单数据
     * 
     * @param map
     */
    public  void deletePosMemu(Map<String, Object> map) throws Exception {
    	 Map<String, Object> parameterMap = new HashMap<String, Object>();
         parameterMap.putAll(map);
         parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOMAN09.deletePosMemu");
         baseConfServiceImpl.update(parameterMap);
    }
    
    /**
   	 * 取得菜单数据List(WS结构组装使用)
   	 * 
   	 * @param map
   	 * 
   	 * @return
   	 */
   	public List<Map<String, Object>> getPosMemuWS(Map<String, Object> map) {
   		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOMAN09.getPosMemuWS");
   		return baseConfServiceImpl.getList(map);
   	}
   	
}
