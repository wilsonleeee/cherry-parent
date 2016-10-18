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
 * POS品牌菜单管理Service
 * 
 * @author niushunjie
 * @version 1.0 2011.3.15
 */
@SuppressWarnings("unchecked")
public class BINOLMOMAN10_Service extends BaseService{
	
	 /**
     * 取得POS品牌菜单管理List 
     * 
     * @param map
     * @return POS品牌菜单管理List
     */
    public List searchPosMenuBrandInfoList(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOMAN10.getMenuBrandInfoList");
        return baseConfServiceImpl.getList(map);
    }
    
    /** 修改POS品牌菜单管理
    * 
    * @param map
    */
   public  void updatePosMenuBrand(Map<String, Object> map) throws Exception {
   	 Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOMAN10.updatePosMenuBrand");
        baseConfServiceImpl.update(parameterMap);
   }
	 
   /** 修改POS品牌菜单管理MenuStatus
    * 
    * @param map
    */
   public  void updatePosMenuBrandMenuStatus(Map<String, Object> map) throws Exception {
   	 	Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOMAN10.updatePosMenuBrandMenuStatus");
        baseConfServiceImpl.update(parameterMap);
   }
   
   /** 新增POS品牌菜单管理
    * 
    * @param map
    * @return 
    */
   public  int addPosMenuBrand(Map<String, Object> map) throws Exception {
   	 Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOMAN10.addPosMenuBrand");
        return  baseConfServiceImpl.saveBackId(parameterMap);
   }
   
   
   /**
	 * 取得配置项数据List(WS结构组装使用)
	 * 
	 * @param map
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getPosMenuBrandWithWS(Map<String, Object> map) {
		map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOMAN10.getPosMenuBrandWithWS");
		return baseConfServiceImpl.getList(map);
	}
}
