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

import org.springframework.cache.annotation.CacheEvict;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 机器查询Service
 * 
 * @author niushunjie
 * @version 1.0 2011.3.15
 */
@SuppressWarnings("unchecked")
public class BINOLMOMAN01_Service extends BaseService{
    /**
     * 取得机器总数
     * 
     * @param map
     * @return 机器总数
     */
    public int getMachineInfoCount(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOMAN01.getMachineInfoCount");
        return baseServiceImpl.getSum(map);
    }

    /**
     * 取得机器List
     * 
     * @param map
     * @return 机器List
     */
    public List getMachineInfoList(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOMAN01.getMachineInfoList");
        return baseServiceImpl.getList(map);
    }
    
    /**
     * 解除绑定
     * 
     * @param map
     * @return
     */
    public int unbindCounter(Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOMAN01.unbindCounter");
        return baseServiceImpl.update(map);
    }
    
    /**
     * 物理删除机器号对照表中数据
     * 
     * @param map
     * @return
     */
    public int deleteMachineCodeCollate(Map<String, Object> map) {
//        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
//        list.add(map);
//        baseServiceImpl.deleteAll(list,"BINOLMOMAN01.deleteMachineCodeCollate");
    	return baseServiceImpl.remove(map, "BINOLMOMAN01.deleteMachineCodeCollate");
    }
    
    /**
     * 修改机器号对照表中的机器状态，0：停用；1：启用；2：未下发；3：已报废
     * @param map
     * @return
     */
    public int modifyMachineStatus(Map<String, Object> map) {
    	Map<String, Object> paramMap = new HashMap<String, Object>();
    	paramMap.putAll(map);
    	paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOMAN01.modifyMachineStatus");
    	return baseServiceImpl.update(paramMap);
    }
    
    /**
     * 物理删除机器信息表中数据
     * 
     * 
     * */
    @CacheEvict(value="CherryMachineCache",allEntries=true,beforeInvocation=false)
    public void deleteMachineInfo(Map<String, Object> map) {
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        list.add(map);
        baseServiceImpl.deleteAll(list,"BINOLMOMAN01.deleteMachineInfo");
    }
    
    /**
     * 根据机器编号取得绑定信息 
     * 
     * @param map
     * @return
     */
    public List getBindInfoByMachine(Map<String, Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOMAN01.getBindInfoByMachine");
        return baseServiceImpl.getList(map);
    }

    /**
     * 删除柜台机器升级表
     * 
     * @param map
     * @return
     */
    public int delCounterUpgrade (Map<String, Object> map) {
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOMAN01.delCounterUpgrade");
        return baseServiceImpl.remove(map);
    }
}
