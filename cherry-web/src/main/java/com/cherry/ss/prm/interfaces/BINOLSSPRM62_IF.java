/*
 * @(#)BINOLSSPRM62_IF.java     1.0 2012/09/27
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
package com.cherry.ss.prm.interfaces;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.ICherryInterface;
import com.cherry.ss.prm.form.BINOLSSPRM62_Form;

/**
 * 
 * 促销品移库详细IF
 * @author niushunjie
 * @version 1.0 2012.09.27
 */
public interface BINOLSSPRM62_IF extends ICherryInterface{       
    /**
     * 工作流中的各种动作
     * @param form
     * @param userInfo
     * @return
     * @throws Exception 
     */
    public void tran_doaction(BINOLSSPRM62_Form form, UserInfo userInfo) throws Exception;
}
