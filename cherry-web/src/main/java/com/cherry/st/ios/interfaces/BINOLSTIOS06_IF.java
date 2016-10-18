/*
 * @(#)BINOLSTIOS06_IF.java     1.0 2012/11/27
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

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.ICherryInterface;
import com.cherry.st.ios.form.BINOLSTIOS06_Form;

/**
 * 
 * 产品调拨Interface
 * 
 * @author niushunjie
 * @version 1.0 2011.11.27
 */
public interface BINOLSTIOS06_IF extends ICherryInterface{
    
    /**
     * 保存调拨信息
     * @param map
     * @param list
     * @param userinfo
     * @throws Exception
     */
    public int tran_save(BINOLSTIOS06_Form form, UserInfo userInfo) throws Exception;

    /**
     * 进行调拨
     * @param map
     * @param list
     * @param userinfo
     * @throws Exception
     */
    public int tran_submit(BINOLSTIOS06_Form form, UserInfo userInfo) throws Exception;
}