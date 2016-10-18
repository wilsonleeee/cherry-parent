/*	
 * @(#)BINOLPTRPS14_IF.java     1.0 2011/03/18		
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
package com.cherry.pt.rps.interfaces;

import java.util.List;
import java.util.Map;

import com.cherry.cm.core.ICherryInterface;
/**
 * 销售记录吸纳关系查询BL接口
 * 
 * @author zgl
 * @version 1.0 2011.03.18
 *
 */
public interface BINOLPTRPS14_IF extends ICherryInterface {

	//获取销售记录单据相信信息
	public Map<String,Object> getSaleRecordDetail(Map<String,Object> map);
	//获取销售记录单据中所有商品详细信息
	public List<Map<String,Object>> getSaleRecordProductDetail(Map<String,Object> map);
	//获取操作员姓名
	public Map<String,Object> getEmployeeName(Map<String,Object> map);
	//获取支付方式详细信息
	public List<Map<String,Object>> getPayTypeDetail(Map<String,Object> map);
}
