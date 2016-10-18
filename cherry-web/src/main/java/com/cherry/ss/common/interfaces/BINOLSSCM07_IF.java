/*  
 * @(#)BINOLSSCM07_IF.java     1.0 2012/09/27       
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
 * 促销品入出库操作共通IF
 * @author niushunjie
 * @version 1.0 2012.09.27
 */
public interface BINOLSSCM07_IF extends ICherryInterface{
    
    /**
     * 将促销品入出库信息写入入出库主从表，并更新相应的库存记录。
     * @param mainData
     * @param detailList
     * @return 入出库主表的自增ID
     */
    public int insertPromotionStockInOutAll(Map<String,Object> mainData,List<Map<String,Object>> detailList);
    
    /**
     * 修改入出库单据主表数据。
     * @param praMap
     * @return 更新影响的数据行数
     */
    public int updatePromotionStockInOutMain(Map<String,Object> praMap);
    
    /**
     * 根据入出库单据修改库存。
     * @param praMap
     */
    public void changeStock(Map<String,Object> praMap);
    
    /**
     * 给定入出库单主ID，取得概要信息。
     * @param promotionStockInOutID
     * @return
     */
    public Map<String,Object> getPromotionStockInOutMainData(int promotionStockInOutID,String language);
    
    /**
     * 给定入出库单主ID，取得明细信息。
     * @param promotionStockInOutID
     * @return
     */
    public List<Map<String,Object>> getPromotionStockDetailData(int promotionStockInOutID,String language);    
  
}
