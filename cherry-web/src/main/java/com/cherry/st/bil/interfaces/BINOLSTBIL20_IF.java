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


import java.util.List;
import java.util.Map;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.core.ICherryInterface;
import com.cherry.st.bil.form.BINOLSTBIL14_Form;
import com.cherry.st.bil.form.BINOLSTBIL20_Form;

/**
 * 
 * 销售退货申请单详细Interface
 * @author nanjunbo
 * @version 1.0 2016.08.29
 */
public interface BINOLSTBIL20_IF extends ICherryInterface{
	
	/**工作流中的各种动作
	 * @param form
	 * @param userInfo
	 * @return
	 * @throws Exception 
	 */
	public void tran_doaction(BINOLSTBIL20_Form form, UserInfo userInfo) throws Exception;
	
	/**
	 * 获取销售退货申请单的主表信息
	 * @param map
	 * @return
	 */
	public Map<String, Object> searchSaleRerurnRequestInfo(Map<String, Object> map);
	
	/**
	 * 获取销售退货申请单的详细信息
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> searchSaleRerurnRequestDetailList(Map<String, Object> map);
	
	/**
	 * 获取销售退货单的支付信息
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> getPayTypeDetail(Map<String,Object> map);
}
