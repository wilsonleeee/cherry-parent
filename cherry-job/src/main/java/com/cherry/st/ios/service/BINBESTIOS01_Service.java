package com.cherry.st.ios.service;

import java.util.List;
import java.util.Map;

import com.cherry.cm.service.BaseService;

/*  
 * @(#)BINBESTIOS01_Service.java    1.0 2012-2-21     
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

/**
 * 新后台与金蝶K3发货单/退库单数据同步
 * 
 * @author zhanggl
 * 
 * 
 * */

@SuppressWarnings("unchecked")
public class BINBESTIOS01_Service extends BaseService {

	/**
	 * 取出WITPOSA_invoice中Flag为1的数据
	 * 
	 * */
	public List<Map<String,Object>> getInvoiceMain(Map<String,Object> map){
		return ifServiceImpl.getList(map, "BINBESTIOS01.getInvoiceMain");
	}
	
	/**
	 * 将WITPOSA_invoice中的取出的数据废弃
	 * 
	 * */
	public int updateInvoiceMain(Map<String,Object> map){
		return ifServiceImpl.update(map, "BINBESTIOS01.updateInvoiceMain");
	}
	
	/**
	 * 从WITPOSA_invoice_detail表中取出指定的数据
	 * 
	 * */
	public List<Map<String,Object>> getInvoiceDetail(Map<String,Object> map){
		return ifServiceImpl.getList(map, "BINBESTIOS01.getInvoiceDetail");
	}
	
}
