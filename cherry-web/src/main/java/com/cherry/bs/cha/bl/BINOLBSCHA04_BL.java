/*  
 * @(#)BINOLBSCHA04_BL.java     1.0 2011/05/31      
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
package com.cherry.bs.cha.bl;

import java.util.Map;
import javax.annotation.Resource;

import com.cherry.bs.cha.interfaces.BINOLBSCHA04_IF;
import com.cherry.bs.cha.service.BINOLBSCHA04_Service;
import com.cherry.ss.common.base.SsBaseBussinessLogic;

public class BINOLBSCHA04_BL extends SsBaseBussinessLogic implements BINOLBSCHA04_IF{
	/** 添加部门画面Service */
	@Resource
	private BINOLBSCHA04_Service binOLBSCHA04_Service;
	
	@Override
	public int tran_insertChannel(Map<String, Object> map) {
			return binOLBSCHA04_Service.addChannel(map);
	}
		
	@Override
	public String getCount(Map<String, Object> map) {
			return binOLBSCHA04_Service.getCount(map);
		}
}