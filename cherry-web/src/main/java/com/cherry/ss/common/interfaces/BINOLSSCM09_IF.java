/*  
 * @(#)BINOLSSCM09_IF.java    1.0 2013/01/25
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
package com.cherry.ss.common.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

/**
 * 
 * 入库共通Interface
 * 
 * @author niushunjie
 * @version 1.0 2013.01.25
 */
public interface BINOLSSCM09_IF extends ICherryInterface{
    /**
     * 
     *将促销品入库信息写入入库主从表
     * 
     *@param mainData
     *@param detailList
     */
    public int insertPrmInDepotAll(Map<String,Object> mainData,List<Map<String,Object>> detailList);
    
    /**
     * 
     *修改入库单据主表数据
     * 
     *@param praMap
     */
    public int updatePrmInDepotMain(Map<String,Object> praMap);
    
    /**
     * 
     *根据入库单据表来向【入出库记录主表】和
     *【入出库记录明细表】中插入数据，并修改库存。
     * 
     *@param praMap
     */
    public void changeStock(Map<String,Object> praMap);
    
    /**
     * 
     *给定入库单主ID，取得概要信息。
     * 
     *@param prmInDepotMainID
     */
    public Map<String,Object> getPrmInDepotMainData(int prmInDepotMainID,String language);
    
    /**
     * 
     *给定入出库单主ID，取得明细信息。
     * 
     *@param prmInDepotMainID
     */
    public List<Map<String,Object>> getPrmInDepotDetailData(int prmInDepotMainID,String language);
    
    /**
     * 判断入库单号存在
     * @param relevantNo
     * @return
     */
    public List<Map<String,Object>> selPrmInDepot(String relevantNo);
    
    /**
     * 给定入库单主ID，删除入库单明细
     * @param praMap
     * @return
     */
    public int delPrmInDepotDetailData(Map<String,Object> praMap);
    
    /**
     * 插入入库单明细表
     * @param list
     */
    public void insertPrmInDepotDetail(List<Map<String,Object>> list);
}