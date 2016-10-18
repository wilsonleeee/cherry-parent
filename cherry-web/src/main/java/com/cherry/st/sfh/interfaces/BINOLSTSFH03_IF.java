/*  
 * @(#)BINOLSTSFH03_IF.java     1.0 2011/09/09      
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
import com.cherry.st.sfh.form.BINOLSTSFH03_Form;

public interface BINOLSTSFH03_IF extends ICherryInterface{
	
	/**
     * @param form
     * @param userInfo
	 * @return
	 */
	public int tran_save(BINOLSTSFH03_Form form, UserInfo userInfo) throws Exception;
	
	/**
	 * 暂存订货单
	 * @param form
	 * @param userInfo
	 * @return
	 * @throws Exception
	 */
	public int tran_saveTemp(BINOLSTSFH03_Form form, UserInfo userInfo) throws Exception;
	
	/**
     * 提交订单
     * @param form
     * @param userInfo
     * @return
     */
	public int tran_submit(BINOLSTSFH03_Form form, UserInfo userInfo)throws Exception;
	
	/**
	 * 删除订单
	 * @param form
	 * @param userInfo
	 * @return
	 */
	public int tran_delete(BINOLSTSFH03_Form form, UserInfo userInfo) throws Exception;
	
	/**工作流中的各种动作
	 * @param form
	 * @param userInfo
	 * @return
	 * @throws Exception 
	 */
	public void tran_doaction(BINOLSTSFH03_Form form, UserInfo userInfo) throws Exception;
	
	/**
	 * 取得安全库存天数
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> getSaleQuantity(Map<String,Object> paramMap);
	
	   /**
     * 取得近30天销量
     * @param paramMap
     * @return
     */
    public List<Map<String,Object>> getLowestStockDays(Map<String,Object> paramMap);
    
    /**
     * 取得近30天销量
     * @param paramMap
     * @return
     */
    public List<Map<String,Object>> getAdtCoefficient(Map<String,Object> paramMap);
    
    /**
     * 根据订货单号取得关联的发货单主ID
     * @param map
     * @return
     */
    public String getDeliverIDByWorkFlowID(Map<String, Object> map);
}
