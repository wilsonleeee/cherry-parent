/*  
 * @(#)BINOLSTCM14_IF.java    1.0 2012.08.23
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opensymphony.workflow.InvalidInputException;
import com.opensymphony.workflow.WorkflowException;

/**
 * 
 * 产品盘点申请操作共通Interface
 * 
 * @author niushunjie
 * @version 1.0 2012.08.23
 */
public interface BINOLSTCM14_IF {
    /**
     * 将盘点申请信息插入数据库中；
     * @param mainData
     * @param detailList
     * @return
     */
    public int insertProStocktakeRequestAll(Map<String,Object> mainData,List<Map<String,Object>> detailList);
    
    /**
     * 修改盘点申请主表数据。
     * @param praMap
     * @return
     */
    public int updateProStocktakeRequest(Map<String,Object> praMap);
    
    /**
     * 给盘点申请单主ID，取得概要信息。
     * @param productOrderID
     * @return
     */
    public Map<String,Object> getProStocktakeRequestMainData(int proStocktakeRequestID,String language);
    
    /**
     * 给盘点申请单主ID，取得明细信息。
     * @param productOrderID
     * @return
     */
    public List<Map<String,Object>> getProStocktakeRequestDetailData(int proStocktakeRequestID,String language);
    
    /**
     * 根据表单创建盘点申请单
     * @param praMap
     * @return
     */
    public int createProStocktakeRequestByForm(Map<String,Object> praMap);
    
    /**
     * 根据盘点申请单创建盘点申请单
     * @param praMap
     * @return
     */
    public int createProStocktakeRequest(Map<String,Object> praMap);
    
    /**
     * 写入出库表，并修改库存（根据盘点单）
     * @param praMap
     */
    public void changeStockByCA(Map<String,Object> praMap);
    
    /**
     * 判断盘点申请单号存在
     * @param relevantNo
     * @return
     */
    public List<Map<String,Object>> selProStocktakeRequest(String relevantNo);
    
    /**
     * 发送MQ
     * @param proReturnRequestIDArr
     * @param pramMap
     */
    public void sendMQ(int[] proReturnRequestIDArr, Map<String,String> pramMap) throws Exception;
    
    /**
     * pos机确认盘点后，要来完成工作流
     * @param mainData
     * @throws WorkflowException 
     * @throws InvalidInputException 
     * @throws NumberFormatException 
     */
    public void posConfirmCAFinishFlow(Map<String, Object> mainData) throws Exception;

    /**
     * 清空盘点申请单明细数据
     * @param map
     */
	public void deleteProStocktakeRequestDetail(Map<String, Object> map);


    /**
     * 查询盘点申请单号关联的审核单信息
     * @param tradeNoIF
     * @return
     */
    public List<Map<String, Object>> selProStocktakeRequestCR(String tradeNoIF);
}
