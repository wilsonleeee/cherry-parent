package com.cherry.st.common.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.ICherryInterface;


/**
 * 
 * 产品入出库表操作共通Interface
 * 
 * @author niushunjie
 * @version 1.0 2011.09.02
 */
public interface BINOLSTCM01_IF extends ICherryInterface{
    
    /**
     * 将产品入出库信息写入入出库主从表，并更新相应的库存记录。
     * @param mainData
     * @param detailList
     * @return 入出库主表的自增ID
     */
    public int insertProductInOutAll(Map<String,Object> mainData,List<Map<String,Object>> detailList);
    
    /**
     * 修改入出库单据主表数据。
     * @param praMap
     * @return 更新影响的数据行数
     */
    public int updateProductInOutMain(Map<String,Object> praMap);
    
    /**
     * 根据入出库单据修改库存。
     * @param praMap
     */
    public void changeStock(Map<String,Object> praMap);
    
    /**
     * 给定入出库单主ID，取得概要信息。
     * @param productInOutMainID
     * @return
     */
    public Map<String,Object> getProductInOutMainData(int productInOutMainID,String language);
    
    /**
     * 给定入出库单主ID，取得明细信息。
     * @param productInOutMainID
     * @return
     */
    public List<Map<String,Object>> getProductInOutDetailData(int productInOutMainID,String language);

    /**
     * 将产品入出库批次信息写入入出库批次主从表，并处理产品批次库存表（入库、出库）。
     * @param mainData
     * @param detailList
     * @return
     */
    public int handleProductInOutBatch(Map<String, Object> mainData, List<Map<String, Object>> detailList);    
  
}