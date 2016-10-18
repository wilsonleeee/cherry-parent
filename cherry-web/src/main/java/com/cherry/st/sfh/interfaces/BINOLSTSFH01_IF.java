/*  
 * @(#)BINOLSTSFH01_IF.java     1.0 2012/11/13      
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

import java.util.Map;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.ICherryInterface;
import com.cherry.st.sfh.form.BINOLSTSFH01_Form;

/**
 * 
 * 产品订货Interface
 * 
 * @author niushunjie
 * @version 1.0 2012.11.13
 */
public interface BINOLSTSFH01_IF extends ICherryInterface{
    
    /**
     * 进行订货处理
     * @param form
     * @throws Exception 
     * @throws Exception 
     */
    public int tran_order(BINOLSTSFH01_Form form,UserInfo userInfo) throws Exception;
    
    /**
     * 保存订货单
     * @param form
     * @throws Exception 
     */
    public int tran_saveOrder(BINOLSTSFH01_Form form,UserInfo userInfo) throws Exception;
    
    /**
     * 取业务日期
     * @param param
     * @return
     */
    public String getBusinessDate(Map<String,Object> param);
}
