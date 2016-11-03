/*
 * @(#)BINOTHONG01_IF.java     1.0 2014/09/04
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
package com.cherry.ot.hong.interfaces;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import com.cherry.cm.core.CherryBatchException;
import com.cherry.cm.core.ICherryInterface;
import com.cherry.mq.mes.common.CherryMQException;

/**
 * 
 * 宏巍电商订单获取 IF
 * 
 * 
 * @author niushunjie
 * @version 1.0 2014.09.04
 */
public interface BINOTHONG01_IF extends ICherryInterface{
    /**
     * 宏巍电商订单获取 Batch处理
     * @param map
     * @return
     * @throws Exception 
     */
    public int tran_batchOTHONG(Map<String, Object> map) throws CherryBatchException, Exception;

    /**
     * 将销售明细中的产品类型为BOM的商品进行拆分，将拆分后的商品加入到销售MQ明细集合中（注：电商信息表只记录原始信息不做拆分）
     * 
     * @param detailList
     * @return
     */
    public List<Map<String,Object>> splitBomPrt(Map<String,Object> erpOrder, List<Map<String,Object>> detailList);
    
    /**
     * 传入订单中商品相关信息取得智能促销返回的新增商品
     * @return
     * @throws ParseException,Exception  
     */
    @SuppressWarnings("unchecked")
	public List<Map<String,Object>> getSmartPromotionNewProList(Map<String,Object> paramMap,List<Map<String,Object>> detailList) throws ParseException,Exception;
    
    /**
     * 得到根据地址截取到的省市名称
     * @param address
     * @param orderNumber
     * @return
     */
    public Map<String,Object> getProvNameAndCityName(String address,String orderNumber);
    
    /**
     * 查询Sale.BIN_ESOrderDetail需要的值;
     * 根据barCode或unitcode(通过系统配置项确定 )来查询产品信息
     * @param paramMap
     * @return 产品的ID、UnitCode、SaleType信息，不会返回NULL（在新后台未找到产品，MAP中的BIN_ProductVendorID=NULL）
     * @throws CherryMQException 
     */
    public Map<String,Object> getESOrderDetailNeedValue(Map<String,Object> paramMap) throws Exception;
    
}