/*  
 * @(#)BINOLBSPOS99_BL.java     1.0 2011/05/31      
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
package com.cherry.bs.pos.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.pos.service.BINOLBSPOS99_Service;

public class BINOLBSPOS99_BL {
	
	
	@Resource
	private BINOLBSPOS99_Service binOLBSPOS99_Service;
	
	/**
	 * 取得员工总数
	 * 
	 * @param map 查询条件
	 * @return 员工总数
	 */
	public int getEmployeeCount(Map<String, Object> map) {
		
		// 取得员工总数
		return binOLBSPOS99_Service.getEmployeeCount(map);
	}
	
	/**
	 * 取得员工List
	 * 
	 * @param map 查询条件
	 * @return 员工List
	 */
	public List<Map<String, Object>> getEmployeeList(Map<String, Object> map) {
		
		// 取得员工List
		return binOLBSPOS99_Service.getEmployeeList(map);
	}

}
