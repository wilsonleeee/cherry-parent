/*  
 * @(#)BINOLBSRES03_BL.java     1.0 2011/05/31      
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

import com.cherry.bs.res.interfaces.BINOLBSRES03_IF;
import com.cherry.bs.res.service.BINOLBSRES03_Service;
import com.cherry.cm.core.CherryException;
import com.cherry.ss.common.base.SsBaseBussinessLogic;

public class BINOLBSRES03_BL extends SsBaseBussinessLogic implements BINOLBSRES03_IF{
	
	@Resource
	private BINOLBSRES03_Service binOLBSRES03_Service;
	
	@Override
	public Map<String, Object> resellerDetail (Map<String, Object> map) {
		return binOLBSRES03_Service.resellerDetail(map);
   }

	@Override
	public void tran_updateReseller(Map<String, Object> map)  throws Exception{
		int result = binOLBSRES03_Service.updateReseller(map);
		if(result == 0){
			throw new CherryException("ECM00038");
		}
    }
	
	@Override
	public String getCount(Map<String, Object> map) {
		return binOLBSRES03_Service.getCount(map);
	}
}