/*
 * @(#)BINOLSTBIL12_Service.java     1.0 2011/11/2
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
package com.cherry.st.bil.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 退库单明细一览
 * @author niushunjie
 * @version 1.0 2011.11.2
 */
@SuppressWarnings("unchecked")
public class BINOLSTBIL12_Service extends BaseService{
    /**
     * 删除【产品退库单据表】伦理删除
     * @param map
     * @return
     */
    public int deleteProductReturnLogic(Map<String, Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTBIL12.deleteProductReturnLogic");
        return baseServiceImpl.update(map);
    }
    
    /**
     * 删除【产品退库单据明细表】伦理删除
     * @param map
     * @return
     */
    public int deleteProductReturnDetailLogic(Map<String, Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTBIL12.deleteProductReturnDetailLogic");
        return baseServiceImpl.update(map);
    }
    /**
     * 删除【产品退库单据明细表】
     * @param map
     * @return
     */
    public int deleteProductReturnDetail(Map<String, Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTBIL12.deleteProductReturnDetail");
        return baseServiceImpl.update(map);
    }
    
    /**
     * 取得实体仓库的所属部门
     * @param map
     * @return
     */
    public List<Map<String,Object>> getOrganIdByDepotInfoID(Map<String, Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTBIL12.getOrganIdByDepotInfoID");
        return baseServiceImpl.getList(map);
    }
    
    /**
     * 取得退库仓库
     * @param map
     * @return
     */
    public List<Map<String,Object>> getRRInventoryInfo(Map<String, Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTBIL12.getRRInventoryInfo");
        return baseServiceImpl.getList(map);
    }
    
    /**
     * 取得接受退库仓库
     * @param map
     * @return
     */
    public List<Map<String,Object>> getARInventoryInfo(Map<String, Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTBIL12.getARInventoryInfo");
        return baseServiceImpl.getList(map);
    }
    
    /**
     * 取得入出库仓库
     * @param map
     * @return
     */
    public List<Map<String,Object>> getIOInventoryInfo(Map<String, Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTBIL12.getIOInventoryInfo");
        return baseServiceImpl.getList(map);
    }
    
    /**
     * 取得ReturnNoIF
     * @param map
     * @return
     */
    public List<Map<String,Object>> getReturnNoIF(Map<String, Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLSTBIL12.getReturnNoIF");
        return baseServiceImpl.getList(map);
    }
}
