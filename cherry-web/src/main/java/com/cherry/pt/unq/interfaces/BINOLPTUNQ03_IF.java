/*  
 * @(#)BINOLPTUNQ03_IF    1.0 2016-06-17    
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
package com.cherry.pt.unq.interfaces;

import java.util.List;
import java.util.Map;

/**
 * 
 * 唯一码维护 IF
 * 
 * @author zw
 * @version 1.0 2016.06.17
 */
public interface BINOLPTUNQ03_IF{

	/**
	 * 
	 * 唯一码维护明细（导入）
	 *
	 * @param map
	 * @return
	 * @throws Exception 
	 */
	public Map<String, Object> resolveExcel(Map<String, Object> map) throws Exception;

	public byte[] exportExcel(Map<String, Object> map, List errorCounterList, int totalCount, int successCount, int failCount) throws Exception;

}
