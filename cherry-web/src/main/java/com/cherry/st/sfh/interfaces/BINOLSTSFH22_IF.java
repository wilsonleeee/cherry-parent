/*  
 * @(#)BINOLSTSFH22_IF.java     1.0 2012/11/13      
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
package com.cherry.st.sfh.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.ICherryInterface;
import com.cherry.st.sfh.form.BINOLSTSFH22_Form;

/**
 * 
 * 订货（浓妆淡抹）Interface
 * 
 * @author zw
 * @version 1.0 2016.09.07
 */
public interface BINOLSTSFH22_IF extends ICherryInterface{
    
    /**
     * 进行订货处理
     * @param form
     * @throws Exception 
     * @throws Exception 
     */
    public int tran_order(BINOLSTSFH22_Form form,UserInfo userInfo) throws Exception;
    
    /**
     * 保存订货单
     * @param form
     * @throws Exception 
     */
    public int tran_saveOrder(BINOLSTSFH22_Form form,UserInfo userInfo) throws Exception;
    
    /**
     * 取业务日期
     * @param param
     * @return
     */
    public String getBusinessDate(Map<String,Object> param);

    /**
     * 取得发货方部门地址
     * @param param
     * @return
     */
	public String getDefaultAddress(Map<String,Object> param);
	
    /**
     * 取得建议发货数据
     * @param param
     * @return
     */
	public List<Map<String, Object>> getSuggestProductByAjax(Map<String, Object> map);

    /**
     * 修改订单状态
     * @param param
     * @return
     */
	public int updateOrderStatus(Map<String, Object> paramMap);

    /**
     * 更具单号获取单据信息
     * @param param
     * @return
     */
	public Map<String, Object> getOrderInfo(Map<String, Object> map);

    /**
     * 取得订货方的NodeId
     * @param param
     * @return
     */
	public String getNodeId(Map<String, Object> map);
}
