/*  
 * @(#)LogicInventorySynchro_BL.java    1.0 2011-9-21     
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
package com.cherry.synchro.st.bl;

import java.util.Map;

import javax.annotation.Resource;


import com.cherry.cm.core.CherryException;
import com.cherry.synchro.st.interfaces.LogicInventorySynchro_IF;
import com.cherry.synchro.st.service.LogicInventorySynchroService;


public class LogicInventorySynchro implements LogicInventorySynchro_IF {

	@Resource
	private LogicInventorySynchroService logicInventorySynchroService;
	
	@Override
	public void synchroLogicInventory(Map<String, Object> paramMap) throws Exception{
		// TODO Auto-generated method stub
		try{
			paramMap.put("Result", "OK");
			logicInventorySynchroService.synchroLogicInventory(paramMap);
			String ret = String.valueOf(paramMap.get("Result"));
			if(!"OK".equals(ret)){
				CherryException cex = new CherryException("ECM00035");
				cex.setErrMessage(cex.getErrMessage()+ret);
				throw cex;
			}
		}catch(CherryException ex){
			throw ex;
		} catch (Exception ex) {
			CherryException cex = new CherryException("ECM00035", ex);
			cex.setErrMessage(cex.getErrMessage() + ex.getMessage());
			throw cex;			
		}
	}

}
