/*  
 * @(#)DroolsFlow.java     1.0 2011/05/31      
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
package com.cherry.cm.drools;

import java.util.HashMap;
import java.util.Map;

import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.process.ProcessInstance;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.ss.common.bl.BINOLSSCM01_BL;

public class DroolsFlow {

	public static void startPromotionDeliverFlow(Map<String,Object> argMap,BINOLSSCM01_BL binOLSSCM01_BL) {		
		StatefulKnowledgeSession ksession = StatefulKSessionFactory.getStatefulKSession();

		// start a new process instance
		UserInfo userInfo = (UserInfo)argMap.get("UserInfo");
		Map<String,Object> tempMap = new HashMap<String,Object>();
		tempMap.put("PromotionInventoryLogID",argMap.get("PromotionInventoryLogID"));
		tempMap.put("UserName",userInfo.getLoginName());
		tempMap.put("BINOLSSCM01BL",binOLSSCM01_BL);
		ProcessInstance instance = ksession.startProcess("PromotionReceivingFlow_01",tempMap);
		ksession.insert(instance);
		ksession.fireAllRules();
		// logger.close();
	
	
	}
}
