/*
 * @(#)BINOLMOMAN03_Service.java     1.0 2011/3/21
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 绑定柜台Service
 * 
 * @author niushunjie
 * @version 1.0 2011.3.21
 */
@SuppressWarnings("unchecked")
public class BINOLMOMAN03_Service extends BaseService{
    /**
     * 绑定柜台
     * 
     * @param map
     * @return 
     */
    public int bindCounter(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOMAN03.bindCounter");
        return baseServiceImpl.update(map);
    }
    
    /**
     * 插入柜台机器升级表
     * 
     * @param map
     * @return 
     */
    public void addCounterUpgrade(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOMAN03.addCounterUpgrade");
        baseServiceImpl.save(map);
    }
    
    /**
     * 取得柜台总数
     * 
     * @param map 查询条件
     * @return 返回柜台总数
     */
    public int getCounterInfoCount(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOMAN03.getCounterInfoCount");
        return baseServiceImpl.getSum(parameterMap);
    }
    
    /**
     * 取得柜台List
     * 
     * @param map 查询条件
     * @return 柜台List
     */
    public List<Map<String, Object>> getCounterInfoList(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOMAN03.getCounterInfoList");
        return (List)baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 根据机器编号取得品牌简称
     * 
     * @param map
     * @return
     */
    public String getBrandNameShortByMachine(Map<String, Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOMAN03.getBrandNameShortByMachine");
        return (String) baseServiceImpl.get(map);
    }
    
    /**
     * 根据机器编号取得机器类型
     * 
     * @param map
     * @return
     */
    public String getMachineTypeByCode(Map<String, Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOMAN03.getMachineTypeByCode");
        return (String) baseServiceImpl.get(map);
    }

    /**
     * 判断升级柜台机器类型是否存在
     * 
     * @param map
     * @return
     */
    public String getUpgradeMachineType(Map<String, Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOMAN03.getUpgradeMachineType");
        return (String) baseServiceImpl.get(map);
    }
    
}
