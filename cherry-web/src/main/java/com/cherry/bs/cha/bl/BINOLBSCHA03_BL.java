/*  
 * @(#)BINOLBSCHA03_BL.java     1.0 2011/05/31      
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

import com.cherry.bs.cha.interfaces.BINOLBSCHA03_IF;
import com.cherry.bs.cha.service.BINOLBSCHA03_Service;
import com.cherry.cm.core.CherryException;
import com.cherry.ss.common.base.SsBaseBussinessLogic;

public class BINOLBSCHA03_BL extends SsBaseBussinessLogic implements BINOLBSCHA03_IF{
	@Resource
	private BINOLBSCHA03_Service binOLBSCHA03_Service;
	
	@Override
	public Map<String, Object> channelDetail(Map<String, Object> map) {
		return binOLBSCHA03_Service.channelDetail(map);
   }

	@Override
	public void tran_updateChannel(Map<String, Object> map)  throws Exception{
		int result = binOLBSCHA03_Service.updateChannel(map);
		if(result == 0){
			throw new CherryException("ECM00038");
		}
    }
	
	@Override
	public String getCount(Map<String, Object> map) {
		return binOLBSCHA03_Service.getCount(map);
	}
}