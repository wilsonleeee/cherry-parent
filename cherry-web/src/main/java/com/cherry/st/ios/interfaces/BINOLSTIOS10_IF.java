/*
 * @(#)BINOLSTIOS10_IF.java     1.0 2013/08/16
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
import com.cherry.st.ios.form.BINOLSTIOS10_Form;
import com.cherry.st.sfh.form.BINOLSTSFH06_Form;

/**
 * 
 * 退库申请Interface
 * 
 * @author niushunjie
 * @version 1.0 2013.08.16
 */
public interface BINOLSTIOS10_IF extends ICherryInterface{
    
    /**
     * 进行退库申请处理
     * @param form
     * @throws Exception 
     * @throws Exception 
     */
    public int tran_submit(BINOLSTIOS10_Form form,UserInfo userInfo) throws Exception;
    
    /**
     * 保存退库申请单
     * @param form
     * @throws Exception 
     */
    public int tran_save(BINOLSTIOS10_Form form,UserInfo userInfo) throws Exception;
    
    public String getDepart(Map<String, Object> map);
    
    /**
     * 查出有效产品
     * @param map
     * @throws Exception 
     */
    public List<Map<String, Object>> searchProductList(Map<String, Object> map);
}
