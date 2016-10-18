/*  
 * @(#)BINOLBSRES04_BL.java     1.0 2011/05/31      
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
package com.cherry.bs.res.bl;

import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.res.interfaces.BINOLBSRES04_IF;
import com.cherry.bs.res.service.BINOLBSRES04_Service;

public class BINOLBSRES04_BL implements BINOLBSRES04_IF{

	@Resource(name="binOLBSRES04_Service")
	private BINOLBSRES04_Service binOLBSRES04_Service;
	
	@Override
	public int tran_insertRes(Map<String, Object> map) {
			return binOLBSRES04_Service.addRes(map);
	}
		
	@Override
	public String getCount(Map<String, Object> map) {
			return binOLBSRES04_Service.getCount(map);
		}
}