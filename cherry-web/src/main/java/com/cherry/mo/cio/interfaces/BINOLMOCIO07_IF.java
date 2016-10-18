/*  
 * @(#)BINOLMOCIO07_IF.java     1.0 2011/05/31      
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
package com.cherry.mo.cio.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.cmbussiness.interfaces.BINOLCM37_IF;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.ICherryInterface;

public interface BINOLMOCIO07_IF extends BINOLCM37_IF{

	public int getSaleTargetCount(Map<String, Object> map);
	
	public List<Map<String, Object>> searchSaleTargetList(Map<String, Object> map);
	
	public void tran_setSaleTarget(Map<String, Object> map) throws CherryException;
	
	public void down(Map<String, Object> map)throws Exception;
	
	public List<Map<String,Object>> getTreeNodes(Map<String,Object> map) throws Exception;
	
	public Map<String, Object> resolveExcel(Map<String, Object> map)throws Exception;
	
	public Map<String, Object> getExportMap(Map<String, Object> map);
}
