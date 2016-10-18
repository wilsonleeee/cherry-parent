/*  
 * @(#)BINOLMOCIO22_Service.java     1.0 2011/06/14      
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
 * 柜台消息发布Service
 * 
 * @author nanjunbo
 * @version 1.0 2011.06.14
 */
public class BINOLMOCIO22_Service extends BaseService {
    
    /**
     * 取得所有区域柜台树
     * 
     * @param map
     * @return 所有区域柜台树List
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getAllCounter(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO22.getAllCounter");
        return baseServiceImpl.getList(parameterMap);
    }
    
    /***
     * 取得按组织结构柜台树
     * @param map
     * @return
     */
    public List<Map<String, Object>> getDepartCntList(Map<String, Object> map){
    	Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO22.getDepartCntList");
        return baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 取得渠道柜台树
     * @param map
     * @return
     */
    public List<Map<String, Object>> getChannelCntList(Map<String, Object> map) {
    	Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO22.getChannelCntList");
        return baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 取大区信息
     * @param map
     * @return
     */
    public List<Map<String, Object>> getRegionList(Map<String, Object> map) {
    	Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO22.getRegionList");
        return baseServiceImpl.getList(parameterMap);
    }

    /**
     * 取得柜台的控制状态
     * 
     * @param map
     * @return 柜台的控制状态List
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getControlFlagList(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO22.getControlFlagList");
        return baseServiceImpl.getList(parameterMap);
    }
    
    /**
     * 根据消息ID删除柜台消息表中的柜台
     * 
     * @param map
     * @return 
     */
    public void deleteForbidden(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO22.deleteForbidden");
        baseServiceImpl.remove(parameterMap);
    }
    
    /**
     * 插入柜台消息禁止表
     * 
     * @param map
     * @return 
     */
    public void insertCounterMessageForbidden(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO22.insertCounterMessageForbidden");
        baseServiceImpl.save(parameterMap);
    }
    
    /**
     * 更新柜台消息禁止表
     * 
     * @param map
     * @return 柜台消息总数
     */
    public int modifyCounterMessageForbidden(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO22.modifyCounterMessageForbidden");
        return baseServiceImpl.update(parameterMap);
    }
    
    /**
     * 取得柜台的组织ID
     * 
     * @param map
     * @return 柜台消息
     */
    @SuppressWarnings("unchecked")
    public List<Map<String,Object>> getCounterOrganiztionId(Map<String,Object> map){
        map.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO22.getCounterOrganiztionId");
        return baseServiceImpl.getList(map);
    }
    
    /**
     * 取得柜台消息
     * 
     * @param map
     * @return 柜台消息
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getCounterMessage(Map<String, Object> map){
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO22.getCounterMessage");
        return (Map<String, Object>) baseServiceImpl.get(parameterMap);
    }
    
    /**
     * 更新发布时间及状态
     * 
     * @param map
     * @return 
     */
    public int modifyPublishDate(Map<String, Object> map) {
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.putAll(map);
        parameterMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO22.modifyPublishDate");
        return baseServiceImpl.update(parameterMap);
    }
    
    /**
     * 取得系统日期
     * 
     * @param
     * @return 系统日期
     */
    public String getPublishDate() {
        return baseServiceImpl.getDateYMD();
    }
    
    /**
     * 根据柜台对应的部门ID获取柜台code
     * @param organizationId
     * @return
     */
    public String getCounterCode(String organizationId){
        return (String)baseServiceImpl.get(organizationId, "BINOLMOCIO22.getCounterCode");
    }
    
    /**
	 * 柜台存在验证
	 * 
	 * @param map 查询条件
	 */
	public Map<String, Object> getCounterInfo(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO22.getCounterInfo");
		return (Map<String, Object>)baseServiceImpl.get(paramMap);
	}
	
	/**
	 * 取得与导入柜台下发类型相对立的柜台的组织ID
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getContraryOrgID(Map<String, Object> map) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.putAll(map);
		paramMap.put(CherryConstants.IBATIS_SQL_ID, "BINOLMOCIO22.getContraryOrgID");
		return baseServiceImpl.getList(paramMap);
	}
}
