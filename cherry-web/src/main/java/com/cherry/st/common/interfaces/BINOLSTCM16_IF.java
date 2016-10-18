/*  
 * @(#)BINOLSTCM16_IF.java    1.0 2012.11.27
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
 * 产品调拨操作共通Interface
 * 
 * @author niushunjie
 * @version 1.0 2012.11.17
 */
public interface BINOLSTCM16_IF extends ICherryInterface{
    /**
     * 将产品调拨申请信息插入数据库中；
     * @param mainData
     * @param detailList
     * @return
     */
    public int insertProductAllocationAll(Map<String,Object> mainData,List<Map<String,Object>> detailList);
    
    /**
     * 修改产品调拨申请表主表数据。
     * @param praMap
     * @return
     */
    public int updateProductAllocation(Map<String,Object> praMap);
    
    /**
     * 插入产品调拨申请表明细数据。
     * @param praMap
     * @return
     */
    public void insertProductAllocationDetail(int billID,List<Map<String,Object>> detailList);
    
    /**
     * 给产品调拨申请表主ID，取得概要信息。
     * @param productOrderID
     * @return
     */
    public Map<String,Object> getProductAllocationMainData(int productAllocationID,String language);
    
    /**
     * 给产品调拨申请表主ID，取得明细信息。
     * @param productOrderID
     * @return
     */
    public List<Map<String,Object>> getProductAllocationDetailData(int productAllocationID,String language);
    
    /**
     * 将产品调出信息插入数据库中；
     * @param mainData
     * @param detailList
     * @return
     */
    public int insertProductAllocationOutAll(Map<String,Object> mainData,List<Map<String,Object>> detailList);
    
    /**
     * 修改产品调出表主表数据。
     * @param praMap
     * @return
     */
    public int updateProductAllocationOut(Map<String,Object> praMap);
    
    /**
     * 插入产品调出表明细数据。
     * @param praMap
     * @return
     */
    public void insertProductAllocationOutDetail(int billID,List<Map<String,Object>> detailList);
    
//    /**
//     * 更新产品调出表明细。（先删后插）
//     * @param praMap
//     * @return
//     */
//    public void updateProductAllocationOutDetail(Map<String,Object> praMap);
    
    /**
     * 给产品调出表主ID，取得概要信息。
     * @param productOrderID
     * @return
     */
    public Map<String,Object> getProductAllocationOutMainData(int productAllocationOutID,String language);
    
    /**
     * 给产品调出表主ID，取得明细信息。
     * @param productOrderID
     * @return
     */
    public List<Map<String,Object>> getProductAllocationOutDetailData(int productAllocationOutID,String language);
    
    /**
     * 将产品调入信息插入数据库中；
     * @param mainData
     * @param detailList
     * @return
     */
    public int insertProductAllocationInAll(Map<String,Object> mainData,List<Map<String,Object>> detailList);
    
    /**
     * 给产品调入表主ID，取得概要信息。
     * @param productOrderID
     * @return
     */
    public Map<String,Object> getProductAllocationInMainData(int productAllocationInID,String language);
    
    /**
     * 给产品调入表主ID，取得明细信息。
     * @param productOrderID
     * @return
     */
    public List<Map<String,Object>> getProductAllocationInDetailData(int productAllocationInID,String language);
    
    /**
     * 写入出库表，并修改库存
     * @param praMap
     */
    public void changeStock(Map<String,Object> praMap);
    
    /**
     * 根据调拨Form创建调出/调入单
     * @param praMap
     * @return
     */
    public int createProductAllocationByForm(Map<String, Object> praMap);
    
//    /**
//     * 根据调拨申请单创建调出/调入单
//     * @param praMap
//     * @return
//     */
//    public int createProductAllocation(Map<String, Object> praMap);
    
    /**
     * 根据调出单创建调入单
     * @param praMap
     * @return
     */
    public int createProductAllocationInByOut(Map<String, Object> praMap);
    
    /**
     * 发送MQ
     * @param billIDArr
     * @param pramMap
     */
    public void sendMQ(int[] billIDArr, Map<String,String> pramMap) throws Exception;
    
    /**
     * 判断调出单号存在
     * @param relevantNo
     * @return
     */
    public List<Map<String,Object>> selProductAllocationOut(String billNo);
}