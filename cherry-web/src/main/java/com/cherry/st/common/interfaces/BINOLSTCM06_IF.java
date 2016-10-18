/*  
 * @(#)BINOLSTCM06_IF.java    1.0 2011.09.20
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
 * 产品盘点业务共通Interface
 * 
 * @author niushunjie
 * @version 1.0 2011.09.20
 */
public interface BINOLSTCM06_IF extends ICherryInterface{

    /**
     * 将盘点信息写入盘点单据主从表。
     * @param mainData
     * @param detailList
     * @return
     */
    public int insertStockTakingAll(Map<String,Object> mainData,List<Map<String,Object>> detailList);
    
    /**
     * 修改盘点单据主表数据。
     * @param praMap
     * @return
     */
    public int updateStockTakingMain(Map<String,Object> praMap);
    
    /**
     * 根据盘点单据来写入出库记录主从表，并修改库存。
     * @param praMap
     */
    public void changeStock(Map<String,Object> praMap);
    
    /**
     * 给盘点单ID，取得概要信息。
     * @param productStockTakingID
     * @return
     */
    public Map<String,Object> getStockTakingMainData(int productStockTakingID,String language);
    
    /**
     * 给盘点单ID，取得明细信息。
     * @param productStockTakingID
     * @return
     */
    public List<Map<String,Object>> getStockTakingDetailData(int productStockTakingID,String language);
    
    /**
     * 给盘点单ID，取得明细信息。(UnitCode/BarCode排序)
     * @param productStockTakingID
     * @return
     */
    public List<Map<String,Object>> getStockTakingDetailDataByOrder(int productStockTakingID,String language,String prtOrderBy);
}
