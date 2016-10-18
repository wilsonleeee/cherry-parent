/*  
 * @(#)BINOLSTCM13_IF.java    1.0 2012.07.24
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

import com.opensymphony.workflow.InvalidInputException;
import com.opensymphony.workflow.WorkflowException;

/**
 * 
 * 产品退库申请单操作共通Interface
 * 
 * @author niushunjie
 * @version 1.0 2012.07.24
 */
public interface BINOLSTCM13_IF {
    /**
     * 将退库申请信息插入数据库中；
     * @param mainData
     * @param detailList
     * @return
     */
    public int insertProReturnRequestAll(Map<String,Object> mainData,List<Map<String,Object>> detailList);
    
    /**
     * 修改退库申请主表数据。
     * @param praMap
     * @return
     */
    public int updateProReturnRequest(Map<String,Object> praMap);
    
    /**
     * 给退库申请单主ID，取得概要信息。
     * @param productOrderID
     * @return
     */
    public Map<String,Object> getProReturnRequestMainData(int proReturnRequestID,String language);
    
    /**
     * 给退库申请单主ID，取得明细信息。
     * @param productOrderID
     * @return
     */
    public List<Map<String,Object>> getProReturnReqDetailData(int proReturnRequestID,String language,Map<String,Object> otherParam);
    
    /**
     * 根据表单创建退库申请单
     * @param praMap
     * @return
     */
    public int createProReturnRequestByForm(Map<String,Object> praMap);
    
    /**
     * 根据退库申请单创建退库申请单
     * @param praMap
     * @return
     */
    public int createProReturnRequest(Map<String,Object> praMap);
    
    /**
     * K3创建退库申请单
     * @param praMap
     * @return
     */
    public int createProReturnRequest_K3(Map<String,Object> praMap);
    
    /**
     * 写入出库表，并修改库存
     * @param praMap
     */
    @Deprecated
    public void changeStock(Map<String,Object> praMap);
    
    /**
     * 写入出库表，并修改库存（根据退库单）
     * @param praMap
     */
    public void changeStockByRR(Map<String,Object> praMap);
    
    /**
     * 判断退库申请单号存在
     * @param relevantNo
     * @return
     */
    public List<Map<String,Object>> selProReturnRequest(String relevantNo);
    
    /**
     * 查询退库申请单据(RJ)根据关联单号
     * @param map
     * @return
     */
    public List<Map<String,Object>> selPrtReturnReqListByRelevanceNo(Map<String, Object> map);
    
    /**
     * 发送MQ
     * @param proDeliverIDArr
     * @param pramMap
     */
    public void sendMQ(int[] proDeliverIDArr, Map<String,String> pramMap) throws Exception;
    
    /**
     * pos机确认退库后，要来完成工作流
     * @param mainData
     * @throws WorkflowException 
     * @throws InvalidInputException 
     * @throws NumberFormatException 
     */
    public void posConfirmReturnFinishFlow(Map<String, Object> mainData) throws Exception;
}
