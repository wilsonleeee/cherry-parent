/*  
 * @(#)BINOLSTBIL10_IF.java     1.0 2011/05/31      
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
import com.cherry.st.bil.form.BINOLSTBIL10_Form;

public interface BINOLSTBIL10_IF extends ICherryInterface{
	
	public Map<String, Object> searchTakingInfo(Map<String, Object> map);
	
	public List<Map<String, Object>> searchTakingDetailList(Map<String, Object> map);
	
	/**
	 * 保存盘点单
     * @param form
     * @param userInfo
     * @return
     */
    public int tran_save(BINOLSTBIL10_Form form, UserInfo userInfo) throws Exception;
    
    /**
     * 提交盘点单
     * @param form
     * @param userInfo
     * @return
     */
    public int tran_submit(BINOLSTBIL10_Form form, UserInfo userInfo)throws Exception;
    
    /**
     * 删除盘点单
     * @param form
     * @param userInfo
     * @return
     */
    public int tran_delete(BINOLSTBIL10_Form form, UserInfo userInfo) throws Exception;
    
    /**工作流中的各种动作
     * @param form
     * @param userInfo
     * @return
     * @throws Exception 
     */
    public void tran_doaction(BINOLSTBIL10_Form form, UserInfo userInfo) throws Exception;
    
    /**
     * 汇总信息
     * 
     * @param map
     * @return
     */
    public Map<String, Object> getSumInfo(Map<String, Object> map);
}
