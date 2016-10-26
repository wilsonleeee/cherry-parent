/*  
 * @(#)BINOLPTJCS20_IF.java     1.0 2014/08/31      
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
package com.cherry.pt.jcs.interfaces;

import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

public interface BINOLPTJCS47_IF extends ICherryInterface{
	
	/**
	 * 修改产品价格方案主表
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int tran_updPrtPriceSolu(Map<String, Object> map) throws Exception;
	
}
