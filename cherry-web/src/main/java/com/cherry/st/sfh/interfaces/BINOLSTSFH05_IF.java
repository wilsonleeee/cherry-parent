/*  
 * @(#)BINOLSTSFH05_IF.java     1.0 2011/09/14      
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

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.ICherryInterface;
import com.cherry.st.sfh.form.BINOLSTSFH05_Form;

/**
 * 
 * 产品发货单详细Interfaces
 * @author niushunjie
 * @version 1.0 2011.09.14
 */
public interface BINOLSTSFH05_IF extends ICherryInterface{
    /**工作流中的各种动作
     * @param form
     * @param userInfo
     * @return
     * @throws Exception 
     */
    public void tran_doaction(BINOLSTSFH05_Form form, UserInfo userInfo) throws Exception;
    
    /**
     * 提交发货单
     * 
     * */
    public int tran_submit(BINOLSTSFH05_Form form, UserInfo userInfo) throws Exception;
    
    /**
     * 保存发货单
     * 
     * */
    public void tran_save(BINOLSTSFH05_Form form, UserInfo userInfo) throws Exception;
    
    /**
     * 删除单据
     * @param form
     * @param userInfo
     * @return
     */
    public int tran_delete(BINOLSTSFH05_Form form, UserInfo userInfo) throws Exception;
    
    public int receiveDeliverByForm(BINOLSTSFH05_Form form,String currentUnit,int userID,int employeeID);
    
}
