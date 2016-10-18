/*  
 * @(#)BINOLPTJCS21_IF.java     1.0 2014/08/31      
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

public interface BINOLPTJCS21_IF extends ICherryInterface{
	
	/**
	 * 保存产品价格方案主表
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int tran_addPrtPriceSolu(Map<String, Object> map) throws Exception;

	public String getCount(Map<String, Object> map);
	
	/**
	 * 获取方案CODE
	 * @param map
	 * @return
	 */
	public String getSolutionCode(Map<String,Object> map);

}
