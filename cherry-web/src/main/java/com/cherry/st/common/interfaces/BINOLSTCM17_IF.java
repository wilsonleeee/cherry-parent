/*  
 * @(#)BINOLSTCM17_IF.java    1.0 2012.12.06
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
package com.cherry.st.common.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

/**
 * 
 * 出库共通
 * 
 * @author niushunjie
 * @version 1.0 2012.12.06
 */
public interface BINOLSTCM17_IF extends ICherryInterface{

    /**
     * 
     *将产品出库信息写入出库主从表
     * 
     *@param mainData
     *@param detailList
     */
    public int insertProductOutDepotAll(Map<String,Object> mainData,List<Map<String,Object>> detailList);
    
    /**
     * 给出库单主ID，取得概要信息。
     * @param productOutDepotID
     * @return
     */
    public Map<String,Object> getProductOutDepotMainData(int productOutDepotID,String language);
    
    /**
     * 给定出库单主ID，取得明细信息。
     * @param productOutDepotID
     * @return
     */
    public List<Map<String,Object>> getProductOutDepotDetailData(int productOutDepotID,String language);
    
    /**
     * 
     *根据出库单据表来向【入出库记录主表】和
     *【入出库记录明细表】中插入数据，并修改库存。
     * 
     *@param praMap
     */
    public void changeStock(Map<String,Object> praMap);
}