/*  
 * @(#)BINOLSTBIL12_IF.java     1.0 2011/11/2      
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

import java.util.List;
import java.util.Map;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.ICherryInterface;
import com.cherry.st.bil.form.BINOLSTBIL12_Form;

/**
 * 
 * 产品退库单详细IF
 * @author niushunjie
 * @version 1.0 2011.11.2
 */
public interface BINOLSTBIL12_IF extends ICherryInterface{
	
	/**
     * @param form
     * @param userInfo
	 * @return
	 */
	public int tran_save(BINOLSTBIL12_Form form, UserInfo userInfo) throws Exception;
	
	/**
     * 提交退库单
     * @param form
     * @param userInfo
     * @return
     */
	public int tran_submit(BINOLSTBIL12_Form form, UserInfo userInfo)throws Exception;
	
	/**
	 * 删除退库单
	 * @param form
	 * @param userInfo
	 * @return
	 */
	public int tran_delete(BINOLSTBIL12_Form form, UserInfo userInfo) throws Exception;
	
	/**工作流中的各种动作
	 * @param form
	 * @param userInfo
	 * @return
	 * @throws Exception 
	 */
	public void tran_doaction(BINOLSTBIL12_Form form, UserInfo userInfo) throws Exception;
	
	/**
	 * 取得退库仓库，接受退库仓库
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> getInventoryInfo(Map<String,Object> mainData,List<Map<String,Object>> detailList) throws Exception;
}
