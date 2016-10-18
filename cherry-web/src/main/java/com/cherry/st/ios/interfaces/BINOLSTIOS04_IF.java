/*
 * @(#)BINOLSTIOS04_IF.java     1.0 2011/9/28
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
package com.cherry.st.ios.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.ICherryInterface;

/**
 * 
 * 商品盘点Interface
 * 
 * @author niushunjie
 * @version 1.0 2011.9.28
 */
public interface BINOLSTIOS04_IF extends ICherryInterface{
    
    /**
     * 取类别属性List
     * @param map
     * @return
     */
    public List<Map<String,Object>> getPrtCatPropertyList(Map<String,Object> map);
    
    /**
     * 取第几级分类
     * @param map
     * @return
     */
    public List<Map<String,Object>> getPrtCatPropValueList(Map<String,Object> map);
    
    /**
     * 保存盘点信息
     * @param map
     * @param list
     * @param userinfo
     * @throws Exception
     */
    public int tran_save(Map<String,Object> map,List<String[]> list,UserInfo userinfo) throws Exception;

    /**
     * 取得产品列表（按批次盘点）
     * @param map
     * @return
     */
    public List<Map<String,Object>> getProductByBatchList(Map<String,Object> map);
    
    /**
     * 取得产品库存列表（非批次盘点）
     * @param map
     * @return
     */
    public List<Map<String,Object>> getProductStockList(Map<String,Object> map);

    /**
     * 暂存盘点信息
     * @param map
     * @param arrList
     * @param userInfo
     * @return
     * @throws Exception
     */
	public int tran_saveTemp(Map<String, Object> map, List<String[]> arrList,UserInfo userInfo) throws Exception;
}
