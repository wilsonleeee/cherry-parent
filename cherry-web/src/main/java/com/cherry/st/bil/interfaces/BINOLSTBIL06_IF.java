/*  
 * @(#)BINOLSTBIL06_IF.java     1.0 2011/10/20      
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
import com.cherry.st.bil.form.BINOLSTBIL06_Form;

/**
 * 
 * 产品报损单详细IF
 * @author niushunjie
 * @version 1.0 2011.10.20
 */
public interface BINOLSTBIL06_IF extends ICherryInterface{
	
	/**
     * @param form
     * @param userInfo
	 * @return
	 */
	public int tran_save(BINOLSTBIL06_Form form, UserInfo userInfo) throws Exception;
	
	/**
     * 提交报损单
     * @param form
     * @param userInfo
     * @return
     */
	public int tran_submit(BINOLSTBIL06_Form form, UserInfo userInfo)throws Exception;
	
	/**
	 * 删除报损单
	 * @param form
	 * @param userInfo
	 * @return
	 */
	public int tran_delete(BINOLSTBIL06_Form form, UserInfo userInfo) throws Exception;
	
	/**工作流中的各种动作
	 * @param form
	 * @param userInfo
	 * @return
	 * @throws Exception 
	 */
	public void tran_doaction(BINOLSTBIL06_Form form, UserInfo userInfo) throws Exception;
}
