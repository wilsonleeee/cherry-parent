/*  
 * @(#)BINOLSTBIL14_IF.java     1.0 2012/7/24      
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
package com.cherry.st.bil.interfaces;


import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.ICherryInterface;
import com.cherry.st.bil.form.BINOLSTBIL14_Form;

/**
 * 
 * 产品退库申请单详细Interface
 * @author niushunjie
 * @version 1.0 2012.7.24
 */
public interface BINOLSTBIL14_IF extends ICherryInterface{
	
	/**工作流中的各种动作
	 * @param form
	 * @param userInfo
	 * @return
	 * @throws Exception 
	 */
	public void tran_doaction(BINOLSTBIL14_Form form, UserInfo userInfo) throws Exception;
	
	/**
	 * 保存
	 * @param map
	 */
	public int tran_save(BINOLSTBIL14_Form form, UserInfo userInfo) throws Exception;
	
	/**
     * 提交
     * @param map
     */
    public int tran_submit(BINOLSTBIL14_Form form, UserInfo userInfo) throws Exception;
}
