/*  
 * @(#)BINOLMOCIO01_Service.java     1.0 2011/06/09      
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.service.BaseService;

/**
 * 
 * 柜台消息管理Service
 * 
 * @author niushunjie
 * @version 1.0 2011.06.09
 */
public class BINOLMOCIO01_Service extends BaseService {

    /**
     * 取得柜台消息总数
     * 
     * @param map
     * @return 柜台消息总数
     */
    public int getCounterMessageCount(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO01.getCounterMessageCount");
        return baseServiceImpl.getSum(parameterMap);
    }
    
    /**
     * 取得取得柜台消息总数List
     * 
     * @param map 查询条件
     * @return 返回机器总数
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getCounterMessageList(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO01.getCounterMessageList");
        return baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 新增柜台消息
     * 
     * @param map
     * @return 
     */
    public void addCounterMessage(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO01.addCounterMessage");
        baseServiceImpl.save(parameterMap);
    }
    
    /**
     * 编辑柜台消息（排他）
     * 
     * @param map 查询条件
     * @return 
     */
    public int modifyCounterMessage(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO01.modifyCounterMessage");
        return baseServiceImpl.update(parameterMap);
    }
    
    /**
     * 删除柜台消息
     * 
     * @param map 查询条件
     * @return 
     */
    public int deleteCounterMessage(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO01.deleteCounterMessage");
        return baseServiceImpl.update(parameterMap);
    }
    
    /**
     * 取得柜台消息
     * 
     * @param Map
     * @return  
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getCounterMessage(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO01.getCounterMessage");
        return (Map<String, Object>) baseServiceImpl.get(parameterMap);
    }
    
    /**
	 * 取得已下发的消息的下发柜台及其所属区域
	 * 
	 * */
    @SuppressWarnings("unchecked")
	public List<Map<String,Object>> getMessageRegion(Map<String,Object> map){
    	return baseServiceImpl.getList(map,"BINOLMOCIO01.getMessageRegion");
    }
    
    /**
     * 取大区信息
     * @param map
     * @return
     */
    public List<Map<String, Object>> getRegionList(Map<String, Object> map) {
    	Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO01.getRegionList");
        return baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 取得已下发的消息的下发柜台及其所属渠道
     * @param map
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getMessageChannel(Map<String, Object> map) {
    	Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO01.getMessageChannel");
        return baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 取得已下发的消息的下发柜台及其所属组织结构
     * @param map
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getMessageOrganize(Map<String, Object> map) {
    	Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO01.getMessageOrganize");
        return baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 查询某条消息的下发类型
     * 
     * */
    @SuppressWarnings("unchecked")
	public List<Map<String,Object>> getControlFlag(Integer messageId){
    	return baseServiceImpl.getList(messageId,"BINOLMOCIO01.getControlFlag");
    }
    
    /**
     * 更新柜台消息状态
     * @param map
     * @return
     */
    public int disableOrEnable(Map<String, Object> map){
    	Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO01.disableOrEnable");
        return baseServiceImpl.update(parameterMap);
    }
}
