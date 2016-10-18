/*  
 * @(#)BINOLBSCHA02_BL.java     1.0 2011/05/31      
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

import com.cherry.bs.cha.interfaces.BINOLBSCHA02_IF;
import com.cherry.bs.cha.service.BINOLBSCHA02_Service;
import com.cherry.ss.common.base.SsBaseBussinessLogic;


public class BINOLBSCHA02_BL extends SsBaseBussinessLogic implements BINOLBSCHA02_IF{
	@Resource
	private BINOLBSCHA02_Service binOLBSCHA02_Service;
	
	@Override
	public Map<String, Object> channelDetail(Map<String, Object> map) {
		return binOLBSCHA02_Service.channelDetail(map);
	}
}
