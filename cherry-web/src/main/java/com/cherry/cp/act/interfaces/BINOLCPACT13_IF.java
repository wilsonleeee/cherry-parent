/*	
 * @(#)BINOLCPACT12_IF.java     1.0 @2014-12-16		
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
package com.cherry.cp.act.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.ICherryInterface;

/**
 * 活动产品库存导入Interface
 * 
 * @author menghao
 * 
 */
public interface BINOLCPACT13_IF extends ICherryInterface {

	/**
	 * 解析活动产品库存数据
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> resolveExcel(Map<String, Object> map) throws Exception;

	/**
	 * 导入活动产品库存
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public void tran_saveImportCampaignStock(
			List<Map<String, Object>> list, Map<String, Object> map)
			throws Exception;
}
